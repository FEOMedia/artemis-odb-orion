package se.feomedia.orion;

import com.artemis.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import se.feomedia.orion.system.OperationSystem;

public class OperationTree {
	private final Array<OperationTree> children;

	private OperationTree parent;
	private Operation operation;
	private transient Executor<?> executor;

	private static final Friend friend = new Friend();

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

	public void initialize(World world, int entityId, OperationSystem.Friend friend) {
		initialize(world.getSystem(OperationSystem.class), entityId);
	}

	private void initialize(OperationSystem operations, int entityId) {
		executor = operations.getExecutor(operation, friend);

		operation.entityId = entityId;
		operation.started = false;
		for (int i = 0, s = children.size; s > i ; i++) {
			children.get(i).initialize(operations, entityId);
		}
	}

	public void clear() {
		clear(this);
	}

	private void clear(OperationTree ot) {
		Pools.free(ot.operation);
		ot.operation = null;
		for (OperationTree node : ot.children) {
			clear(node);
		}
	}

	public static final class Friend {
		private Friend() {}
	}
}
