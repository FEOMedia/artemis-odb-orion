package se.feomedia.orion;

import se.feomedia.orion.io.OperationTreeSerializer;
import se.feomedia.orion.kryo.KryoOperationTreeSerializer;

public final class InternalUtil {
	public InternalUtil(KryoOperationTreeSerializer.Friend friend) {
		friend.hashCode();
	}

	public InternalUtil(OperationTreeSerializer.Friend friend) {
		friend.hashCode();
	}

	public Operation getOperation(OperationTree tree) {
		return tree.operation;
	}

	public OperationTree toNode(Operation operation) {
		return operation.toNode();
	}
}
