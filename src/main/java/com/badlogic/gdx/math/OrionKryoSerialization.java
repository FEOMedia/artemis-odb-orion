package com.badlogic.gdx.math;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import se.feomedia.orion.OperationTree;
import se.feomedia.orion.RepeatOperation;
import se.feomedia.orion.kryo.KryoOperationTreeSerializer;
import se.feomedia.orion.kryo.OperationInstantiator;
import se.feomedia.orion.operation.*;

import java.lang.reflect.Field;

import static java.lang.Math.abs;

public class OrionKryoSerialization {
	public static void configure(Kryo kryo) {
		kryo.setInstantiatorStrategy(new OperationInstantiator());
		kryo.register(OperationTree.class, new KryoOperationTreeSerializer());
		kryo.register(DelayOperation.class);
		kryo.register(DelayOperation.DelayExecutor.class);
		kryo.register(DelayTickOperation.class);
		kryo.register(DelayTickOperation.DelayTickExecutor.class);
		kryo.register(IfElseOperation.class);
		kryo.register(IfElseOperation.IfElseExecutor.class);
		kryo.register(KillOperation.class);
		kryo.register(KillOperation.KillExecutor.class);
		kryo.register(ParallelOperation.class);
		kryo.register(ParallelOperation.ParallelExecutor.class);
		kryo.register(RepeatOperation.class);
		kryo.register(RepeatOperation.RepeatExecutor.class);
		kryo.register(SequenceOperation.class);
		kryo.register(SequenceOperation.SequenceExecutor.class);

		kryo.register(Interpolation.Pow.class, new PowSerializer());
		kryo.register(Interpolation.PowIn.class, new PowInSerializer());
		kryo.register(Interpolation.PowOut.class, new PowOutSerializer());
		kryo.register(Interpolation.Exp.class, new ExpSerializer());
		kryo.register(Interpolation.ExpIn.class, new ExpInSerializer());
		kryo.register(Interpolation.ExpOut.class, new ExpOutSerializer());
		kryo.register(Interpolation.Elastic.class, new ElasticSerializer());
		kryo.register(Interpolation.ElasticIn.class, new ElasticInSerializer());
		kryo.register(Interpolation.ElasticOut.class, new ElasticOutSerializer());
		kryo.register(Interpolation.Swing.class, new SwingSerializer());
		kryo.register(Interpolation.SwingIn.class, new SwingInSerializer());
		kryo.register(Interpolation.SwingOut.class, new SwingOutSerializer());
		kryo.register(Interpolation.Bounce.class, new BounceSerializer());
		kryo.register(Interpolation.BounceIn.class, new BounceInSerializer());
		kryo.register(Interpolation.BounceOut.class, new BounceOutSerializer());
	}

	static class PowSerializer extends Serializer<Interpolation.Pow> {
		@Override
		public void write(Kryo kryo, Output output, Interpolation.Pow object) {
			output.writeInt(object.power);
		}

		@Override
		public Interpolation.Pow read(Kryo kryo, Input input, Class<Interpolation.Pow> type) {
			return new Interpolation.Pow(input.readInt());
		}

		@Override
		public Interpolation.Pow copy(Kryo kryo, Interpolation.Pow original) {
			return new Interpolation.Pow(original.power);
		}
	}

	static class PowInSerializer extends Serializer<Interpolation.PowIn> {
		@Override
		public void write(Kryo kryo, Output output, Interpolation.PowIn object) {
			output.writeInt(object.power);
		}

		@Override
		public Interpolation.PowIn read(Kryo kryo, Input input, Class<Interpolation.PowIn> type) {
			return new Interpolation.PowIn(input.readInt());
		}

		@Override
		public Interpolation.PowIn copy(Kryo kryo, Interpolation.PowIn original) {
			return new Interpolation.PowIn(original.power);
		}
	}



