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

	@Override
	public Class<? extends Executor> executorType() {
		return SequenceExecutor.class;
	}

	@Wire
	public static class SequenceExecutor extends ParentingExecutor<SequenceOperation> {
		@Override
		protected float act(float delta, SequenceOperation op, OperationTree node) {
			Array<OperationTree> children = node.children();
			for (int i = 0, s = children.size; s > i; i++) {
				OperationTree ot = children.get(i);
				delta = ot.act(delta);
				if (delta <= 0)
					break;
			}

			return end(delta, op);
		}
	}
}
