package se.feomedia.orion;

import com.artemis.*;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import org.junit.Test;
import se.feomedia.orion.component.RecordOperationId;
import se.feomedia.orion.operation.RecordIdOperation;
import se.feomedia.orion.system.OperationSystem;

import static org.junit.Assert.*;
import static se.feomedia.orion.OperationFactory.*;
import static se.feomedia.orion.OperationTestUtil.operatives;
import static se.feomedia.orion.OperationTestUtil.process;


@Wire(failOnNull = false)
public class ForkOperationTest {
	private ComponentMapper<RecordOperationId> idMapper;
	private TagManager tags;

	@Test
	public void fork_with_entity_ids() {
		World world = new World(new WorldConfiguration()
			.setSystem(OperationSystem.class));

		world.inject(this);

		Entity e = world.createEntity();
		int f0 = createForked(world);
		int f1 = createForked(world);
		int f2 = createForked(world);

		final boolean[] poke = new boolean[1];

		sequence(
			parallel(
				fork(f0, idOperation()),
				fork(f1,
					sequence(
						ifTrue(true,
							idOperation()
						)
					)
				),
				fork(f2,
					parallel(
						sequence(
							delayTick(1),
							idOperation()
						)
					)
				)
			),
			run(new Runnable() {
				@Override
				public void run() {
					poke[0] = true;
				}
			})
		).register(e);

		process(world);

		assertEquals(1, operatives(world).size());
		assertEquals(f0, idMapper.get(f0).idFromOperation);
		assertEquals(f1, idMapper.get(f1).idFromOperation);
		assertNotEquals(f2, idMapper.get(f2).idFromOperation);
		assertFalse(poke[0]);

		process(world);

		assertEquals(0, operatives(world).size());
		assertEquals(f0, idMapper.get(f0).idFromOperation);
		assertEquals(f1, idMapper.get(f1).idFromOperation);
		assertEquals(f2, idMapper.get(f2).idFromOperation);
		assertTrue(poke[0]);
	}

	@Test
	public void fork_with_tags() {
		World world = new World(new WorldConfiguration()
			.setSystem(TagManager.class)
			.setSystem(OperationSystem.class));

		world.inject(this);

		Entity e = world.createEntity();
		int f0 = createForked(world, "f0");
		int f1 = createForked(world, "f1");
		int f2 = createForked(world, "f2");

		final boolean[] poke = new boolean[1];

		sequence(
			parallel(
				fork("f0", idOperation()),
				fork("f1",
					sequence(
						ifTrue(true,
							idOperation()
						)
					)
				),
				fork("f2",
					parallel(
						sequence(
							delayTick(1),
							idOperation()
						)
					)
				)
			),
			run(new Runnable() {
				@Override
				public void run() {
					poke[0] = true;
				}
			})
		).register(e);

		process(world);

		assertEquals(1, operatives(world).size());
		assertEquals(f0, idMapper.get(f0).idFromOperation);
		assertEquals(f1, idMapper.get(f1).idFromOperation);
		assertNotEquals(f2, idMapper.get(f2).idFromOperation);
		assertFalse(poke[0]);

		process(world);

		assertEquals(0, operatives(world).size());
		assertEquals(f0, idMapper.get(f0).idFromOperation);
		assertEquals(f1, idMapper.get(f1).idFromOperation);
		assertEquals(f2, idMapper.get(f2).idFromOperation);
		assertTrue(poke[0]);
	}

	private int createForked(World world, String tag) {
		int e = world.create();
		idMapper.create(e);
		tags.register(tag, e);

		return e;
	}

	private int createForked(World world) {
		int e = world.create();
		idMapper.create(e);

		return e;
	}

	private static RecordIdOperation idOperation() {
		return operation(RecordIdOperation.class);
	}
}
