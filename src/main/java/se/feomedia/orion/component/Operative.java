package se.feomedia.orion.component;

import com.artemis.PooledComponent;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import se.feomedia.orion.OperationTree;

public class Operative extends PooledComponent {
	public Array<OperationTree> operations = new Array<>(true, 16, OperationTree.class);

	@Override
	protected void reset() {
		Pools.freeAll(operations);
		operations.clear();
	}
}
