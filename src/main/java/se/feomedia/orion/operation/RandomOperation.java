package se.feomedia.orion.operation;

import com.badlogic.gdx.utils.FloatArray;
import se.feomedia.orion.Executor;
import se.feomedia.orion.Operation;
import se.feomedia.orion.OperationTree;
import se.feomedia.orion.ParentingOperation;

public class RandomOperation extends ParentingOperation {
	private FloatArray cumulativeSums = new FloatArray();
	private float accumulated;
	private boolean initialized;
	private int operationIndex;

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
		if (initialized)
			throw new IllegalStateException("operation already initialized");

		if (weight < 0)
			throw new IllegalArgumentException("weight < 0");
	}

	@Override
	public void reset() {
		super.reset();
		cumulativeSums.clear();
		initialized = false;
		accumulated = 0;
		operationIndex = 0;
	}

	public static class RandomExecutor extends ParentingExecutor<RandomOperation> {
		@Override
		protected void begin(RandomOperation operation, OperationTree node) {
			super.begin(operation, node);
		}

		@Override
		protected float act(float delta, RandomOperation op, OperationTree node) {
			return node.children().get(op.operationIndex).act(delta);
		}
	}
}
