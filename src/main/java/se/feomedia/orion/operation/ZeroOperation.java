package se.feomedia.orion.operation;

import com.artemis.annotations.Wire;
import se.feomedia.orion.Executor;
import se.feomedia.orion.Operation;
import se.feomedia.orion.OperationTree;

public class ZeroOperation extends Operation {

	@Override
	public Class<? extends Executor> executorType() {
		return ZeroExecutor.class;
	}

	@Override
	protected boolean isComplete() {
		return true;
	}

	@Override
	public void reset() {}

	@Wire
	public static class ZeroExecutor extends Executor<ZeroOperation> {
		@Override
		protected float act(float delta, ZeroOperation operation, OperationTree node) {
			// this will never run, but the executor must still be resolved by
			// the OperationSystem.
			return delta;
		}
	}
}
