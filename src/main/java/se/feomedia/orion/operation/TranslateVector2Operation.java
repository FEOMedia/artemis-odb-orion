package se.feomedia.orion.operation;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.math.Vector2;
import se.feomedia.orion.Executor;
import se.feomedia.orion.OperationTree;

public class TranslateVector2Operation extends TemporalOperation {
	public Vector2 value;
	public Vector2 begin = new Vector2();
	public Vector2 end = new Vector2();

	@Override
	public Class<? extends Executor> executorType() {
		return TranslateVector2Executor.class;
	}

	@Override
	public void reset() {
		super.reset();
		value = null;
	}

	@Wire
	public static class TranslateVector2Executor extends TemporalExecutor<TranslateVector2Operation> {
		@Override
		protected void begin(TranslateVector2Operation op, OperationTree node) {
			super.begin(op, node);
			op.begin.set(op.value);
		}

		@Override
		protected void act(float delta, float percent, TranslateVector2Operation op, OperationTree node) {
			op.value.set(op.begin).interpolate(op.end, percent, op.interpolation);
		}
	}
}