package se.feomedia.orion.io;

import com.artemis.io.JsonArtemisSerializer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class InterpolationSerializer {

	public static void registerAll(JsonArtemisSerializer backend) {
		backend.register(Interpolation.Pow.class, new PowSerializer());
		backend.register(Interpolation.PowIn.class, new PowInSerializer());
		backend.register(Interpolation.PowOut.class, new PowOutSerializer());
		backend.register(Interpolation.Exp.class, new ExpSerializer());
		backend.register(Interpolation.ExpIn.class, new ExpInSerializer());
		backend.register(Interpolation.ExpOut.class, new ExpOutSerializer());
		backend.register(Interpolation.Elastic.class, new ElasticSerializer());
		backend.register(Interpolation.ElasticIn.class, new ElasticInSerializer());
		backend.register(Interpolation.ElasticOut.class, new ElasticOutSerializer());
		backend.register(Interpolation.Bounce.class, new BounceSerializer());
		backend.register(Interpolation.BounceIn.class, new BounceInSerializer());
		backend.register(Interpolation.BounceOut.class, new BounceOutSerializer());
		backend.register(Interpolation.Swing.class, new SwingSerializer());
		backend.register(Interpolation.SwingIn.class, new SwingInSerializer());
		backend.register(Interpolation.SwingOut.class, new SwingOutSerializer());
	}

	public static void registerAll(Json backend) {
		backend.setSerializer(Interpolation.Pow.class, new PowSerializer());
		backend.setSerializer(Interpolation.PowIn.class, new PowInSerializer());
		backend.setSerializer(Interpolation.PowOut.class, new PowOutSerializer());
		backend.setSerializer(Interpolation.Exp.class, new ExpSerializer());
		backend.setSerializer(Interpolation.ExpIn.class, new ExpInSerializer());
		backend.setSerializer(Interpolation.ExpOut.class, new ExpOutSerializer());
		backend.setSerializer(Interpolation.Elastic.class, new ElasticSerializer());
		backend.setSerializer(Interpolation.ElasticIn.class, new ElasticInSerializer());
		backend.setSerializer(Interpolation.ElasticOut.class, new ElasticOutSerializer());
		backend.setSerializer(Interpolation.Bounce.class, new BounceSerializer());
		backend.setSerializer(Interpolation.BounceIn.class, new BounceInSerializer());
		backend.setSerializer(Interpolation.BounceOut.class, new BounceOutSerializer());
		backend.setSerializer(Interpolation.Swing.class, new SwingSerializer());
		backend.setSerializer(Interpolation.SwingIn.class, new SwingInSerializer());
		backend.setSerializer(Interpolation.SwingOut.class, new SwingOutSerializer());
	}

	static void write(Json json, Interpolation object) {
		json.writeObjectStart(object.getClass(), null);
		json.writeFields(object);
		json.writeObjectEnd();
	}

	public static class PowSerializer implements Json.Serializer<Interpolation.Pow> {
		@Override
		public void write(Json json, Interpolation.Pow obj, Class knownType) {
			InterpolationSerializer.write(json, obj);
		}

		@Override
		public Interpolation.Pow read(Json json, JsonValue jsonData, Class type) {
			Interpolation.Pow interp = new Interpolation.Pow(0);
			json.readFields(interp, jsonData);
			return interp;
		}
	}

	public static class PowInSerializer implements Json.Serializer<Interpolation.PowIn> {
		@Override
		public void write(Json json, Interpolation.PowIn obj, Class knownType) {
			InterpolationSerializer.write(json, obj);
		}

		@Override
		public Interpolation.PowIn read(Json json, JsonValue jsonData, Class type) {
			Interpolation.PowIn interp = new Interpolation.PowIn(0);
			json.readFields(interp, jsonData);
			return interp;
		}
	}

	public static class PowOutSerializer implements Json.Serializer<Interpolation.PowOut> {
		@Override
		public void write(Json json, Interpolation.PowOut obj, Class knownType) {
			InterpolationSerializer.write(json, obj);
		}

		@Override
		public Interpolation.PowOut read(Json json, JsonValue jsonData, Class type) {
			Interpolation.PowOut interp = new Interpolation.PowOut(0);
			json.readFields(interp, jsonData);
			return interp;
		}
	}

	public static class ExpSerializer implements Json.Serializer<Interpolation.Exp> {
		@Override
		public void write(Json json, Interpolation.Exp object, Class knownType) {
			InterpolationSerializer.write(json, object);
		}

		@Override
		public Interpolation.Exp read(Json json, JsonValue jsonData, Class type) {
			Interpolation.Exp interp = new Interpolation.Exp(1, 1);
			json.readFields(interp, jsonData);
			return interp;
		}
	}

	public static class ExpInSerializer implements Json.Serializer<Interpolation.ExpIn> {
		@Override
		public void write(Json json, Interpolation.ExpIn object, Class knownType) {
			InterpolationSerializer.write(json, object);
		}

		@Override
		public Interpolation.ExpIn read(Json json, JsonValue jsonData, Class type) {
			Interpolation.ExpIn interp = new Interpolation.ExpIn(1, 1);
			json.readFields(interp, jsonData);
			return interp;
		}
	}

	public static class ExpOutSerializer implements Json.Serializer<Interpolation.ExpOut> {
		@Override
		public void write(Json json, Interpolation.ExpOut object, Class knownType) {
			InterpolationSerializer.write(json, object);
		}

		@Override
		public Interpolation.ExpOut read(Json json, JsonValue jsonData, Class type) {
			Interpolation.ExpOut interp = new Interpolation.ExpOut(1, 1);
			json.readFields(interp, jsonData);
			return interp;
		}
	}


	public static class ElasticSerializer implements Json.Serializer<Interpolation.Elastic> {
		@Override
		public void write(Json json, Interpolation.Elastic object, Class knownType) {
			InterpolationSerializer.write(json, object);
		}

		@Override
		public Interpolation.Elastic read(Json json, JsonValue jsonData, Class type) {
			Interpolation.Elastic interp = new Interpolation.Elastic(0, 0, 0, 0);
			json.readFields(interp, jsonData);
			return interp;
		}
	}

	public static class ElasticInSerializer implements Json.Serializer<Interpolation.ElasticIn> {
		@Override
		public void write(Json json, Interpolation.ElasticIn object, Class knownType) {
			InterpolationSerializer.write(json, object);
		}

		@Override
		public Interpolation.ElasticIn read(Json json, JsonValue jsonData, Class type) {
			Interpolation.ElasticIn interp = new Interpolation.ElasticIn(0, 0, 0, 0);
			json.readFields(interp, jsonData);
			return interp;
		}
	}

	public static class ElasticOutSerializer implements Json.Serializer<Interpolation.ElasticOut> {
		@Override
		public void write(Json json, Interpolation.ElasticOut object, Class knownType) {
			InterpolationSerializer.write(json, object);
		}

		@Override
		public Interpolation.ElasticOut read(Json json, JsonValue jsonData, Class type) {
			Interpolation.ElasticOut interp = new Interpolation.ElasticOut(0, 0, 0, 0);
			json.readFields(interp, jsonData);
			return interp;
		}
	}

	public static class BounceSerializer implements Json.Serializer<Interpolation.Bounce> {
		@Override
		public void write(Json json, Interpolation.Bounce object, Class knownType) {
			InterpolationSerializer.write(json, object);
		}

		@Override
		public Interpolation.Bounce read(Json json, JsonValue jsonData, Class type) {
			Interpolation.Bounce interp = new Interpolation.Bounce(2);
			json.readFields(interp, jsonData);
			return interp;
		}
	}

	public static class BounceInSerializer implements Json.Serializer<Interpolation.BounceIn> {
		@Override
		public void write(Json json, Interpolation.BounceIn object, Class knownType) {
			InterpolationSerializer.write(json, object);
		}

		@Override
		public Interpolation.BounceIn read(Json json, JsonValue jsonData, Class type) {
			Interpolation.BounceIn interp = new Interpolation.BounceIn(2);
			json.readFields(interp, jsonData);
			return interp;
		}
	}

	public static class BounceOutSerializer implements Json.Serializer<Interpolation.BounceOut> {
		@Override
		public void write(Json json, Interpolation.BounceOut object, Class knownType) {
			InterpolationSerializer.write(json, object);
		}

		@Override
		public Interpolation.BounceOut read(Json json, JsonValue jsonData, Class type) {
			Interpolation.BounceOut interp = new Interpolation.BounceOut(2);
			json.readFields(interp, jsonData);
			return interp;
		}
	}

	public static class SwingSerializer implements Json.Serializer<Interpolation.Swing> {
		@Override
		public void write(Json json, Interpolation.Swing object, Class knownType) {
			InterpolationSerializer.write(json, object);
		}

		@Override
		public Interpolation.Swing read(Json json, JsonValue jsonData, Class type) {
			Interpolation.Swing interp = new Interpolation.Swing(1);
			json.readFields(interp, jsonData);
			return interp;
		}
	}

	public static class SwingInSerializer implements Json.Serializer<Interpolation.SwingIn> {
		@Override
		public void write(Json json, Interpolation.SwingIn object, Class knownType) {
			InterpolationSerializer.write(json, object);
		}

		@Override
		public Interpolation.SwingIn read(Json json, JsonValue jsonData, Class type) {
			Interpolation.SwingIn interp = new Interpolation.SwingIn(1);
			json.readFields(interp, jsonData);
			return interp;
		}
	}

	public static class SwingOutSerializer implements Json.Serializer<Interpolation.SwingOut> {
		@Override
		public void write(Json json, Interpolation.SwingOut object, Class knownType) {
			InterpolationSerializer.write(json, object);
		}

		@Override
		public Interpolation.SwingOut read(Json json, JsonValue jsonData, Class type) {
			Interpolation.SwingOut interp = new Interpolation.SwingOut(1);
			json.readFields(interp, jsonData);
			return interp;
		}
	}
}
