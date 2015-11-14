package se.feomedia.orion.operation;

import se.feomedia.orion.Executor;
import se.feomedia.orion.Operation;
import se.feomedia.orion.OperationTree;

public class NullOperation extends Operation {

	@Override
	public Class<? extends Executor> executorType() {
		return NullExecutor.class;
	}

	@Override
	protected boolean isComplete() {
		return true;
	}

	@Override
	public void reset() {}

	public static class NullExecutor extends Executor<NullOperation> {
		@Override
		protected float act(float delta, NullOperation operation, OperationTree node) {
			// this will never run, but the executor must still be resolved by
			// the OperationSystem.
			return delta;
		}
	}
}
