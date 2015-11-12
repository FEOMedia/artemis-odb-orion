package se.feomedia.orion.operation;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import org.junit.Test;
import se.feomedia.orion.system.OperationSystem;

import static org.junit.Assert.assertEquals;
import static se.feomedia.orion.OperationFactory.delayTick;
import static se.feomedia.orion.OperationFactory.sequence;

public class DelayTickTest {
	@Test
	public void test_delay() {
		World w = new World(new WorldConfiguration()
			.setSystem(OperationSystem.class));

		DelayTickOperation op1 = delayTick(1);
		DelayTickOperation op2 = delayTick(2);
		DelayTickOperation op3 = delayTick(1);

		sequence(op1, op2, op3).register(w.createEntity());
		assertEquals(false, op1.isComplete());
		assertEquals(false, op2.isComplete());
		assertEquals(false, op3.isComplete());

		w.delta = .01f;
		w.process();
		assertEquals(true, op1.isComplete());
		assertEquals(false, op2.isComplete());
		assertEquals(false, op3.isComplete());

		w.delta = .01f;
		w.process();
		assertEquals(true, op1.isComplete());
		assertEquals(false, op2.isComplete());
		assertEquals(false, op3.isComplete());

		w.delta = .01f;
		w.process();
		assertEquals(true, op1.isComplete());
		assertEquals(true, op2.isComplete());
		assertEquals(false, op3.isComplete());

		w.delta = .01f;
		w.process();
		assertEquals(true, op1.isComplete());
		assertEquals(true, op2.isComplete());
		assertEquals(true, op3.isComplete());
	}

}
