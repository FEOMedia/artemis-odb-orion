package se.feomedia.orion.system;

import com.artemis.*;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import se.feomedia.orion.Executor;
import se.feomedia.orion.Operation;
import se.feomedia.orion.OperationTree;
import se.feomedia.orion.component.Operative;

import static com.artemis.Aspect.all;

public class OperationSystem extends IteratingSystem {
	private ComponentMapper<Operative> operativeMapper;

	private ObjectMap<Class<? extends Executor>, Executor> executors = new ObjectMap<>();

	private Operative voidEntityOperations = new Operative();

	public OperationSystem() {
		super(all(Operative.class));
	}

	/**
	 * Constructor with specialized aspect. Can be used to add <code>exclude</code>
	 * components for entities otherwise marked as disabled.
	 *
	 * @param base Useful for excluding disabled entities in projects.
	 */
	public OperationSystem(Aspect.Builder base) {
		super(base.all(Operative.class));
	}

	@Override
	protected void initialize() { }

	public Array<OperationTree> getVoidEntityOperations() {
		return voidEntityOperations.operations;
	}

	public void register(int entityId, OperationTree operation) {
		Operative operative = operativeMapper.create(entityId);
		if (operative != null) { // null == pending deletion
			operative.operations.add(operation);
			operation.initialize(this, entityId);
		}
	}

	public void register(OperationTree operation) {
		operation.initialize(this, -1);
		voidEntityOperations.operations.add(operation);
	}

	@Override
	protected void inserted(int entityId) {
		Array<OperationTree> operations = operativeMapper.get(entityId).operations;
		for (int i = 0, s = operations.size; s > i; i++) {
			operations.get(i).initialize(this, entityId);
		}
	}

	public Executor getExecutor(Operation operation, OperationTree.Friend friend) {
		friend.hashCode();

		Executor executor = executors.get(operation.executorType());
		if (executor == null) {
			executor = createExecutor(operation);
			executors.put(operation.executorType(), executor);
		}

		return executor;
	}

	private Executor createExecutor(Operation operation) {
		try {
			Executor executor = operation.executorType().newInstance();

			world.inject(executor);
			executor.initialize(world);

			return executor;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void process(int e) {
		Array<OperationTree> operations = operativeMapper.get(e).operations;
		process(operations);

		if (operations.size == 0) //hehe, 1.2.0 bug... world.edit(e).remove(Operative.class);
			operativeMapper.remove(e);
	}

	private void process(Array<OperationTree> operations) {
		for (int i = 0; operations.size > i; i++) {
			OperationTree ot = operations.get(i);
			ot.act(world.delta);
			if (ot.isComplete()) {
				OperationTree node = operations.removeIndex(i--);
				node.clear();
			}
		}
	}

	@Override
	protected void end() {
		process(voidEntityOperations.operations);
	}

	@Override
	protected void removed(int entityId) {
		clear(entityId);
	}

	public void clear() {
		clear(voidEntityOperations.operations);
	}

	public void clear(int entityId) {
		if (!operativeMapper.has(entityId))
			return;

		Array<OperationTree> operations = operativeMapper.get(entityId).operations;
		clear(operations);

		if (world.getEntityManager().isActive(entityId))
			operativeMapper.remove(entityId);
	}

	private static void clear(Array<OperationTree> operations) {
		for (int i = 0; i < operations.size; i++) {
			operations.get(i).clear();
		}

		operations.clear();
	}
}
