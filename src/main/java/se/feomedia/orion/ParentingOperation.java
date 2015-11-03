package se.feomedia.orion;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.utils.Array;
import se.feomedia.orion.operation.NullOperation;

import static java.lang.Math.min;
import static se.feomedia.orion.OperationFactory.operation;

public abstract class ParentingOperation extends Operation {
	public boolean completed;

	protected Array<Operation> operations = new Array<>();

	@Override
	protected boolean isComplete() {
		return completed;
	}

	public void addChild(Operation op) {
		if (op == null)
			op = operation(NullOperation.class);

		operations.add(op);
	}

	@Override
	protected OperationTree toNode() {
		OperationTree tree = super.toNode();
		for (int i = 0; i < operations.size; i++) {
			Operation operation = operations.get(i);
			tree.add(operation.toNode());
		}

		return tree;
	}

	@Override
	public void reset() {
		operations.clear();
		completed = false;
	}

	@Wire
	public abstract static class ParentingExecutor<T extends ParentingOperation> extends Executor<T> {
		protected final float end(float delta, ParentingOperation op) {
			op.completed = op.completed || delta > 0;
			return min(0f, delta);
		}
	}
}