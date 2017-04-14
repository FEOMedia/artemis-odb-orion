package se.feomedia.orion;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.utils.Pool;
import se.feomedia.orion.system.OperationSystem;

/**
 * <p>Operations are analogue to components. They should only carry
 * pure data, as transient state and logic is handled by
 * {@link Executor|Executors}.</p>
 *
 * @see OperationFactory#operation(Class)
 */
public abstract class Operation implements Pool.Poolable {
	public transient int entityId = -1;
	protected boolean started;
	protected boolean completed;

	public abstract Class<? extends Executor> executorType();

	public void rewind() {
		completed = false;
	}

	public void reset() {
		started = false;
		completed = false;
	}

	public final boolean isComplete() {
		return completed;
	}

	protected OperationTree toNode() {
		return OperationTree.obtain(this);
	}

	public final void register(World world, int entityId) {
		OperationSystem os = world.getSystem(OperationSystem.class);
		if (os == null) {
			String name = OperationSystem.class.getSimpleName();
			String error = "Failed to register operation as " + name
				+ " isn't registered with the World instance.";

			throw new RuntimeException(error);
		}

		os.register(entityId, toNode());
	}

	public final void register(Entity e) {
		register(e.getWorld(), e.getId());
	}

	public final void register(World world) {
		world.getSystem(OperationSystem.class).register(toNode());
	}

	@Override
	public String toString() {
		return  executorType().getSimpleName().replaceAll("Executor$", "");
	}
}