	static class PowOutSerializer extends Serializer<Interpolation.PowOut> {
		@Override
		public void write(Kryo kryo, Output output, Interpolation.PowOut object) {
			output.writeInt(object.power);
		}

		@Override
		public Interpolation.PowOut read(Kryo kryo, Input input, Class<Interpolation.PowOut> type) {
			return new Interpolation.PowOut(input.readInt());
		}

		@Override
		public Interpolation.PowOut copy(Kryo kryo, Interpolation.PowOut original) {
			return new Interpolation.PowOut(original.power);
		}
	}

	static class ExpSerializer extends Serializer<Interpolation.Exp> {
		@Override
		public void write(Kryo kryo, Output output, Interpolation.Exp object) {
			output.writeFloat(object.value);
			output.writeFloat(object.power);
		}

		@Override
		public Interpolation.Exp read(Kryo kryo, Input input, Class<Interpolation.Exp> type) {
			return new Interpolation.Exp(input.readFloat(), input.readFloat());
		}

		@Override
		public Interpolation.Exp copy(Kryo kryo, Interpolation.Exp original) {
			return new Interpolation.Exp(original.value, original.power);
		}
	}


	static class ExpInSerializer extends Serializer<Interpolation.ExpIn> {
		@Override
		public void write(Kryo kryo, Output output, Interpolation.ExpIn object) {
			output.writeFloat(object.value);
			output.writeFloat(object.power);
		}

		@Override
		public Interpolation.ExpIn read(Kryo kryo, Input input, Class<Interpolation.ExpIn> type) {
			return new Interpolation.ExpIn(input.readFloat(), input.readFloat());
		}

		@Override
		public Interpolation.ExpIn copy(Kryo kryo, Interpolation.ExpIn original) {
			return new Interpolation.ExpIn(original.value, original.power);
		}
	}

	static class ExpOutSerializer extends Serializer<Interpolation.ExpOut> {
		@Override
		public void write(Kryo kryo, Output output, Interpolation.ExpOut object) {
			output.writeFloat(object.value);
			output.writeFloat(object.power);
		}

		@Override
		public Interpolation.ExpOut read(Kryo kryo, Input input, Class<Interpolation.ExpOut> type) {
			return new Interpolation.ExpOut(input.readFloat(), input.readFloat());
		}

		@Override
		public Interpolation.ExpOut copy(Kryo kryo, Interpolation.ExpOut original) {
			return new Interpolation.ExpOut(original.value, original.power);
		}
	}

	static class ElasticSerializer extends Serializer<Interpolation.Elastic> {
		@Override
		public void write(Kryo kryo, Output output, Interpolation.Elastic object) {
			output.writeFloat(object.value);
			output.writeFloat(object.power);
			output.writeFloat(abs(object.scale / MathUtils.PI));
			output.writeFloat(object.bounces);
		}

		@Override
		public Interpolation.Elastic read(Kryo kryo, Input input, Class<Interpolation.Elastic> type) {
			return new Interpolation.Elastic(
				input.readFloat(),
				input.readFloat(),
				MathUtils.round(input.readFloat()),
				input.readFloat());
		}

		@Override
		public Interpolation.Elastic copy(Kryo kryo, Interpolation.Elastic original) {
			return new Interpolation.Elastic(
				original.value,
				original.power,
				MathUtils.round(abs(original.bounces / MathUtils.PI)),
				original.scale);
		}
	}

	static class ElasticInSerializer extends Serializer<Interpolation.ElasticIn> {
		@Override
		public void write(Kryo kryo, Output output, Interpolation.ElasticIn object) {
			output.writeFloat(object.value);
			output.writeFloat(object.power);
			output.writeFloat(abs(object.scale / MathUtils.PI));
			output.writeFloat(object.bounces);
		}

		@Override
		public Interpolation.ElasticIn read(Kryo kryo, Input input, Class<Interpolation.ElasticIn> type) {
			return new Interpolation.ElasticIn(
				input.readFloat(),
				input.readFloat(),
				MathUtils.round(input.readFloat()),
				input.readFloat());
		}

