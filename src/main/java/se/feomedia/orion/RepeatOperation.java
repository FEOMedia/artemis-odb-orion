package se.feomedia.orion;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.utils.Array;
import se.feomedia.orion.system.OperationSystem;

/**
 * Repeats an operation N number of times.
 */
public class RepeatOperation extends ParentingOperation {
	int total;
	int acc;

	Operation repeated;

	@Override
	public Class<? extends Executor> executorType() {
		return RepeatExecutor.class;
	}

	@Override
	public void rewind() {
		super.rewind();
		acc = 0;

		if (repeated != null) {
			repeated.rewind();
		}
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
				op.repeated = childNode.operation;
				op.repeated.rewind();
			}
		}

		@Override
		protected float act(float delta, RepeatOperation op, OperationTree node) {
			OperationTree child = node.children().first();

			while (op.acc < op.total && delta > 0) {
				delta = child.act(delta);

				if (child.isComplete()) {
					op.acc++;

					if (op.acc >= op.total) {
						op.completed = true;
						op.repeated.rewind();
					}
					else {
						child.operation.rewind();
					}
				}
			}

			return delta;
		}
	}
}
