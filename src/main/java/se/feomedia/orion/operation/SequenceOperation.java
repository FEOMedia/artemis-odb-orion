package se.feomedia.orion.operation;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.utils.Array;
import se.feomedia.orion.Executor;
import se.feomedia.orion.OperationTree;
import se.feomedia.orion.ParentingOperation;


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
			OperationTree[] nodes = children.items;
			for (int i = 0, s = children.size; s > i; i++) {
				delta = nodes[i].act(delta);
				if (delta <= 0)
					break;
			}

//			if (delta > 0)
//				op.completed = true;

			return end(delta, op);
		}
	}
}
