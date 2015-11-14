package se.feomedia.orion.operation;

import com.artemis.World;
import com.artemis.annotations.Wire;
import se.feomedia.orion.Executor;
import se.feomedia.orion.OperationTree;

/**
 * Kills the owning entity.
 */
public class KillOperation extends SingleUseOperation {

	@Override
	public Class<? extends Executor> executorType() {
		return KillExecutor.class;
	}

	@Wire
	public static class KillExecutor extends SingleUseExecutor<KillOperation> {
		private World world;

		@Override
		public void initialize(World world) {
			super.initialize(world);
			this.world = world;
		}

		@Override
		protected void act(KillOperation op, OperationTree node) {
			world.delete(op.entityId);
		}
	}
}
