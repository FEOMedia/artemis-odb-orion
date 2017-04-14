package se.feomedia.orion;

import com.artemis.*;
import org.junit.Test;
import se.feomedia.orion.system.OperationSystem;

import static org.junit.Assert.assertEquals;
import static se.feomedia.orion.OperationFactory.*;
import static se.feomedia.orion.OperationTestUtil.process;

public class RepeatOperation2Test {

	@Test
	public void repeat_nested_repeats() {
		World world = new World(
			new WorldConfiguration()
				.setSystem(OperationSystem.class));

		Entity entity = world.createEntity();
		MultiCounterComponent cmp = entity.edit()
			.create(MultiCounterComponent.class);

		repeat(2,
			parallel(
				repeat(50,
					increment(99, 0)
				),
				repeat(50,
					increment(100, 1)
				)
			)
		).register(world, entity.getId());

		process(world);

		assertEquals(99, cmp.counters[0]);
		assertEquals(100, cmp.counters[1]);


		cmp.reset();

		repeat(2,
			sequence(
				repeat(50,
					increment(49, 0)
				),
				repeat(50,
					increment(50, 1)
				)
			)
		).register(world, entity.getId());

		process(world);

		assertEquals(49, cmp.counters[0]);
		assertEquals(50, cmp.counters[1]);
	}

	@Test
	public void repeat_custom_operation() {
		World world = new World(
			new WorldConfiguration()
				.setSystem(OperationSystem.class));

		Entity entity = world.createEntity();
		MultiCounterComponent cmp = entity.edit()
			.create(MultiCounterComponent.class);

		repeat(10,
			increment(11, 0)
		).register(world, entity.getId());

		process(world);

		assertEquals(11, cmp.counters[0]);
	}


	public static class MultiCounterComponent extends PooledComponent {
		public static final int COUNTERS_COUNT = 2;
		public int[] counters = new int[COUNTERS_COUNT];

		@Override
		protected void reset() {
			for (int i = 0; i < COUNTERS_COUNT; ++i) {
				counters[i] = 0;
			}
		}
	}

	public static class IncrementOperation extends Operation {
		private int targetValue = 0;
		private int counterIndex = -1;


		public IncrementOperation() { }


		public void configure(int targetValue, int counterIndex) {
			this.targetValue = targetValue;
			this.counterIndex = counterIndex;
		}

		@Override
		public void rewind() {
			super.rewind();
		}

		@Override
		public void reset() {
			targetValue = 0;
			counterIndex = -1;
			super.reset();
		}


		@Override
		public Class<? extends Executor> executorType() {
			return IncrementOpExecutor.class;
		}


		public static class IncrementOpExecutor extends Executor<IncrementOperation> {
			ComponentMapper<MultiCounterComponent> mMultiCounter;

			@Override
			protected float act(float delta, IncrementOperation op, OperationTree node) {
				MultiCounterComponent cmp = mMultiCounter.get(op.entityId);

				if (cmp.counters[op.counterIndex] < op.targetValue) {
					cmp.counters[op.counterIndex] += 1;
				}

				if (cmp.counters[op.counterIndex] >= op.targetValue) {
					op.completed = true;
				}

				return delta;
			}
		}
	}

	public static IncrementOperation increment(int targetValue, int counterIndex) {
		IncrementOperation op = operation(IncrementOperation.class);
		op.configure(targetValue, counterIndex);

		return op;
	}
}