		@Override
		public Interpolation.ElasticIn copy(Kryo kryo, Interpolation.ElasticIn original) {
			return new Interpolation.ElasticIn(
				original.value,
				original.power,
				MathUtils.round(abs(original.bounces / MathUtils.PI)),
				original.scale);
		}
	}

	static class ElasticOutSerializer extends Serializer<Interpolation.ElasticOut> {
		@Override
		public void write(Kryo kryo, Output output, Interpolation.ElasticOut object) {
			output.writeFloat(object.value);
			output.writeFloat(object.power);
			output.writeFloat(abs(object.scale / MathUtils.PI));
			output.writeFloat(object.bounces);
		}

		@Override
		public Interpolation.ElasticOut read(Kryo kryo, Input input, Class<Interpolation.ElasticOut> type) {
			return new Interpolation.ElasticOut(
				input.readFloat(),
				input.readFloat(),
				MathUtils.round(input.readFloat()),
				input.readFloat());
		}

		@Override
		public Interpolation.ElasticOut copy(Kryo kryo, Interpolation.ElasticOut original) {
			return new Interpolation.ElasticOut(
				original.value,
				original.power,
				MathUtils.round(abs(original.bounces / MathUtils.PI)),
				original.scale);
		}
	}

	static class SwingSerializer extends Serializer<Interpolation.Swing> {
		private final Field field;

