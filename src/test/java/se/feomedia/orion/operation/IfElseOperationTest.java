package se.feomedia.orion.operation;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import org.junit.Test;
import se.feomedia.orion.system.OperationSystem;

import static org.junit.Assert.assertEquals;
import static se.feomedia.orion.OperationFactory.*;

public class IfElseOperationTest {
	@Test
	public void ifElse_true_test() {
		World world = new World(new WorldConfiguration()
			.setSystem(new OperationSystem()));

		MySingleUseOperation trueOp = operation(MySingleUseOperation.class);
		MySingleUseOperation falseOp = operation(MySingleUseOperation.class);

		ifTrue(true, trueOp).elseDo(falseOp).register(world, world.create());

		world.delta = 1f;
		world.process();

		assertEquals(1, trueOp.timesRun);
		assertEquals(0, falseOp.timesRun);
	}

	@Test
	public void ifElse_false_test() {
		World world = new World(new WorldConfiguration()
			.setSystem(new OperationSystem()));

		MySingleUseOperation trueOp = operation(MySingleUseOperation.class);
		MySingleUseOperation falseOp = operation(MySingleUseOperation.class);

		ifTrue(false, trueOp).elseDo(falseOp).register(world, world.create());

		world.delta = 1f;
		world.process();

		assertEquals(0, trueOp.timesRun);
		assertEquals(1, falseOp.timesRun);
	}

	@Test
	public void ifTrue_test() {
		World world = new World(new WorldConfiguration()
			.setSystem(new OperationSystem()));

		MySingleUseOperation trueOp = operation(MySingleUseOperation.class);

		ifTrue(true, trueOp).register(world, world.create());

		world.delta = 1f;
		world.process();

		assertEquals(1, trueOp.timesRun);

		world.process();

		ifTrue(true, null).register(world, world.create());
		ifTrue(false, null).register(world, world.create());

		world.process();
		world.delta = 1f;
	}

	@Test
	public void ifFalse_test() {
		World world = new World(new WorldConfiguration()
			.setSystem(new OperationSystem()));

		MySingleUseOperation falseOp = operation(MySingleUseOperation.class);

		ifFalse(false, falseOp).register(world, world.create());

		world.delta = 1f;
		world.process();

		assertEquals(1, falseOp.timesRun);

		world.process();

		ifFalse(false, null).register(world, world.create());
		ifFalse(true, null).register(world, world.create());

		world.process();
		world.delta = 1f;
	}
}
