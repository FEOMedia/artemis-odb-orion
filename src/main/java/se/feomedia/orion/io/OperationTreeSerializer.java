package se.feomedia.orion.io;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import se.feomedia.orion.InternalUtil;
import se.feomedia.orion.Operation;
import se.feomedia.orion.OperationTree;

public class OperationTreeSerializer implements Json.Serializer<OperationTree> {
	private final InternalUtil util = new InternalUtil(new Friend());

	public OperationTreeSerializer() {
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
			return util.toNode(op);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public static final class Friend {
		private Friend() {}
	}
}
