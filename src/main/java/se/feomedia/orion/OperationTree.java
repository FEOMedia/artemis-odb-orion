package se.feomedia.orion;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import se.feomedia.orion.system.OperationSystem;

/**
 * <p>The registered representation of an operation. Each node holds
 * on operation and executor pair. The entire tree is automatically
 * reclaimed once the root operation finishes.</p>
 *
 * <p>Operations are allowed to graft new nodes through their executors
 * (but tread carefully). Manipulating previously processed operations
 * is harder, as most base types cache completion status.</p>
 */
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
		for (OperationTree child : this.children) {
			children.append(child.toString());
		}

		String op = (operation != null)
			? operation.getClass().getSimpleName()
			: "<operation:null>";
		String ex = (executor != null)
			? executor.getClass().getSimpleName()
			: "<executor:null>";
		return String.format("%s%s:%s%s", indent, op, ex, children);
	}

	public Array<OperationTree> children() {
		return children;
	}

	public void initialize(OperationSystem operations, int entityId) {
		executor = operations.getExecutor(operation, friend);

		operation.entityId = entityId;
		operation.started = false;
		for (int i = 0, s = children.size; s > i ; i++) {
			children.get(i).initialize(operations, entityId);
		}
	}

	public void clear() {
		Array<Operation> cleared = MultiPool.toRemoveArray();
		clear(this, cleared);

		MultiPool.free(cleared);
	}

	private void clear(OperationTree ot, Array<Operation> toRemove) {
		toRemove.add(ot.operation);
		ot.operation = null;
		ot.parent = null;
		ot.executor = null;

		if (ot.children.size > 0) {
			for (OperationTree node : ot.children) {
				clear(node, toRemove);
			}
			ot.children.clear();
		}

		pool.free(ot);
	}

	/**
	 * Finds all operations. Only this and child nodes are scanned.
	 *
	 * @param type Operation type
	 * @param out Result array, be sure to clear before.
	 * @return all operations matching requested type
	 */
	public <T extends Operation> Array<T> findAll(Class<T> type, Array<T> out) {
		Operation op = this.operation;
		if (isSameType(type, op))
			out.add((T) op);

		for (int i = 0, s = children.size; s > i; i++)
			children.get(i).findAll(type, out);

		return out;
	}

	private static <T extends Operation> boolean isSameType(Class<T> type, Operation op) {
		return type.isAssignableFrom(op.getClass());
	}

	public static final class Friend {
		private Friend() {}
	}
}
