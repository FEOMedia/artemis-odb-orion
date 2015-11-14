package se.feomedia.orion;

import com.artemis.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import se.feomedia.orion.component.Operative;

public class OperationTreeSerializer implements Json.Serializer<OperationTree> {
	private final World world;

	public OperationTreeSerializer(World world) {
		this.world = world;
	}

	@Override
	public void write(Json json, OperationTree ot, Class knownType) {
		json.writeObjectStart(ot.operation.getClass(), Operation.class);
		json.writeFields(ot.operation);
		json.writeObjectEnd();
	}

	@Override
	public OperationTree read(Json json, JsonValue jsonData, Class type) {
		throw new RuntimeException();
	}
}
