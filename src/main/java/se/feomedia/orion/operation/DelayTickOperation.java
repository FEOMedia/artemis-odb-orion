package se.feomedia.orion.operation;

import se.feomedia.orion.Executor;
import se.feomedia.orion.Operation;
import se.feomedia.orion.OperationFactory;
import se.feomedia.orion.OperationTree;

public class DelayTickOperation extends Operation {
	public int ticksToWait;
	int acc;

	public DelayTickOperation(OperationFactory.Friend friend) {
		super(friend);
	}

	@Override
	public Class<? extends Executor> executorType() {
		return DelayTickExecutor.class;
	}

	@Override
	protected boolean isComplete() {
		return acc == ticksToWait;
	}

	@Override
	public void reset() {
		acc = ticksToWait = 0;
	}

	public static class DelayTickExecutor extends Executor<DelayTickOperation> {

		@Override
		protected float act(float delta, DelayTickOperation op, OperationTree node) {
			op.acc++;
			return 0;
		}
	}
}
