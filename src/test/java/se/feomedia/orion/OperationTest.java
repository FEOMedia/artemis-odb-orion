package se.feomedia.orion;

import com.artemis.MundaneWireException;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.annotations.Wire;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import org.junit.Test;
import se.feomedia.orion.operation.DelayOperation;
import se.feomedia.orion.operation.KillOperation;
import se.feomedia.orion.operation.MySingleUseOperation;
import se.feomedia.orion.operation.SingleUseOperation;
import se.feomedia.orion.system.OperationSystem;

import static org.junit.Assert.*;
import static se.feomedia.orion.OperationFactory.*;
import static se.feomedia.orion.OperationTestUtil.operatives;

public class OperationTest {
	@Test
	public void test_hiearhical_clean_up() {
		World world = new World(new WorldConfiguration()
			.setSystem(OperationSystem.class));

		KillOperation killOperation = killEntity();

		sequence(
			sequence(
				sequence(
					parallel(
						sequence(),
						sequence(killOperation)
					)
				)
			)
		).register(world, world.create());

		world.delta = .25f;
		world.process();

		assertSame(killOperation, killEntity());
		assertNotSame(killOperation, killEntity());
	}



	@Test
	public void testWireInjection() {
		World world = new World(new WorldConfiguration()
				.setSystem(new GroupManager())
				.setSystem(new TagManager())
				.setSystem(new OperationSystem()));

		ManagerOperation action = operation(ManagerOperation.class);
		action.register(world, world.create());

		world.process();
		world.process();
	}

	@Test
	public void testSequentialProcessing() {

		OperationSystem operationSystem = new OperationSystem();
		World world = new World(new WorldConfiguration()
				.setSystem(operationSystem));

		DelayOperation[] ds = new DelayOperation[] {
			delay(0.25f),
			delay(0.25f),
			delay(0.25f),
			delay(0.25f)
		};

		sequence(ds[0], ds[1], ds[2], ds[3]).register(world, world.create());

		world.delta = 0;
		world.process();

		assertEquals(1, operatives(world).size());

		for (int i = 0; i < ds.length; i++) {
			assertFalse(ds[i].isComplete());
			assertEquals(.25f, ds[i].remaining(), .001f);
		}

		world.delta = 0.15f;
		world.process();

		assertFalse(ds[0].isComplete());
		assertEquals(.25f - 0.15f, ds[0].remaining(), .001f);
		for (int i = 1; i < ds.length; i++) {
			assertFalse(ds[i].isComplete());
			assertEquals(.25f, ds[i].remaining(), .001f);
		}

		world.delta = 0.75f;
		world.process();

		for (int i = 0; i < ds.length - 1; i++) {
			assertTrue(ds[i].isComplete());
			assertEquals(0f, ds[i].remaining(), 0.0000000f);
		}

		assertFalse(ds[3].isComplete());
		assertEquals(.25f - 0.15f, ds[3].remaining(), .001f);

		world.delta = 0.25f;
		world.process();
		world.process();

		// orion should be evicted from the system here.
		assertEquals(0, operatives(world).size());

//		assertEquals(0, operationSystem.getOperations().size);
	}

	@Test
	public void testParallelProcessing() {
		OperationSystem operationSystem = new OperationSystem();
		World world = new World(new WorldConfiguration()
				.setSystem(operationSystem));

		DelayOperation[] ds = new DelayOperation[] {
			delay(0.25f),
			delay(0.25f),
			delay(0.25f),
			delay(0.75f)
		};

		parallel(ds[0], ds[1], ds[2], ds[3]).register(world, world.create());

		world.delta = 0.000001f;
		world.process();

		for (int i = 0; i < ds.length - 1; i++) {
			assertFalse(ds[i].isComplete());
			assertEquals(.25f, ds[i].remaining(), .001f);
		}

		world.delta = 0.15f;
		world.process();

		for (int i = 0; i < ds.length - 1; i++) {
			assertFalse(ds[i].isComplete());
			assertEquals(.25f - 0.15f, ds[i].remaining(), .001f);
		}
		assertEquals(.75f - 0.15f, ds[3].remaining(), .001f);

		world.delta = 0.5f;
		world.process();

		for (int i = 0; i < ds.length - 1; i++) {
			assertTrue(ds[i].isComplete());
			assertEquals(0f, ds[i].remaining(), 0.0001f);
		}
		assertEquals(0.75f - 0.65f, ds[3].remaining(), 0.0001f);
		assertFalse(ds[3].isComplete());

		world.delta = 0.5f;
		world.process();
		world.process();

		// orion should be evicted from the system here.
		assertEquals(0, operatives(world).size());
	}

	@Test
	public void testSingleUseOperation() {
		OperationSystem operationSystem = new OperationSystem();
		World world = new World(new WorldConfiguration()
			.setSystem(operationSystem));

		MySingleUseOperation op = operation(MySingleUseOperation.class);
		op.register(world, world.create());

		process(world);

		assertEquals(0, operatives(world).size());
		assertSame(op, operation(MySingleUseOperation.class));
	}

	@Test(expected = MundaneWireException.class)
	public void no_wire_failed_inject_test() {
		World world = new World(new WorldConfiguration()
			.setSystem(OperationSystem.class));

		operation(NoWireNoInjectOperation.class).register(world);

		process(world);
	}

	private static void process(World world) {
		world.delta = 1f / 60f;
		world.process();
	}

	public static class ManagerOperation extends Operation {
		@Override
		public Class<? extends Executor> executorType() {
			return ManagerExecutor.class;
		}

		@Wire
		public static class ManagerExecutor extends Executor<ManagerOperation> {
			private GroupManager groupManager;
			private GroupManager tagManager;

			@Override
			protected float act(float delta, ManagerOperation op, OperationTree node) {
				assertNotNull(groupManager);
				assertNotNull(tagManager);

				op.completed = true;

				groupManager = tagManager = null;

				return delta;
			}
		}
	}

	public static class NoWireNoInjectOperation extends SingleUseOperation {
		@Override
		public Class<? extends Executor> executorType() {
			return NoWireNoInjectExecutor.class;
		}

		public static class NoWireNoInjectExecutor extends SingleUseOperation.SingleUseExecutor<NoWireNoInjectOperation> {
			private GroupManager groupManager;
			private GroupManager tagManager;

			@Override
			protected void act(NoWireNoInjectOperation op, OperationTree node) {
				assertNull(groupManager);
				assertNull(tagManager);
			}
		}
	}

}