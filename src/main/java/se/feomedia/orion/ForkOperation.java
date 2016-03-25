package se.feomedia.orion;

import com.artemis.EntityManager;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;

public class ForkOperation extends ParentingOperation {
	int forkEntityId = -1;
	String tag;

	@Override
	public Class<? extends Executor> executorType() {
		return ForkExecutor.class;
	}

	public void configure(int entityId, String tag, Operation operation) {
		this.tag = tag;
		this.forkEntityId = entityId;
		addChild(operation);
	}

	@Override
	public void reset() {
		super.reset();
		tag = null;
		forkEntityId = -1;
	}

	@Wire(failOnNull = false)
	public static class ForkExecutor extends ParentingExecutor<ForkOperation> {
		private TagManager tags;
		private EntityManager entityManager;

		@Override
		protected void begin(ForkOperation op, OperationTree node) {
			if (op.tag != null)
				op.forkEntityId = tags.getEntity(op.tag).getId();

			updateEntity(op.forkEntityId, node);
		}

		private void updateEntity(int forkEntity, OperationTree node) {
			node.operation.entityId = forkEntity;
			for (OperationTree n : node.children()) {
				updateEntity(forkEntity, n);
			}
		}

		@Override
		protected float act(float delta, ForkOperation op, OperationTree node) {
			if (!entityManager.isActive(op.forkEntityId))
				return 0;

			return node.children().first().act(delta);
		}
	}
}