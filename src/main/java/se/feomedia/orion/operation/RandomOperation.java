package se.feomedia.orion.operation;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.FloatArray;
import se.feomedia.orion.Executor;
import se.feomedia.orion.Operation;
import se.feomedia.orion.OperationTree;
import se.feomedia.orion.ParentingOperation;

public class RandomOperation extends ParentingOperation {
	FloatArray cumulativeSums = new FloatArray(8);
	float accumulated;
	int operationIndex;

	@Override
	public Class<? extends Executor> executorType() {
		return RandomExecutor.class;
	}

	public void configure(float weight, Operation op) {
		validate(weight);

		addChild(op);
		accumulated += weight;
		cumulativeSums.add(accumulated);
	}

	private void validate(float weight) {
		if (started)
			throw new IllegalStateException("operation already initialized");

		if (weight < 0)
			throw new IllegalArgumentException("weight < 0, was " + weight);
	}

	@Override
	public void reset() {
		super.reset();
		cumulativeSums.clear();
		accumulated = 0;
		operationIndex = 0;
	}

	public static class RandomExecutor extends ParentingExecutor<RandomOperation> {
		@Override
		protected void begin(RandomOperation op, OperationTree node) {
			int index = 0;
			float rnd = MathUtils.random(op.accumulated);
			for (int i = 0, s = op.cumulativeSums.size; s > i; i++) {
				if (rnd < op.cumulativeSums.items[i])
					break;

				index++;
			}

			op.operationIndex = index;
		}

		@Override
		protected float act(float delta, RandomOperation op, OperationTree node) {
			return node.children().get(op.operationIndex).act(delta);
		}
	}
}
