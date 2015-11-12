package se.feomedia.orion.operation;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.annotations.Wire;
import org.junit.Test;
import se.feomedia.orion.Executor;
import se.feomedia.orion.OperationTree;
import se.feomedia.orion.system.OperationSystem;

import static org.junit.Assert.*;
import static se.feomedia.orion.OperationFactory.delayTick;
import static se.feomedia.orion.OperationFactory.operation;
import static se.feomedia.orion.OperationFactory.sequence;

public class VoidEntityOperationTest {

	@Test
	public void test_when_killing_entities() {
		World w = new World(new WorldConfiguration()
			.setSystem(new OperationSystem()));

		HiThereOperation op = operation(HiThereOperation.class).s("hi");
		HiThereOperation op2 = operation(HiThereOperation.class).s("hepp");

		HiThereOperation eOp = operation(HiThereOperation.class).s("eOp");
		HiThereOperation eOp2 = operation(HiThereOperation.class).s("eOp2");

		sequence(
			delayTick(1),
			op,
			delayTick(1),
			delayTick(1),
			op2
		).register(w);

		int entity = w.create();
		sequence(
			delayTick(1),
			eOp,
			delayTick(1),
			delayTick(1),
			eOp2
		).register(w, entity);

		// 1
		w.delta = .1f;
		w.process();

		assertFalse(op.isComplete());
		assertFalse(op2.isComplete());
		assertFalse(eOp.isComplete());
		assertFalse(eOp2.isComplete());

		// 2
		w.delta = .1f;
		w.process();

		assertTrue(op.isComplete());
		assertFalse(op2.isComplete());
		assertTrue(eOp.isComplete());
		assertFalse(eOp2.isComplete());

		// 3
		w.delta = .1f;
		w.delete(entity);
		w.process();

		assertTrue(op.isComplete());
		assertFalse(op2.isComplete());
		assertTrue(eOp.isComplete());
		assertFalse(eOp2.isComplete());

		w.delta = .1f;
		w.process(); // 4

		assertTrue(op.isComplete());
		assertTrue(op2.isComplete());
		assertTrue(eOp.isComplete());
		assertFalse(eOp2.isComplete());

		assertEquals("hi!!!", op.s);
		assertEquals("hepp!!!", op2.s);
		assertEquals("eOp!!!", eOp.s);
		assertEquals("eOp2", eOp2.s);
	}

	public static class HiThereOperation extends SingleUseOperation {
		public String s;

		@Override
		public Class<? extends Executor> executorType() {
			return HiThereExecutor.class;
		}

		@Override
		public void reset() {
			super.reset();
			s = null;
		}

		public HiThereOperation s(String s) {
			this.s = s;
			return this;
		}

		@Wire
		public static class HiThereExecutor extends SingleUseExecutor<HiThereOperation> {
			@Override
			protected void act(HiThereOperation op, OperationTree node) {
				op.s += "!!!";
			}
		}
	}
}
