package se.feomedia.orion;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.annotations.Wire;
import org.junit.Test;
import se.feomedia.orion.operation.SingleUseOperation;
import se.feomedia.orion.system.OperationSystem;

import static org.junit.Assert.assertEquals;
import static se.feomedia.orion.OperationFactory.*;
import static se.feomedia.orion.OperationTestUtil.process;

public class RepeatOperationTest  {
	@Test
	public void repeat_thrice_over_three_frames() {
		System.out.println("# repeat_thrice_over_three_frames\n");
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
		System.out.println("# repeat_thrice_over_one_frame\n");

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
		public int counter = 1;

		@Override
		public Class<? extends Executor> executorType() {
			return SingleFrameExecutor.class;
		}

		@Override
		public void reset() {
			super.reset();
			counter = 1;
		}

		@Override
		protected boolean isComplete() {
			return counter == 2;
		}

		@Wire
		public static class SingleFrameExecutor extends Executor<SingleFrameOperation> {
			@Wire(name="counter")
			int[] counter;

			@Override
			protected float act(float delta, SingleFrameOperation op, OperationTree node) {
				System.out.println("invoking");

				counter[0]++;
				op.counter++;
				assertEquals(2, op.counter);
				return 0;
			}
		}
	}

	public static class InstantOperation extends Operation {
		public static int invocations;
		private boolean complete;

		@Override
		public Class<? extends Executor> executorType() {
			return InstantExecutor.class;
		}

		@Override
		protected boolean isComplete() {
			return complete;
		}

		@Override
		public void reset() {
			complete = false;
		}

		public static class InstantExecutor extends Executor<InstantOperation> {
			@Override
			protected float act(float delta, InstantOperation op, OperationTree node) {
				invocations++;
				op.complete = true;
				return delta;
			}
		}
	}
}