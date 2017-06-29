package se.feomedia.orion.operation;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.utils.Array;
import se.feomedia.orion.Executor;
import se.feomedia.orion.OperationTree;
import se.feomedia.orion.ParentingOperation;


/**
 * Processes all operations sequentially, until the last operation
 * reports completed.
 */
public class SequenceOperation extends ParentingOperation {
	int completedOps = 0;

	@Override
	public Class<? extends Executor> executorType() {
		return SequenceExecutor.class;
	}

	@Override
	public void rewind() {
		completedOps = 0;
		super.rewind();
	}

	@Override
	public void reset() {
		completedOps = 0;
		super.reset();
	}

	@Wire
	public static class SequenceExecutor extends ParentingExecutor<SequenceOperation> {
		@Override
		protected float act(float delta, SequenceOperation op, OperationTree node) {
			Array<OperationTree> children = node.children();

			if (op.completedOps >= children.size) {
				op.completed = true;
			}

			for (int i = 0; children.size > i; i++) {
				OperationTree ot = children.get(i);
				boolean wasCompleted = ot.isComplete();
				if (wasCompleted) {
					continue;
				}

				delta = ot.act(delta);

				if (!wasCompleted && ot.isComplete()) {
					op.completedOps++;
				}
				if (delta <= 0)
					break;
			}

			return end(delta, op);
		}
	}
}
