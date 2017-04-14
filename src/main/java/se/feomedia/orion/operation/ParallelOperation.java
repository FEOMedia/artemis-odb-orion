package se.feomedia.orion.operation;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.utils.Array;
import se.feomedia.orion.Executor;
import se.feomedia.orion.OperationTree;
import se.feomedia.orion.ParentingOperation;

import static java.lang.Math.min;

/**
 * Processes operations simultaneously. This operation will not
 * report completed until all operations have run their course completed.
 */
public class ParallelOperation extends ParentingOperation {
	int completedOps = 0;

	@Override
	public Class<? extends Executor> executorType() {
		return ParallelExecutor.class;
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
	public static class ParallelExecutor extends ParentingExecutor<ParallelOperation> {
		@Override
		protected float act(float delta, ParallelOperation op, OperationTree node) {
			Array<OperationTree> children = node.children();

			if (op.completedOps >= children.size) {
				op.completed = true;
			}

			float remaining = delta;
			for (int i = 0; children.size > i; i++) {
				OperationTree ot = children.get(i);
				boolean wasCompleted = ot.isComplete();

				if (wasCompleted) {
					continue;
				}

				remaining = min(remaining, ot.act(delta));
				if (!wasCompleted && ot.isComplete()) {
					op.completedOps++;
				}
			}

			return end(remaining, op);
		}
	}
}
