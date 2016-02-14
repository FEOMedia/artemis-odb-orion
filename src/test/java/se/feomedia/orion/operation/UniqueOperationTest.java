package se.feomedia.orion.operation;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import org.junit.Before;
import org.junit.Test;
import se.feomedia.orion.OperationTestUtil;
import se.feomedia.orion.system.OperationSystem;

import static org.junit.Assert.*;
import static se.feomedia.orion.OperationFactory.*;
import static se.feomedia.orion.OperationTestUtil.operatives;

public class UniqueOperationTest {
	private boolean bool1;
	private boolean bool2;

	@Before
	public void setup() {
		bool1 = bool2 = false;
	}

	@Test
	public void run_single_operation() {
		WorldConfiguration wc = new WorldConfiguration()
				.setSystem(OperationSystem.class);
		World world = new World(wc);

		sequence(
			unique("hello",
				run(new Runnable() {
					@Override
					public void run() {
						bool1 = true;
					}
				})
			),
			run(new Runnable() {
				@Override
				public void run() {
					bool2 = true;
				}
			})
		).register(world.createEntity());

		OperationTestUtil.process(world);

		assertTrue(bool1);
		assertTrue(bool2);
	}

	@Test
	public void replace_operation() {
		WorldConfiguration wc = new WorldConfiguration()
				.setSystem(OperationSystem.class);
		World world = new World(wc);

		Entity e = world.createEntity();
		unique("hello",
			sequence(
				delayTick(3),
				run(new Runnable() {
					@Override
					public void run() {
						fail();
					}
				})
			)
		).register(e);

		OperationTestUtil.process(world);
		assertFalse(bool2);

		unique("hello",
			run(new Runnable() {
				@Override
				public void run() {
					bool2 = true;
				}
			})
		).register(e);

		OperationTestUtil.process(world);
		OperationTestUtil.process(world);
		assertTrue(bool2);


		assertEquals(0, operatives(world).size());
	}

	@Test
	public void replace_non_entity_operation() {
		WorldConfiguration wc = new WorldConfiguration()
				.setSystem(OperationSystem.class);
		World world = new World(wc);

		unique("hello",
			sequence(
				delayTick(3),
				run(new Runnable() {
					@Override
					public void run() {
						fail();
					}
				})
			)
		).register(world);

		OperationTestUtil.process(world);
		assertFalse(bool2);

		unique("hello",
			run(new Runnable() {
				@Override
				public void run() {
					bool2 = true;
				}
			})
		).register(world);

		OperationTestUtil.process(world);
		OperationTestUtil.process(world);
		assertTrue(bool2);

		assertEquals(0, operatives(world).size());
	}
}
