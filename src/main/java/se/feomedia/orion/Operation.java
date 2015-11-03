package se.feomedia.orion;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.utils.Pool;
import se.feomedia.orion.system.OperationSystem;

public abstract class Operation implements Pool.Poolable {
	public transient int entityId = -1;

	protected boolean started;

	public abstract Class<? extends Executor> executorType();
	protected abstract boolean isComplete();

	protected OperationTree toNode() {
		return new OperationTree(this);
	}

	public final void register(World world, int entityId) {
		world.getSystem(OperationSystem.class).register(entityId, toNode());
	}

	public final void register(Entity e) {
		register(e.getWorld(), e.getId());
	}
}
