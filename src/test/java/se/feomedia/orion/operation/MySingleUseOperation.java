package se.feomedia.orion.operation;

import junit.framework.TestCase;
import se.feomedia.orion.Executor;
import se.feomedia.orion.OperationFactory;
import se.feomedia.orion.OperationTree;

public class MySingleUseOperation extends SingleUseOperation {
	int timesRun;

	public MySingleUseOperation(OperationFactory.Friend friend) {
		super(friend);
	}

	@Override
	public void reset() {
		super.reset();
		timesRun = 0;
	}

	@Override
	public Class<? extends Executor> executorType() {
		return MySingleUseExecutor.class;
	}

	public static class MySingleUseExecutor extends SingleUseExecutor<MySingleUseOperation> {
		@Override
		protected void act(MySingleUseOperation op, OperationTree node) {
			op.timesRun++;
			TestCase.assertEquals(1, op.timesRun);
		}
	}
}
