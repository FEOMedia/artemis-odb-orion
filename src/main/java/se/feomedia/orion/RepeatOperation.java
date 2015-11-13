package se.feomedia.orion;

import com.artemis.annotations.Wire;
import se.feomedia.orion.system.OperationSystem;

public class RepeatOperation extends ParentingOperation {
	int total;
	int acc;

	Operation repeated;

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
		repeated = null;
	}

	public void configure(int total, Operation operation) {
		this.total = total;
		addChild(operation);
	}

	@Wire
	public static class RepeatExecutor extends ParentingExecutor<RepeatOperation> {
		private OperationSystem operations;

		@Override
		protected void begin(RepeatOperation op, OperationTree node) {
			super.begin(op, node);
			if (op.repeated == null) {
				System.out.println("## in begin");
				System.out.println(node);
				System.out.println();

				op.repeated = operations.copy(node.children().get(0).operation);
			}
		}

		@Override
		protected float act(float delta, RepeatOperation op, OperationTree node) {
			OperationTree child = node.children().first();

			while (op.acc < op.total && delta > 0) {
				delta = child.act(delta);
				if (child.isComplete()) {
					op.acc++;
					node.children().removeIndex(0);

					Operation copy = operations.copy(op.repeated);

					node.add(copy.toNode());
					node.initialize(operations, op.entityId);

					System.out.println("## copy");
					System.out.println(node.children().first());
					System.out.println();

					child.clear();
					child = node.children().first();
				}
			}

			return delta;
		}
	}
}
