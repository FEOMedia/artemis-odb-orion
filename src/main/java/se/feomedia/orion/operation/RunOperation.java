package se.feomedia.orion.operation;

import com.artemis.annotations.Wire;
import se.feomedia.orion.Executor;
import se.feomedia.orion.OperationTree;

public class RunOperation extends SingleUseOperation {
	public Runnable runnable;

	@Override
	public Class<? extends Executor> executorType() {
		return RunExecutor.class;
	}

	@Override
	public void reset() {
		super.reset();
		runnable = null;
	}

	@Wire
	public static class RunExecutor extends SingleUseExecutor<RunOperation> {
		@Override
		protected void act(RunOperation op, OperationTree node) {
			op.runnable.run();
		}
	}
}