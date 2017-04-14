package se.feomedia.orion;

import com.artemis.World;
import com.artemis.annotations.Wire;

/**
 * <p>Executors act on operations. They are similar to entity systems -
 * logic building blocks for acting on operation data - and typically
 * reference the same ECS assets; component mappers and other systems.</p>
 * <p/>
 * <p>Executor lifecycle is managed by the {@link World}. There will
 * never be more than one Executor per type and world. If <code>@Wire</code>
 * is present, the executor is subject to normal dependency injection.</p>
 *
 * @param <T> Operation type.
 */
@Wire
public abstract class Executor<T extends Operation> {
	public void initialize(World world) {}

	/**
	 * Updates the operation.
	 *
	 * @param delta     since last frame
	 * @param operation the operation
	 * @param node      node hosting this operation
	 * @return new delta, may be same if operation was instant.
	 */
	protected abstract float act(float delta, T operation, OperationTree node);

	/**
	 * Called before the first time {@link #act(float, Operation, OperationTree)}
	 * is called.
	 *
	 * @param operation Current operation.
	 * @param node      Current node, hosting the operation.
	 */
	protected void begin(T operation, OperationTree node) {}

	protected final float process(float delta, Operation operation, OperationTree node) {
		if (operation.isComplete())
			return delta;

		if (!operation.started) {
			begin((T) operation, node);
			operation.started = true;
		}

		float dt = 0f;

		if (!operation.isComplete()) {
			dt = act(delta, (T) operation, node);
		}

		if (operation.isComplete()) {
			end((T) operation, node);
		}

		return dt;
	}

	/**
	 * Called upon {@link Operation#isComplete()} returning true.
	 *
	 * @param operation Current operation.
	 * @param node      Current node, hosting the operation.
	 */
	protected void end(T operation, OperationTree node) {}
}
