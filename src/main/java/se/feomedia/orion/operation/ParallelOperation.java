package se.feomedia.orion.operation;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.utils.Array;
import se.feomedia.orion.Executor;
import se.feomedia.orion.OperationFactory;
import se.feomedia.orion.OperationTree;
import se.feomedia.orion.ParentingOperation;

import static java.lang.Math.min;

/**
 * Processes operations simultaneously. This operation will not
 * report completed until all operations have run their course completed.
 */
public class ParallelOperation extends ParentingOperation {

	public ParallelOperation(OperationFactory.Friend friend) {
		super(friend);
	}

	@Override
	public Class<? extends Executor> executorType() {
		return ParallelExecutor.class;
	}

	@Wire
	public static class ParallelExecutor extends ParentingExecutor<ParallelOperation> {
		@Override
		protected float act(float delta, ParallelOperation op, OperationTree node) {

			float remaining = delta;
			Array<OperationTree> children = node.children();
			OperationTree[] nodes = children.items;
			for (int i = 0, s = children.size; s > i; i++) {
				remaining = min(remaining, nodes[i].act(delta));
			}

			return end(remaining, op);
		}
	}
}
