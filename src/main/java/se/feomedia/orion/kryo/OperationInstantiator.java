package se.feomedia.orion.kryo;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pools;
import com.esotericsoftware.kryo.Kryo;
import org.objenesis.instantiator.ObjectInstantiator;
import org.objenesis.strategy.InstantiatorStrategy;
import se.feomedia.orion.Operation;

public class OperationInstantiator implements org.objenesis.strategy.InstantiatorStrategy {
	private final InstantiatorStrategy fallback;
	private ObjectMap<Class<?>, ObjectInstantiator<?>> instantiators = new ObjectMap<>();

	public OperationInstantiator() {
		fallback = new Kryo.DefaultInstantiatorStrategy();
	}

	@Override
	public <T> ObjectInstantiator<T> newInstantiatorOf(Class<T> type) {
		if (Operation.class.isAssignableFrom(type)) {
			return instantiator(type);
		} else {
			return fallback.newInstantiatorOf(type);
		}
	}

	private <T> ObjectInstantiator<T> instantiator(Class<T> type) {
		ObjectInstantiator<T> instantiator = (ObjectInstantiator<T>) instantiators.get(type);
		if (instantiator == null) {
			instantiator = new PooledInstantiator<>(type);
			instantiators.put(type, instantiator);
		}

		return instantiator;
	}

	private static class PooledInstantiator<T> implements ObjectInstantiator<T> {
		private final Class<T> type;

		private PooledInstantiator(Class<T> type) {
			this.type = type;
		}

		@Override
		public T newInstance() {
			return Pools.obtain(type);
		}
	}
}
