package se.feomedia.orion.operation;

import com.badlogic.gdx.math.Interpolation;
import se.feomedia.orion.Executor;
import se.feomedia.orion.Operation;
import se.feomedia.orion.OperationTree;

import static java.lang.Math.max;
import static java.lang.Math.min;

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
	public boolean isComplete() {
		return percent() == 1.0f;
	}

	@Override
	public void reset() {
		acc = 0;
		duration = 0;
	}

	protected final float percent() {
		return (duration > 0)
			? min(1.0f, acc / duration)
			: 1.0f;
	}

	public abstract static class TemporalExecutor<T extends TemporalOperation> extends Executor<T> {

		@Override
		protected final float act(float delta, T operation, OperationTree node) {
			operation.acc += delta;
			return max(0, act(delta, operation.percent(), operation, node));
		}

		protected abstract float act(float delta, float alpha, T operation, OperationTree node);
	}
}
