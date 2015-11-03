package se.feomedia.orion.operation;

import se.feomedia.orion.Executor;
import se.feomedia.orion.Operation;
import se.feomedia.orion.OperationTree;
import se.feomedia.orion.ParentingOperation;

public class RepeatOperation extends ParentingOperation {
	int total;
	int acc;

	@Override
	public Class<? extends Executor> executorType() {
		return RepeatExecutor.class;
	}

	public boolean isComplete() {
		return acc >= total;
	}

	@Override
	public void reset() {
		super.reset();
		acc = 0;
		total = 0;
	}

	public void configure(int total, Operation operation) {
		this.total = total;
		addChild(operation);
	}

	public static class RepeatExecutor extends ParentingExecutor<RepeatOperation> {

		@Override
		protected float act(float delta, RepeatOperation op, OperationTree node) {
			OperationTree child = node.children().get(0);

			while (op.acc < op.total && delta > 0) {
				delta = child.act(delta);
				op.acc++;
			}

			return delta;
		}
	}
}
