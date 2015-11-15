package se.feomedia.orion.operation;

import com.artemis.annotations.Wire;
import se.feomedia.orion.Executor;
import se.feomedia.orion.OperationTree;

public class DelayOperation extends TemporalOperation {

	@Override
	public Class<? extends Executor> executorType() {
		return DelayExecutor.class;
	}

	@Wire
	public static class DelayExecutor extends TemporalOperation.TemporalExecutor<DelayOperation> {
		@Override
		protected void act(float delta, float percent, DelayOperation op, OperationTree node) {
		}
	}
}
