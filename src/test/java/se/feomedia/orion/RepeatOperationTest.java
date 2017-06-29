package se.feomedia.orion;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.utils.IntArray;
import org.junit.Test;
import se.feomedia.orion.operation.SingleUseOperation;
import se.feomedia.orion.system.OperationSystem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static se.feomedia.orion.OperationFactory.*;
import static se.feomedia.orion.OperationTestUtil.process;

public class RepeatOperationTest  {
	@Test
	public void repeat_thrice_over_three_frames() {
		int[] counter = new int[1];

		World world = new World(new WorldConfiguration()
			.register("counter", counter)
			.setSystem(OperationSystem.class));

		repeat(3,
			sequence(
				operation(SingleFrameOperation.class)
			)
		).register(world);

		assertEquals(0, counter[0]);

		process(world);
		assertEquals(1, counter[0]);

		process(world);
		assertEquals(2, counter[0]);

		process(world);
		assertEquals(3, counter[0]);

		process(world);
		assertEquals(3, counter[0]);
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

	@Test
	public void repeated_operations_are_recycled() {
		IntArray instances = new IntArray();

		World world = new World(
			new WorldConfiguration()
				.register("instances", instances)
				.setSystem(OperationSystem.class));

		repeat(100,
			operation(SelfTrackingOperation.class)
		).register(world, world.create());

		process(world);
		assertEquals(1, instances.size);

		process(world); // just making sure nothing new happens
		assertEquals(1, instances.size);

		assertTrue(instances.contains(
			System.identityHashCode(operation(SelfTrackingOperation.class))));
	}

	public static class SingleFrameOperation extends Operation {
		public int counter = 1;

		@Override
		public Class<? extends Executor> executorType() {
			return SingleFrameExecutor.class;
		}

		@Override
		public void rewind() {
			super.rewind();
			counter = 1;
		}

		@Override
		public void reset() {
			super.reset();
			counter = 1;
		}

		@Wire
		public static class SingleFrameExecutor extends Executor<SingleFrameOperation> {
			@Wire(name="counter")
			int[] counter;

			@Override
			protected float act(float delta, SingleFrameOperation op, OperationTree node) {
				counter[0]++;
				op.counter++;
				assertEquals(2, op.counter);

				if (op.counter == 2)
					op.completed = true;

				return 0;
			}
		}
	}

	public static class SelfTrackingOperation extends SingleUseOperation {

		@Override
		public Class<? extends Executor> executorType() {
			return SelfTrackingExecutor.class;
		}

		@Wire
		public static class SelfTrackingExecutor extends SingleUseExecutor<SelfTrackingOperation> {
			@Wire(name="instances")
			IntArray instances;

			@Override
			protected void act(SelfTrackingOperation op, OperationTree node) {
				int hash = System.identityHashCode(op);
				if (!instances.contains(hash))
					instances.add(hash);
			}
		}
	}

	public static class InstantOperation extends Operation {
		public static int invocations;


		@Override
		public Class<? extends Executor> executorType() {
			return InstantExecutor.class;
		}

		public static class InstantExecutor extends Executor<InstantOperation> {
			@Override
			protected float act(float delta, InstantOperation op, OperationTree node) {
				invocations++;
				op.completed = true;
				return delta;
			}
		}
	}
}
