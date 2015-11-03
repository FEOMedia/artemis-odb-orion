package se.feomedia.orion.operation;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import org.junit.Test;
import se.feomedia.orion.Executor;
import se.feomedia.orion.OperationTree;
import se.feomedia.orion.system.OperationSystem;

import static org.junit.Assert.assertEquals;
import static se.feomedia.orion.OperationFactory.operation;
import static se.feomedia.orion.OperationFactory.repeat;
import static se.feomedia.orion.OperationTestUtil.process;

public class RepeatOperationTest  {
	@Test
	public void repeat_thrice_over_three_frames() {
		World world = new World(
			new WorldConfiguration()
				.setSystem(OperationSystem.class));

		repeat(3, operation(SingleFrameOperation.class))
			.register(world, world.create());

		assertEquals(0, SingleFrameOperation.invocations);

		process(world);
		assertEquals(1, SingleFrameOperation.invocations);

		process(world);
		assertEquals(2, SingleFrameOperation.invocations);

		process(world);
		assertEquals(3, SingleFrameOperation.invocations);

		process(world);
		assertEquals(3, SingleFrameOperation.invocations);
	}

	@Test
	public void repeat_thrice_over_one_frame() {
		World world = new World(
			new WorldConfiguration()
				.setSystem(OperationSystem.class));

		repeat(3, operation(InstantOperation.class))
			.register(world, world.create());

		assertEquals(0, InstantOperation.invocations);

		process(world);
		assertEquals(3, InstantOperation.invocations);

		process(world);
		assertEquals(3, InstantOperation.invocations);
	}

	public static class SingleFrameOperation extends SingleUseOperation {
		public static int invocations;

		@Override
		public Class<? extends Executor> executorType() {
			return SingleFrameExecutor.class;
		}

		public static class SingleFrameExecutor extends Executor<SingleFrameOperation> {
			@Override
			protected float act(float delta, SingleFrameOperation operation, OperationTree node) {
				invocations++;
				return 0;
			}
		}
	}

	public static class InstantOperation extends SingleUseOperation {
		public static int invocations;

		@Override
		public Class<? extends Executor> executorType() {
			return InstantExecutor.class;
		}

		public static class InstantExecutor extends Executor<InstantOperation> {
			@Override
			protected float act(float delta, InstantOperation operation, OperationTree node) {
				invocations++;
				return delta;
			}
		}
	}
}