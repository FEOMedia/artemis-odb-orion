package se.feomedia.orion.operation;

import se.feomedia.orion.Executor;
import se.feomedia.orion.Operation;
import se.feomedia.orion.OperationTree;
import se.feomedia.orion.ParentingOperation;

public class IfElseOperation extends ParentingOperation {
	boolean isTrue;

	@Override
	public Class<? extends Executor> executorType() {
		return IfElseExecutor.class;
	}

	@Override
	protected boolean isComplete() {
		return super.isComplete();
	}

	public void configure(boolean b, Operation ifTrue, Operation ifFalse) {
		isTrue = b;
		addChild(ifFalse);
		addChild(ifTrue);
	}

	public static class IfElseExecutor extends ParentingExecutor<IfElseOperation> {
		@Override
		protected float act(float delta, IfElseOperation op, OperationTree node) {
			OperationTree child = node.children().get(op.isTrue ? 1 : 0);
			return child != null ? child.act(delta) : delta;
		}
	}
}
