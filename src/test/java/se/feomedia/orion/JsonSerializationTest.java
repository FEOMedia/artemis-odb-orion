package se.feomedia.orion;

import com.artemis.Aspect;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.annotations.Wire;
import com.artemis.io.JsonArtemisSerializer;
import com.artemis.io.SaveFileFormat;
import com.artemis.managers.WorldSerializationManager;
import org.junit.Test;
import se.feomedia.orion.component.Operative;
import se.feomedia.orion.io.InterpolationSerializer;
import se.feomedia.orion.io.OperationTreeSerializer;
import se.feomedia.orion.operation.SingleUseOperation;
import se.feomedia.orion.system.OperationSystem;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import static com.artemis.Aspect.all;
import static org.junit.Assert.*;
import static se.feomedia.orion.OperationFactory.*;
import static se.feomedia.orion.OperationTestUtil.process;

public class JsonSerializationTest {
	@Test
	public void save_load_entity_with_operation() {
		String[] string = new String[1];
		int[] entity = new int[1];

		World world = new World(new WorldConfiguration()
			.register("string", string)
			.register("entity", entity)
			.setSystem(OperationSystem.class)
			.setSystem(WorldSerializationManager.class));

		WorldSerializationManager worldSerializer =
			world.getSystem(WorldSerializationManager.class);

		JsonArtemisSerializer backend = new JsonArtemisSerializer(world);
		backend.prettyPrint(true);
		InterpolationSerializer.registerAll(backend);
		backend.register(OperationTree.class, new OperationTreeSerializer(world));
		worldSerializer.setSerializer(backend);

		int e0 = world.create();
		int e1 = world.create();

		process(world);


		assertEquals(null, string[0]);
		assertEquals(0, entity[0]);

		sequence(
			delayTick(1),
			operation(EntityWriterOperation.class)
		).register(world, e1);

		process(world);
		String saveE1 = save(world, all(Operative.class));

		process(world);

		assertNotNull(string[0]);
		assertEquals(1, entity[0]);

		ByteArrayInputStream is = new ByteArrayInputStream(
				saveE1.getBytes(StandardCharsets.UTF_8));
		SaveFileFormat load = worldSerializer.load(is, SaveFileFormat.class);

		int e2 = load.entities.get(0);
		assertEquals(1, load.entities.size());
		assertNotEquals(e1, e2);

		// making sure values haven't changed due to loading
		assertNotNull(string[0]);
		assertEquals(1, entity[0]);

		world.process();

		assertNotNull(string[0]);
		assertEquals(e2, entity[0]);
	}

	private static String save(World world, Aspect.Builder aspect) {
		EntitySubscription subscription =
			world.getAspectSubscriptionManager().get(aspect);

		return save(world.getSystem(WorldSerializationManager.class), subscription);
	}

	private static String save(WorldSerializationManager manager, EntitySubscription subscription) {
		StringWriter writer = new StringWriter();
		SaveFileFormat save = new SaveFileFormat(subscription.getEntities());
		manager.save(writer, save);
		return writer.toString();
	}

	public static class EntityWriterOperation extends SingleUseOperation {
		@Override
		public Class<? extends Executor> executorType() {
			return EntityWriterExecutor.class;
		}

		@Wire
		public static class EntityWriterExecutor extends SingleUseExecutor<EntityWriterOperation> {
			@Wire(name = "string")
			String[] string;
			@Wire(name = "entity")
			int[] entity;

			@Override
			protected void act(EntityWriterOperation op, OperationTree node) {
				OperationTree root = node;
				while (root.parent() != null) {
					root = root.parent();
				}

				string[0] = node.toString();
				entity[0] = op.entityId;
			}
		}
	}
}
