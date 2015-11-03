package se.feomedia.orion.operation;

import com.artemis.annotations.Wire;
import se.feomedia.orion.Executor;
import se.feomedia.orion.OperationTree;

import static java.lang.Math.max;

public class DelayOperation extends TemporalOperation {

	@Override
	public Class<? extends Executor> executorType() {
		return DelayExecutor.class;
	}

	@Wire
	public static class DelayExecutor extends TemporalOperation.TemporalExecutor<DelayOperation> {
		@Override
		protected float act(float delta, float percent, DelayOperation op, OperationTree node) {
			return max(0, op.acc - op.duration);
		}
	}
}
