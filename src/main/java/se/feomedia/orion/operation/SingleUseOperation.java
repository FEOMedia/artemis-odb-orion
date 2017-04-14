package se.feomedia.orion.operation;

import com.artemis.annotations.Wire;
import se.feomedia.orion.Executor;
import se.feomedia.orion.Operation;
import se.feomedia.orion.OperationTree;

public abstract class SingleUseOperation extends Operation {
	@Wire
	public abstract static class SingleUseExecutor<T extends SingleUseOperation> extends Executor<T> {
		@Override
		protected final float act(float delta, T op, OperationTree node) {
			act(op, node);
			op.completed = true;
			return delta;
		}

		protected abstract void act(T op, OperationTree node);
	}
}
