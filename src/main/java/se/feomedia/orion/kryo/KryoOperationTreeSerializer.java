package se.feomedia.orion.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import se.feomedia.orion.InternalUtil;
import se.feomedia.orion.Operation;
import se.feomedia.orion.OperationTree;

public class KryoOperationTreeSerializer extends Serializer<OperationTree> {
	private final InternalUtil util = new InternalUtil(new Friend());

	@Override
	public void write(Kryo kryo, Output output, OperationTree node) {
		kryo.writeClassAndObject(output, util.getOperation(node));
	}

	@Override
	public OperationTree read(Kryo kryo, Input input, Class<OperationTree> type) {
		return util.toNode((Operation) kryo.readClassAndObject(input));
	}

	public static final class Friend {
		private Friend() {}
	}
}
