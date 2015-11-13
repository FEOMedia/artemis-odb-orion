package se.feomedia.orion.operation;

import com.artemis.annotations.Wire;
import se.feomedia.orion.Executor;
import se.feomedia.orion.OperationFactory;
import se.feomedia.orion.OperationTree;

public class DelayOperation extends TemporalOperation {
	public DelayOperation(OperationFactory.Friend friend) {
		super(friend);
	}

	@Override
	public Class<? extends Executor> executorType() {
		return DelayExecutor.class;
	}

	@Wire
	public static class DelayExecutor extends TemporalOperation.TemporalExecutor<DelayOperation> {
		@Override
		protected float act(float delta, float percent, DelayOperation op, OperationTree node) {
			return op.acc - op.duration;
		}
	}
}
