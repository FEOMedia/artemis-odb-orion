package se.feomedia.orion.operation;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.annotations.Wire;
import org.junit.Test;
import se.feomedia.orion.Executor;
import se.feomedia.orion.Operation;
import se.feomedia.orion.OperationTree;
import se.feomedia.orion.system.OperationSystem;

import static org.junit.Assert.*;
import static se.feomedia.orion.OperationFactory.*;

public class ExecutorEndTest {
	@Test
	public void end_test() {
		World world = new World(new WorldConfiguration()
			.setSystem(OperationSystem.class));

		FiniteOperation fo = operation(FiniteOperation.class);

		fo.register(world, world.create());

		world.process();

		assertEquals(1, fo.n);

		world.process();

		assertEquals(2, fo.n);

		world.process();

		assertEquals(-1, fo.n);
	}


	public static class FiniteOperation extends Operation {
		public int n = 0;

		@Override
		public Class<? extends Executor> executorType() {
			return FiniteExecutor.class;
		}

		@Wire
		public static class FiniteExecutor extends Executor<FiniteOperation> {
			@Override
			protected float act(float delta, FiniteOperation operation, OperationTree node) {
				operation.n++;

				if (operation.n > 2)
					operation.completed = true;

				return 0;
			}

			@Override
			protected void begin(FiniteOperation operation, OperationTree node) {
				operation.n = 0;
			}

			@Override
			protected void end(FiniteOperation operation, OperationTree node) {
				operation.n = -1;
			}
		}
	}
}