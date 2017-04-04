package se.feomedia.orion;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;

/** Stores all object pools. Thread-safe. */
class MultiPool {

	private static final ThreadLocal<MultiPool> context = new ThreadLocal<MultiPool>() {
		@Override
		protected MultiPool initialValue() {
			return new MultiPool();
		}
	};

	private final ObjectMap<Class, Pool<?>> pools = new ObjectMap<>();
	private final WeightPool weightPool = new WeightPool();
	private final Vector2Pool vector2Pool = new Vector2Pool();
	private final Array<Operation> toRemove = new Array<>();

	static final Array<Operation> toRemoveArray() {
		Array<Operation> toRemove = context.get().toRemove;
		toRemove.clear();
		return toRemove;
	}

	static OperationFactory.Weight weight() {
		return context.get().weightPool.obtain();
	}

	static Vector2 vector2() {
		return context.get().vector2Pool.obtain();
	}

	static <T extends Operation> T operation(Class<T> type) {
		return context.get().pool(type).obtain();
	}

	static <T extends Operation> void free(T operation) {
		Pool<T> pool = (Pool<T>) context.get().pool(operation.getClass());
		pool.free(operation);
	}

	static void free(Array<Operation> ops) {
		MultiPool mp = context.get();
		for (Operation op : ops)
			mp.pool((Class) op.getClass()).free(op);
	}

	private <T extends Operation> Pool<T> pool(Class<T> operationType) {
		Pool<T> pool = (Pool<T>) pools.get(operationType);
		if (pool == null) {
			pool = new OperationPool<>(operationType, Math.max(1, OperationFactory.initialPoolSize));
			pools.put(operationType, pool);
		}

		return pool;
	}

	static class OperationPool<T extends Operation> extends Pool<T> {
		private final Class<T> operationType;

		public OperationPool(Class<T> operationType, int initialSize) {
			super(initialSize);
			this.operationType = operationType;

			Array<T> initial = new Array<>(initialSize);
			for (int i = 0; initialSize > i; i++) {
				initial.add(newObject());
			}

			freeAll(initial);
		}

		@Override
		protected T newObject() {
			try {
				return operationType.newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	static class Vector2Pool {
		private int count;
		private final Vector2[] vectors;

		Vector2Pool() {
			vectors = new Vector2[0b111 + 1];
			for (int i = 0; i < vectors.length; i++)
				vectors[i] = new Vector2();
		}

		public Vector2 obtain() {
			// no need to reset, as x and y are set each time
			return vectors[count++ & 0b111];
		}
	}

	static class WeightPool extends Pool<OperationFactory.Weight> {
		@Override
		protected OperationFactory.Weight newObject() {
			return new OperationFactory.Weight();
		}
	}
}
