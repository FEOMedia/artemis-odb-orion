package se.feomedia.orion;

import com.artemis.ComponentMapper;
import com.artemis.EntityManager;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.artemis.utils.IntBag;
import se.feomedia.orion.component.Forked;

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
		private ComponentMapper<Forked> forkedMapper;

		private TagManager tags;
		private EntityManager entityManager;

		@Override
		public void initialize(World world) {
			// while subscribing to Forked.class would have been the intuitively
			// more correct approach, `removed` would not fire if the entity
			// was deleted during the same system tick as it was forked to.
			world.getAspectSubscriptionManager()
				.get(all())
				.addSubscriptionListener(new EntitySubscription.SubscriptionListener() {
					@Override
					public void inserted(IntBag entities) {}

					@Override
					public void removed(IntBag entities) {
						int[] ids = entities.getData();
						for (int i = 0, s = entities.size(); s > i; i++) {
							Forked forked = forkedMapper.get(ids[i]);
							if (forked == null)
								continue;

							for (ForkOperation fo : forked.owners)
								fo.completed = true;
						}
					}
				});
		}

		@Override
		protected void begin(ForkOperation op, OperationTree node) {
			if (op.tag != null)
				op.forkEntityId = tags.getEntity(op.tag).getId();

			int forked = op.forkEntityId;
			if (forked != -1 && entityManager.isActive(forked)) {
				updateEntity(forked, node);
				forkedMapper.create(forked).owners.add(op);
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
			if (op.forkEntityId != -1 && entityManager.isActive(op.forkEntityId)) {
				OperationTree forkedOp = node.children().first();
				delta = forkedOp.act(delta);
				op.completed |= forkedOp.isComplete();
			} else {
				op.completed = true;
			}

			return delta;
		}

		@Override
		protected void end(ForkOperation op, OperationTree node) {
			int id = op.forkEntityId;
			if (id != -1 && entityManager.isActive(id)) {
				Forked forked = forkedMapper.get(id);
				if (forked != null) {
					forked.owners.removeValue(op, true);
					if (forked.owners.size == 0)
						forkedMapper.remove(id);
				}
			}
		}
	}
}
