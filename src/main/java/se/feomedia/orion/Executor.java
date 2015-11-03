package se.feomedia.orion;

import com.artemis.World;
import com.artemis.annotations.Wire;

@Wire
public abstract class Executor<T extends Operation> {
	public Operation parent;

	protected void initialize(World world) {
		world.inject(this);
	}

	/**
	 * Runs every tick.
	 */
	protected abstract float act(float delta, T operation, OperationTree node);

	protected void begin() {}

	public float process(float delta, Operation operation, OperationTree node) {
		if (operation.isComplete())
			return delta;

		if (!operation.started) {
			begin();
			operation.started = true;
		}

		delta = act(delta, (T) operation, node);

		return delta;
	}
}
