package se.feomedia.orion.kryo;

import com.badlogic.gdx.utils.ObjectMap;
import com.esotericsoftware.kryo.Kryo;
import org.objenesis.instantiator.ObjectInstantiator;
import org.objenesis.strategy.InstantiatorStrategy;
import se.feomedia.orion.Operation;

import static se.feomedia.orion.OperationFactory.operation;

public class OperationInstantiator<T extends Operation> implements InstantiatorStrategy {
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
		ObjectInstantiator<?> instantiator = instantiators.get(type);
		if (instantiator == null) {
			instantiator = new PooledInstantiator(type);
			instantiators.put(type, instantiator);
		}

		return (ObjectInstantiator<T>) instantiator;
	}

	private static class PooledInstantiator<T extends Operation> implements ObjectInstantiator<T> {
		private final Class<T> type;

		private PooledInstantiator(Class<T> type) {
			this.type = type;
		}

		@Override
		public T newInstance() {
			return operation(type);
		}
	}
}
