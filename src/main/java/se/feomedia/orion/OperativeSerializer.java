package se.feomedia.orion;

import com.artemis.World;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import se.feomedia.orion.component.Operative;

public class OperativeSerializer implements Json.Serializer<Operative> {
	private final World world;

	public OperativeSerializer(World world) {
		this.world = world;
	}

	@Override
	public void write(Json json, Operative ot, Class knownType) {
		throw new RuntimeException();
	}

	@Override
	public Operative read(Json json, JsonValue jsonData, Class type) {
		throw new RuntimeException();
	}
}
