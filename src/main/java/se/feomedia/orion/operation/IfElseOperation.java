package se.feomedia.orion.operation;

import se.feomedia.orion.*;

public class IfElseOperation extends ParentingOperation {
	boolean isTrue;

	@Override
	public Class<? extends Executor> executorType() {
		return IfElseExecutor.class;
	}

	public void configure(boolean b, Operation ifTrue) {
		isTrue = b;
		addChild(ifTrue);
	}

	public Operation elseDo(Operation ifFalse) {
		if (operations.size > 1) {
			throw new OperationException("Can't add more than one else operation");
		}

		addChild(ifFalse);
		return this;
	}

	@Override
	protected OperationTree toNode() {
		if (operations.size == 1)
			addChild(null);

		return super.toNode();
	}

	public static class IfElseExecutor extends ParentingExecutor<IfElseOperation> {
		@Override
		protected float act(float delta, IfElseOperation op, OperationTree node) {
			OperationTree child = node.children().get(op.isTrue ? 0 : 1);
			return child != null ? child.act(delta) : delta;
		}
	}
}
