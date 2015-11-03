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

	public OperationSystem() {
		super(all(Operative.class));
	}

	public void register(int entityId, OperationTree operation) {
		operation.initialize(world);
		operativeMapper.create(entityId).operations.add(operation);
	}

	public Executor getExecutor(Operation operation) {
		Executor executor = executors.get(operation.executorType());
		if (executor == null) {
			executor = createExecutor(operation);
			executors.put(operation.executorType(), executor);

			world.inject(executor);
		}

		return executor;
	}

	private Executor createExecutor(Operation operation) {
		try {
			Executor executor = operation.executorType().newInstance();
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
		for (int i = 0; operations.size > i; i++) {
			OperationTree ot = operations.get(i);
			ot.act(world.delta);
			if (ot.isComplete()) {
				OperationTree node = operations.removeIndex(i--);
				node.reset();
			}
		}

		if (operations.size == 0)
			operativeMapper.remove(e);
	}
}
