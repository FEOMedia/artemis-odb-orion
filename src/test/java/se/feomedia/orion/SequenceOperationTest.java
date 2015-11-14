package se.feomedia.orion;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import org.junit.Test;
import se.feomedia.orion.operation.MySingleUseOperation;
import se.feomedia.orion.operation.SequenceOperation;
import se.feomedia.orion.system.OperationSystem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static se.feomedia.orion.OperationFactory.*;
import static se.feomedia.orion.OperationTestUtil.operatives;
import static se.feomedia.orion.OperationTestUtil.process;

public class SequenceOperationTest {
	@Test
	public void reports_complete_immediately_when_done() {
		World world = new World(new WorldConfiguration()
			.setSystem(OperationSystem.class));

		int entityId = world.create();

		SequenceOperation seq = sequence(
			delayTick(1),
			operation(MySingleUseOperation.class)
		);
		seq.register(world, entityId);

		process(world);
		assertEquals(1, operatives(world).size());

		process(world);
		assertEquals(0, operatives(world).size());
		assertSame(seq, sequence());
	}
}
