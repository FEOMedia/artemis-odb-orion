package se.feomedia.orion;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.utils.Array;
import se.feomedia.orion.operation.NullOperation;

import static java.lang.Math.max;
import static se.feomedia.orion.OperationFactory.operation;

/**
 * Operation type hosting other operations.
 *
 * @see se.feomedia.orion.operation.ParallelOperation
 * @see se.feomedia.orion.operation.SequenceOperation
 * @see RepeatOperation
 *
 */
public abstract class ParentingOperation extends Operation {
	protected Array<Operation> operations = new Array<>(true, 8);


	public void addChild(Operation op) {
		if (op == null)
			op = operation(NullOperation.class);

		operations.add(op);
	}

	@Override
	protected OperationTree toNode() {
		OperationTree tree = super.toNode();
		for (int i = 0; i < operations.size; i++) {
			Operation operation = operations.get(i);
			tree.add(operation.toNode());
		}

		return tree;
	}

	@Override
	public void rewind() {
		super.rewind();
		for (int i = 0, s = operations.size; i < s; i++) {
			operations.get(i).rewind();
		}
	}

	@Override
	public void reset() {
		operations.clear();
		super.reset();
	}

	@Override
	public String toString() {
		return super.toString()
			+ "[" + operations.size  + "]";
	}

	@Wire
	public abstract static class ParentingExecutor<T extends ParentingOperation> extends Executor<T> {
		protected final float end(float delta, ParentingOperation op) {
			op.completed = op.completed || delta > 0;
			return max(0f, delta);
		}
	}
}
