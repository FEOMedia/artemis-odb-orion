package se.feomedia.orion;

import com.artemis.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import se.feomedia.orion.system.OperationSystem;

public class OperationTree {

	Operation operation;

	private transient final Array<OperationTree> children;
	private transient OperationTree parent;
	private transient Executor<?> executor;

	private static final Friend friend = new Friend();

	private static final Pool<OperationTree> pool = new Pool<OperationTree>() {
		@Override
		protected OperationTree newObject() {
			return new OperationTree();
		}
	};

	static OperationTree obtain(Operation operation) {
		OperationTree tree = pool.obtain();
		tree.operation = operation;

		return tree;
	}

	private OperationTree() {
		children = new Array<>(true, 8);
	}

	public OperationTree parent() {
		return parent;
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
		OperationTree ot = parent;
		while (ot != null) {
			if (indent.length() == 0) indent.append("\n");
			indent.append("    ");
			ot = ot.parent;
		}

		StringBuilder children = new StringBuilder();
		if (this.children.size > 0) {
			for (OperationTree child : this.children) {
				children.append(child.toString());
			}
		}

		return String.format(indent + "%s:%s%s",
			operation.getClass().getSimpleName(),
			executor.getClass().getSimpleName(),
			children);
	}

	public Array<OperationTree> children() {
		return children;
	}

	public void initialize(World world, int entityId, OperationSystem.Friend friend) {
		friend.hashCode();
		initialize(world.getSystem(OperationSystem.class), entityId);
	}

	void initialize(OperationSystem operations, int entityId) {
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
		OperationFactory.free(ot.operation);
		ot.operation = null;
		ot.parent = null;
		ot.executor = null;

		for (OperationTree node : ot.children) {
			clear(node);
		}
		ot.children.clear();

		pool.free(ot);
	}

	public static final class Friend {
		private Friend() {}
	}
}
