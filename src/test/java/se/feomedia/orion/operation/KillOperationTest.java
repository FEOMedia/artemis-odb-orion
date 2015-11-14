package se.feomedia.orion.operation;

import com.artemis.EntityManager;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.annotations.Wire;
import org.junit.Test;
import se.feomedia.orion.Executor;
import se.feomedia.orion.OperationTree;
import se.feomedia.orion.system.OperationSystem;

import static org.junit.Assert.*;
import static se.feomedia.orion.OperationFactory.*;

public class KillOperationTest {
	@Test
	public void kill_entity() {
		World world = new World(
			new WorldConfiguration()
				.setSystem(OperationSystem.class));

		EntityManager em = world.getEntityManager();

		int id1 = world.create();
		int id2 = world.create();
		int id3 = world.create();

		assertTrue(em.isActive(id1));
		assertTrue(em.isActive(id2));
		assertTrue(em.isActive(id3));

		// entity dies immediately
		killEntity().register(world, id1);


		// delete on 2nd frame
		sequence(
			delay(.25f),
			killEntity()
		).register(world, id2);

		// delete on 2nd frame
		sequence(
			delay(.25f),
			killEntity(),
			delay(.25f),
			operation(ThrowExceptionOperation.class)
		).register(world, id3);

		world.delta = .1f;
		world.process();

		assertFalse(em.isActive(id1));
		assertTrue(em.isActive(id2));
		assertTrue(em.isActive(id3));

		world.delta = .25f;
		world.process();

		assertFalse(em.isActive(id2));
		assertFalse(em.isActive(id3));

		world.delta = .25f;
		world.process();

		assertFalse(em.isActive(id3));
	}


	public static class ThrowExceptionOperation extends SingleUseOperation {


		@Override
		public Class<? extends Executor> executorType() {
			return ThrowExceptionExecutor.class;
		}

		@Wire
		public static class ThrowExceptionExecutor extends SingleUseExecutor<ThrowExceptionOperation> {
			@Override
			protected void act(ThrowExceptionOperation op, OperationTree node) {
				fail();
			}
		}
	}
}