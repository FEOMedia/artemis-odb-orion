package se.feomedia.orion;

import com.artemis.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import se.feomedia.orion.system.OperationSystem;

/**
 * A hierarchical data structure operating on a single executorType. A node's
 * value can be <code>null</code>.
 */
public class OperationTree {
	private final Array<OperationTree> children;

	private OperationTree parent;
	private Operation operation;
	private transient Executor<?> executor;

	protected OperationTree() {
		this(null, null);
	}

	public OperationTree(Operation operation) {
		this(null, operation);
	}

	private OperationTree(OperationTree parent, Operation operation) {
		if (parent != null)
			parent.children.add(this);

		children = new Array<>(OperationTree.class);
		this.parent = parent;
		this.operation = operation;
	}

	public float act(float delta) {
		return executor.process(delta, operation, this);
	}

	public boolean isComplete() {
		return operation.isComplete();
	}

	public void add(OperationTree child) {
		child.parent = this;
		children.add(child);
	}

	/**
	 * Gets the distance between the current node and the root node.
	 *
	 * @return Distance to root node.
	 */
	public int getDepth() {
		int depth = 0;
		OperationTree t = this;
		while ((t = t.parent) != null) {
			depth++;
		}

		return depth;
	}

	@Override
	public String toString() {
		StringBuilder indent = new StringBuilder();
		OperationTree t = this;
		while ((t = t.parent) != null) {
			indent.append("   ");
		}

		return String.format("value=%s (%s)", executor, children);
	}

	public Array<OperationTree> children() {
		return children;
	}

	public void initialize(World world) {
		initialize(world.getSystem(OperationSystem.class));
	}

	protected void initialize(OperationSystem operations) {
		executor = operations.getExecutor(operation);
		for (int i = 0, s = children.size; s > i ; i++) {
			children.get(i).initialize(operations);
		}
	}

	public void reset() {
		reset(this);
	}

	private void reset(OperationTree ot) {
		Pools.free(ot.operation);
		ot.operation = null;
		for (OperationTree node : ot.children) {
			reset(node);
		}
	}
}
