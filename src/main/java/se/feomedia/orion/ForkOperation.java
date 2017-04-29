package se.feomedia.orion;

import com.artemis.EntityManager;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.artemis.utils.IntBag;

import static com.artemis.Aspect.all;

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

		/** recreated entity ids must not pick up the last entity's op */
		private IntBag active = new IntBag();

		@Override
		public void initialize(World world) {
			world.getAspectSubscriptionManager()
				.get(all())
				.addSubscriptionListener(new EntitySubscription.SubscriptionListener() {
					@Override
					public void inserted(IntBag entities) {}

					@Override
					public void removed(IntBag entities) {
						int[] ids = entities.getData();
						for (int i = 0, s = entities.size(); s > i; i++) {
							active.set(ids[i], 0);
						}
					}
				});
		}

		@Override
		protected void begin(ForkOperation op, OperationTree node) {
			if (op.tag != null)
				op.forkEntityId = tags.getEntity(op.tag).getId();

			int id = op.forkEntityId;
			if (id > -1 && entityManager.isActive(id)) {
				int count = active.size() > id ? active.get(id) : 0;
				active.set(id, count + 1);
				updateEntity(id, node);
			} else {
				op.completed = true;
			}
		}

		private void updateEntity(int forkEntity, OperationTree node) {
			node.operation.entityId = forkEntity;
			for (OperationTree n : node.children()) {
				updateEntity(forkEntity, n);
			}
		}

		@Override
		protected float act(float delta, ForkOperation op, OperationTree node) {
			return (active.get(op.forkEntityId) > 0)
				? node.children().first().act(delta)
				: 0;
		}

		@Override
		protected void end(ForkOperation op, OperationTree node) {
			active.getData()[op.forkEntityId]--;
		}
	}
}
