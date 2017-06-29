package se.feomedia.orion.operation;

import se.feomedia.orion.Executor;
import se.feomedia.orion.Operation;
import se.feomedia.orion.OperationTree;

public class DelayTickOperation extends Operation {
	public int ticksToWait;
	int acc;

	@Override
	public Class<? extends Executor> executorType() {
		return DelayTickExecutor.class;
	}

	@Override
	public void reset() {
		super.reset();
		acc = ticksToWait = 0;
	}

	public static class DelayTickExecutor extends Executor<DelayTickOperation> {

		@Override
		protected float act(float delta, DelayTickOperation op, OperationTree node) {
			op.acc++;
			if (op.acc >= op.ticksToWait) {
				op.completed = true;
			}
			return 0;
		}
	}
}
