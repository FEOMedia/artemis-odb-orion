package se.feomedia.orion.operation;

import com.artemis.ComponentMapper;
import com.badlogic.gdx.utils.Array;
import se.feomedia.orion.Executor;
import se.feomedia.orion.Operation;
import se.feomedia.orion.OperationTree;
import se.feomedia.orion.ParentingOperation;
import se.feomedia.orion.component.Operative;
import se.feomedia.orion.system.OperationSystem;

public class UniqueOperation extends ParentingOperation {
	public String tag;

	@Override
	public Class<? extends Executor> executorType() {
		return UniqueExecutor.class;
	}

	@Override
	public void reset() {
		super.reset();
		tag = null;
	}

	public void configure(String tag, Operation operation) {
		this.tag = tag;
		addChild(operation);
	}

	public static class UniqueExecutor extends ParentingExecutor<UniqueOperation> {
		private Array<UniqueOperation> out = new Array<>();
		private ComponentMapper<Operative> operativeMapper;
		private OperationSystem operationSystem;

		@Override
		protected void begin(UniqueOperation op, OperationTree node) {
			super.begin(op, node);

			for (OperationTree root : allOperations(op))
				root.findAll(UniqueOperation.class, out);

			for (UniqueOperation operation : out) {
				if (operation != op && op.tag.equals(operation.tag))
					operation.completed = true;
			}

			out.clear();
		}

		private Array<OperationTree> allOperations(UniqueOperation op) {
			return (op.entityId != -1) // if -1, free-floating operation
				? operativeMapper.create(op.entityId).operations
				: operationSystem.getVoidEntityOperations();
		}

		@Override
		protected float act(float delta, UniqueOperation op, OperationTree node) {
			if (op.completed) // avoid processing the tick it was
				return delta; // invalidated by another unique

			OperationTree wrapped = node.children().get(0);
			delta = wrapped.act(delta);
			op.completed = wrapped.isComplete();

			return delta;
		}
	}
}
