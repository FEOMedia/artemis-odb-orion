package se.feomedia.orion.operation;

import com.badlogic.gdx.math.Interpolation;
import se.feomedia.orion.Executor;
import se.feomedia.orion.Operation;
import se.feomedia.orion.OperationTree;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static se.feomedia.orion.OperationFactory.seconds;

/**
 * Base type of operations with a notion of duration.
 */
public abstract class TemporalOperation extends Operation {
	public float acc;
	public float duration;
	public Interpolation interpolation = Interpolation.linear;

	public void duration(float duration) {
		this.duration = duration;
	}

	public float duration() {
		return this.duration;
	}

	public float remaining() {
		return max(0, duration - acc);
	}

	@Override
	public void rewind() {
		acc = 0;
		super.rewind();
	}

	@Override
	public void reset() {
		acc = 0;
		duration = 0;
		super.reset();
	}

	protected final float percent() {
		return (duration > 0)
			? min(1.0f, acc / duration)
			: 1.0f;
	}

	protected final float alpha() {
		return interpolation.apply(percent());
	}

	public abstract static class TemporalExecutor<T extends TemporalOperation> extends Executor<T> {

		@Override
		protected void begin(T op, OperationTree node) {
			super.begin(op, node);

			// ensure we're running at least once per frame
			op.duration = seconds(op.duration);
		}

		@Override
		protected final float act(float delta, T op, OperationTree node) {
			op.acc += delta;
			act(delta, op.percent(), op, node);

			if (op.acc > 0 && op.percent() == 1.0f)
				op.completed = true;

			// we're returning available time, hence acc - duration
			return max(0, op.acc - op.duration);
		}

		protected abstract void act(float delta, float alpha, T operation, OperationTree node);
	}
}
