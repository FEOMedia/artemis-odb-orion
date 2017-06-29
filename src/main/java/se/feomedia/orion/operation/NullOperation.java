package se.feomedia.orion.operation;

import com.artemis.annotations.Wire;
import se.feomedia.orion.Executor;
import se.feomedia.orion.Operation;
import se.feomedia.orion.OperationTree;

public class NullOperation extends Operation {

	@Override
	public Class<? extends Executor> executorType() {
		return NullExecutor.class;
	}

	@Override
	public void reset() {
		completed = true;
	}

	@Wire
	public static class NullExecutor extends Executor<NullOperation> {
		@Override
		protected float act(float delta, NullOperation operation, OperationTree node) {
			// this will never run, but the executor must still be resolved by
			// the OperationSystem.
			throw new RuntimeException("this should not run!");
//			return delta;
		}
	}
}
