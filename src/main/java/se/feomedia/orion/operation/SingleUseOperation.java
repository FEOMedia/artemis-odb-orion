package se.feomedia.orion.operation;

import com.artemis.annotations.Wire;
import se.feomedia.orion.Executor;
import se.feomedia.orion.Operation;
import se.feomedia.orion.OperationTree;

public abstract class SingleUseOperation extends Operation {
	boolean hasRun;

	@Override
	protected boolean isComplete() {
		return hasRun;
	}

	@Override
	public void reset() {
		hasRun = false;
	}

	@Wire
	public abstract static class SingleUseExecutor<T extends SingleUseOperation> extends Executor<T> {
		@Override
		protected final float act(float delta, T op, OperationTree node) {
			op.hasRun = true;
			act(op, node);
			return delta;
		}

		protected abstract void act(T op, OperationTree node);
	}
}
