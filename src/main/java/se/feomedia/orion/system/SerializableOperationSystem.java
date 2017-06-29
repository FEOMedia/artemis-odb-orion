package se.feomedia.orion.system;

import com.artemis.Aspect;
import com.badlogic.gdx.math.OrionKryoSerialization;
import com.esotericsoftware.kryo.Kryo;
import se.feomedia.orion.Operation;
import se.feomedia.orion.component.Operative;

public class SerializableOperationSystem extends OperationSystem {

	public Kryo kryo;

	/**
	 * Constructor with specialized aspect. Can be used to add <code>exclude</code>
	 * components for entities otherwise marked as disabled.
	 *
	 * @param base Useful for excluding disabled entities in projects.
	 */
	public SerializableOperationSystem(Aspect.Builder base) {
		super(base.all(Operative.class));
	}


	@Override
	protected void initialize() {
		kryo = new Kryo();
		OrionKryoSerialization.configure(kryo);
	}

	public <T extends Operation> T copy(T operation) {
		return kryo.copy(operation);
	}
}
