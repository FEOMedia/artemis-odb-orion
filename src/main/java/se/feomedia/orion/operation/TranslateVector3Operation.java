package se.feomedia.orion.operation;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.math.Vector3;
import se.feomedia.orion.Executor;
import se.feomedia.orion.OperationTree;

public class TranslateVector3Operation extends TemporalOperation {
	public Vector3 value;
	public Vector3 begin = new Vector3();
	public Vector3 end = new Vector3();

	@Override
	public Class<? extends Executor> executorType() {
		return TranslateVector3Executor.class;
	}

	@Override
	public void reset() {
		super.reset();
		value = null;
	}

	@Wire
	public static class TranslateVector3Executor extends TemporalExecutor<TranslateVector3Operation> {
		@Override
		protected void begin(TranslateVector3Operation op, OperationTree node) {
			super.begin(op, node);
			op.begin.set(op.value);
		}

		@Override
		protected void act(float delta, float percent, TranslateVector3Operation op, OperationTree node) {
			op.value.set(op.begin).interpolate(op.end, percent, op.interpolation);
		}
	}
}