package se.feomedia.orion;

import com.artemis.World;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class OperationTreeSerializer implements Json.Serializer<OperationTree> {
	private final World world;

	public OperationTreeSerializer(World world) {
		this.world = world;
	}

	@Override
	public void write(Json json, OperationTree ot, Class knownType) {
		json.writeObjectStart();
		json.writeFields(ot);
		json.writeObjectEnd();
	}

	@Override
	public OperationTree read(Json json, JsonValue jsonData, Class type) {
		try {
			Class<Operation> opType =
				(Class<Operation>) Class.forName(jsonData.child.child.asString());
			Operation op = json.readValue(opType, jsonData.child);

			return op.toNode();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
