package se.feomedia.orion.system;

import com.artemis.*;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.esotericsoftware.kryo.Kryo;
import org.objenesis.instantiator.ObjectInstantiator;
import org.objenesis.strategy.BaseInstantiatorStrategy;
import se.feomedia.orion.Executor;
import se.feomedia.orion.Operation;
import se.feomedia.orion.OperationTree;
import se.feomedia.orion.RepeatOperation;
import se.feomedia.orion.component.Operative;
import se.feomedia.orion.kryo.OperationInstantiator;
import se.feomedia.orion.operation.*;

import static com.artemis.Aspect.all;

public class OperationSystem extends IteratingSystem {
	private ComponentMapper<Operative> operativeMapper;

	private ObjectMap<Class<? extends Executor>, Executor> executors = new ObjectMap<>();
	private final Friend friend = new Friend();

	private Operative voidEntityOperations = new Operative();
	public Kryo kryo;

	public OperationSystem() {
		super(all(Operative.class));
	}

	@Override
	protected void initialize() {
		kryo = new Kryo();
		kryo.setInstantiatorStrategy(new OperationInstantiator());
		kryo.register(DelayOperation.class);
		kryo.register(DelayOperation.DelayExecutor.class);
		kryo.register(DelayTickOperation.class);
		kryo.register(DelayTickOperation.DelayTickExecutor.class);
		kryo.register(IfElseOperation.class);
		kryo.register(IfElseOperation.IfElseExecutor.class);
		kryo.register(KillOperation.class);
		kryo.register(KillOperation.KillExecutor.class);
		kryo.register(ParallelOperation.class);
		kryo.register(ParallelOperation.ParallelExecutor.class);
		kryo.register(RepeatOperation.class);
		kryo.register(RepeatOperation.RepeatExecutor.class);
		kryo.register(SequenceOperation.class);
		kryo.register(SequenceOperation.SequenceExecutor.class);
	}

	public <T extends Operation> T copy(T operation) {
		return kryo.copy(operation);
	}

	public void register(int entityId, OperationTree operation) {
		operation.initialize(world, entityId, friend);
		operativeMapper.create(entityId).operations.add(operation);
	}

	public void register(OperationTree operation) {
		operation.initialize(world, -1, friend);
		voidEntityOperations.operations.add(operation);
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
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void process(int e) {
		Array<OperationTree> operations = operativeMapper.get(e).operations;
		process(operations);

		if (operations.size == 0) world.edit(e).remove(Operative.class);
//			operativeMapper.remove(e); // artemis <= 1.1.2 bug, cancels entity deletion
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

	public static final class Friend {
		private Friend() {}
	}
}
