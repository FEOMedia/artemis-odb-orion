package se.feomedia.orion;

import com.artemis.World;
import com.badlogic.gdx.utils.Pool;
import se.feomedia.orion.system.OperationSystem;

public abstract class Operation implements Pool.Poolable {
	protected transient boolean started;
	protected transient int entityId;

	public abstract Class<? extends Executor> executorType();

	protected abstract boolean isComplete();

	protected OperationTree toNode() {
		return new OperationTree(this);
	}

	public final void register(World world, int entityId) {
		world.getSystem(OperationSystem.class).register(entityId, toNode());
	}
}