		public SwingSerializer() {
			try {
				field = Interpolation.Swing.class.getDeclaredField("scale");
				field.setAccessible(true);
			} catch (NoSuchFieldException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void write(Kryo kryo, Output output, Interpolation.Swing object) {
			try {
				output.writeFloat(field.getFloat(object));
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public Interpolation.Swing read(Kryo kryo, Input input, Class<Interpolation.Swing> type) {
			return new Interpolation.Swing(input.readFloat());
		}

		@Override
		public Interpolation.Swing copy(Kryo kryo, Interpolation.Swing original) {
			try {
				return new Interpolation.Swing(field.getFloat(original));
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}

	static class SwingInSerializer extends Serializer<Interpolation.SwingIn> {
		private final Field field;

		public SwingInSerializer() {
			try {
				field = Interpolation.SwingIn.class.getDeclaredField("scale");
				field.setAccessible(true);
			} catch (NoSuchFieldException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void write(Kryo kryo, Output output, Interpolation.SwingIn object) {
			try {
				output.writeFloat(field.getFloat(object));
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public Interpolation.SwingIn read(Kryo kryo, Input input, Class<Interpolation.SwingIn> type) {
			return new Interpolation.SwingIn(input.readFloat());
		}

		@Override
		public Interpolation.SwingIn copy(Kryo kryo, Interpolation.SwingIn original) {
			try {
				return new Interpolation.SwingIn(field.getFloat(original));
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}

	static class SwingOutSerializer extends Serializer<Interpolation.SwingOut> {
		private final Field field;

		public SwingOutSerializer() {
			try {
				field = Interpolation.SwingOut.class.getDeclaredField("scale");
				field.setAccessible(true);
			} catch (NoSuchFieldException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void write(Kryo kryo, Output output, Interpolation.SwingOut object) {
			try {
				output.writeFloat(field.getFloat(object));
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public Interpolation.SwingOut read(Kryo kryo, Input input, Class<Interpolation.SwingOut> type) {
			return new Interpolation.SwingOut(input.readFloat());
		}

		@Override
		public Interpolation.SwingOut copy(Kryo kryo, Interpolation.SwingOut original) {
			try {
				return new Interpolation.SwingOut(field.getFloat(original));
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}

	static class BounceSerializer extends Serializer<Interpolation.Bounce> {
		private final Field fieldW;
		private final Field fieldH;

		public BounceSerializer() {
			try {
				fieldW = Interpolation.BounceOut.class.getDeclaredField("widths");
				fieldW.setAccessible(true);
				fieldH = Interpolation.BounceOut.class.getDeclaredField("heights");
				fieldH.setAccessible(true);
			} catch (NoSuchFieldException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void write(Kryo kryo, Output output, Interpolation.Bounce object) {
			try {
				float[] widths = (float[]) fieldW.get(object);
				float[] heights = (float[]) fieldH.get(object);
				byte widthLength = (byte) widths.length;
				byte heightLength = (byte) heights.length;

				output.writeByte(widthLength);
				output.writeByte(heightLength);
				output.writeFloats(widths);
				output.writeFloats(heights);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public Interpolation.Bounce read(Kryo kryo, Input input, Class<Interpolation.Bounce> type) {
			int widths = input.readByte();
			int heights = input.readByte();
			return new Interpolation.Bounce(input.readFloats(widths), input.readFloats(heights));
		}

		@Override
		public Interpolation.Bounce copy(Kryo kryo, Interpolation.Bounce original) {
			try {
				float[] widths = (float[]) fieldW.get(original);
				return new Interpolation.Bounce(widths.length);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}

	static class BounceInSerializer extends Serializer<Interpolation.BounceIn> {
		private final Field fieldW;
		private final Field fieldH;

		public BounceInSerializer() {
			try {
				fieldW = Interpolation.BounceOut.class.getDeclaredField("widths");
				fieldW.setAccessible(true);
				fieldH = Interpolation.BounceOut.class.getDeclaredField("heights");
				fieldH.setAccessible(true);
			} catch (NoSuchFieldException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void write(Kryo kryo, Output output, Interpolation.BounceIn object) {
			try {
				float[] widths = (float[]) fieldW.get(object);
				float[] heights = (float[]) fieldH.get(object);
				byte widthLength = (byte) widths.length;
				byte heightLength = (byte) heights.length;

				output.writeByte(widthLength);
				output.writeByte(heightLength);
				output.writeFloats(widths);
				output.writeFloats(heights);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public Interpolation.BounceIn read(Kryo kryo, Input input, Class<Interpolation.BounceIn> type) {
			int widths = input.readByte();
			int heights = input.readByte();
			return new Interpolation.BounceIn(input.readFloats(widths), input.readFloats(heights));
		}

		@Override
		public Interpolation.BounceIn copy(Kryo kryo, Interpolation.BounceIn original) {
			try {
				float[] widths = (float[]) fieldW.get(original);
				return new Interpolation.BounceIn(widths.length);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}

	static class BounceOutSerializer extends Serializer<Interpolation.BounceOut> {
		private final Field fieldW;
		private final Field fieldH;

		public BounceOutSerializer() {
			try {
				fieldW = Interpolation.BounceOut.class.getDeclaredField("widths");
				fieldW.setAccessible(true);
				fieldH = Interpolation.BounceOut.class.getDeclaredField("heights");
				fieldH.setAccessible(true);
			} catch (NoSuchFieldException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void write(Kryo kryo, Output output, Interpolation.BounceOut object) {
			try {
				float[] widths = (float[]) fieldW.get(object);
				float[] heights = (float[]) fieldH.get(object);
				byte widthLength = (byte) widths.length;
				byte heightLength = (byte) heights.length;

				output.writeByte(widthLength);
				output.writeByte(heightLength);
				output.writeFloats(widths);
				output.writeFloats(heights);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public Interpolation.BounceOut read(Kryo kryo, Input input, Class<Interpolation.BounceOut> type) {
			int widths = input.readByte();
			int heights = input.readByte();
			return new Interpolation.BounceOut(input.readFloats(widths), input.readFloats(heights));
		}

		@Override
		public Interpolation.BounceOut copy(Kryo kryo, Interpolation.BounceOut original) {
			try {
				float[] widths = (float[]) fieldW.get(original);
				return new Interpolation.BounceOut(widths.length);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
