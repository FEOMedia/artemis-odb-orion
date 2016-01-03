package se.feomedia.orion;

import com.artemis.annotations.Wire;
import se.feomedia.orion.system.OperationSystem;

/**
 * Repeats an operation N number of times.
 */
public class RepeatOperation extends ParentingOperation {
	int total;
	int acc;

	transient Operation repeated;

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
				OperationTree childNode = node.children().get(0);
				op.repeated = operations.copy(childNode.operation);
			}
		}

		@Override
		protected float act(float delta, RepeatOperation op, OperationTree node) {
			OperationTree child = node.children().first();

			while (op.acc < op.total && delta > 0) {
				delta = child.act(delta);
				if (child.isComplete()) {
					op.acc++;
					child = replaceChild(node);
				}
			}

			return delta;
		}

		private OperationTree replaceChild(OperationTree node) {
			RepeatOperation op = (RepeatOperation) node.operation;

			OperationTree oldChild = node.children().pop();

			Operation copy = operations.copy(op.repeated);
			node.add(copy.toNode());
			node.initialize(operations, op.entityId);

			// reclaiming objects
			oldChild.clear();
			return node.children().first();
		}
	}
}
