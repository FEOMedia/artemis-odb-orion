package se.feomedia.orion.operation;

import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import se.feomedia.orion.Executor;
import se.feomedia.orion.OperationTree;
import se.feomedia.orion.component.RecordOperationId;

public class RecordIdOperation extends SingleUseOperation {

	@Override
	public Class<? extends Executor> executorType() {
		return RecordIdExecutor.class;
	}

	@Wire
	public static class RecordIdExecutor extends SingleUseExecutor<RecordIdOperation> {
		private ComponentMapper<RecordOperationId> idMapper;

		@Override
		protected void act(RecordIdOperation op, OperationTree node) {
			idMapper.get(op.entityId).idFromOperation = op.entityId;
		}
	}
}