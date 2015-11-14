package se.feomedia.orion;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;
import se.feomedia.orion.operation.*;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.utils.NumberUtils.floatToRawIntBits;
import static com.badlogic.gdx.utils.NumberUtils.intBitsToFloat;

public final class OperationFactory {

	private static final Vector2 xy = new Vector2();
	private static final Friend friend = new Friend();

	private static final ObjectMap<Class<? extends Operation>, Pool<?>> pools
			= new ObjectMap<>();

	private OperationFactory() {}

	public static SequenceOperation sequence() {
		SequenceOperation action = operation(SequenceOperation.class);
		return action;
	}

	public static SequenceOperation sequence(Operation a) {
		SequenceOperation action = operation(SequenceOperation.class);
		action.addChild(a);
		return action;
	}

	public static SequenceOperation sequence(Operation a, Operation b) {
		SequenceOperation action = operation(SequenceOperation.class);
		action.addChild(a);
		action.addChild(b);
		return action;
	}

	public static SequenceOperation sequence(Operation a, Operation b, Operation c) {
		SequenceOperation action = operation(SequenceOperation.class);
		action.addChild(a);
		action.addChild(b);
		action.addChild(c);
		return action;
	}

	public static SequenceOperation sequence(Operation a, Operation b, Operation c, Operation d) {
		SequenceOperation action = operation(SequenceOperation.class);
		action.addChild(a);
		action.addChild(b);
		action.addChild(c);
		action.addChild(d);
		return action;
	}

	public static SequenceOperation sequence(Operation a, Operation b, Operation c, Operation d, Operation ...e) {
		SequenceOperation action = operation(SequenceOperation.class);
		action.addChild(a);
		action.addChild(b);
		action.addChild(c);
		action.addChild(d);
		for (int i = 0; i < e.length; i++) {
			action.addChild(e[i]);
		}
		return action;
	}

	public static ParallelOperation parallel() {
		ParallelOperation action = operation(ParallelOperation.class);
		return action;
	}

	public static ParallelOperation parallel(Operation a) {
		ParallelOperation action = operation(ParallelOperation.class);
		action.addChild(a);
		return action;
	}

	public static ParallelOperation parallel(Operation a, Operation b) {
		ParallelOperation action = operation(ParallelOperation.class);
		action.addChild(a);
		action.addChild(b);
		return action;
	}

	public static ParallelOperation parallel(Operation a, Operation b, Operation c) {
		ParallelOperation action = operation(ParallelOperation.class);
		action.addChild(a);
		action.addChild(b);
		action.addChild(c);
		return action;
	}

	public static ParallelOperation parallel(Operation a, Operation b, Operation c, Operation d) {
		ParallelOperation action = operation(ParallelOperation.class);
		action.addChild(a);
		action.addChild(b);
		action.addChild(c);
		action.addChild(d);
		return action;
	}

	public static ParallelOperation parallel(Operation a, Operation b, Operation c, Operation d, Operation ...e) {
		ParallelOperation action = operation(ParallelOperation.class);
		action.addChild(a);
		action.addChild(b);
		action.addChild(c);
		action.addChild(d);
		for (int i = 0; i < e.length; i++) {
			action.addChild(e[i]);
		}
		return action;
	}

	public static DelayOperation delay(float duration) {
		DelayOperation action = operation(DelayOperation.class);
		action.duration = duration;

		return action;
	}

	public static RepeatOperation repeat(int times, Operation operation) {
		RepeatOperation op = operation(RepeatOperation.class);
		op.configure(times, operation);

		return op;
	}

	public static DelayTickOperation delayTick(int ticksToWait) {
		DelayTickOperation action = operation(DelayTickOperation.class);
		action.ticksToWait = ticksToWait;

		return action;
	}

	public static KillOperation killEntity() {
		KillOperation op = operation(KillOperation.class);
		return op;
	}

	private static IfElseOperation ifElse(boolean b, Operation ifTrue) {
		IfElseOperation op = operation(IfElseOperation.class);
		op.configure(b, ifTrue);
		return op;
	}

	public static IfElseOperation ifTrue(boolean b, Operation ifTrue) {
		return ifElse(b, ifTrue);
	}

	public static IfElseOperation ifFalse(boolean b, Operation ifFalse) {
		return ifElse(!b, ifFalse);
	}

	public static <T extends Operation> T operation(final Class<T> operationType) {
		return pool(operationType).obtain();
	}

	static <T extends Operation> void free(Operation op) {
		((Pool) pool(op.getClass())).free(op);
	}

	private static <T extends Operation> Pool<T> pool(Class<T> operationType) {
		Pool<T> pool = (Pool<T>) pools.get(operationType);
		if (pool == null) {
			pool = new OperationPool<>(operationType);
			pools.put(operationType, pool);
		}

		return pool;
	}


	public static float seconds(float value) {
		if (value == 0) {
			int raw = floatToRawIntBits(value);
			value = intBitsToFloat(raw + 1);
		}

		return value;
	}

	public static float secondsRng(float max) {
		return secondsRng(0, seconds(max));
	}

	public static float secondsRng(float min, float max) {
		return random(seconds(min), max);
	}

	public static Vector2 xy(float x, float y) {
		return xy.set(x, y);
	}

	public static <T extends TemporalOperation> T configure(T op,
	                                                        float duration,
	                                                        Interpolation interpolation) {
		op.duration = duration;
		op.interpolation = interpolation;
		return op;
	}

	public static final class Friend {
		private Friend() {}
	}

	static class OperationPool<T extends Operation> extends Pool<T> {
		private final Class<T> operationType;

		public OperationPool(Class<T> operationType) {
			this.operationType = operationType;
		}

		@Override
		protected T newObject() {
			try {
				return operationType.newInstance();
			} catch (InstantiationException	| IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
