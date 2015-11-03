package se.feomedia.orion.component;

import com.artemis.PooledComponent;
import com.badlogic.gdx.utils.Array;
import se.feomedia.orion.OperationTree;

public class Operative extends PooledComponent {
	public Array<OperationTree> operations = new Array<>(true, 16, OperationTree.class);

	@Override
	protected void reset() {
		OperationTree[] ops = operations.items;
		for (int i = 0, s = operations.size; s > i ; i++) {
			ops[i].clear();
		}

		operations.clear();
	}
}
