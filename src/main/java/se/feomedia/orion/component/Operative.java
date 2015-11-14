package se.feomedia.orion.component;

import com.artemis.PooledComponent;
import com.badlogic.gdx.utils.Array;
import se.feomedia.orion.OperationTree;

public class Operative extends PooledComponent {
	public Array<OperationTree> operations = new Array<>(true, 8);

	@Override
	protected void reset() {
		for (int i = 0, s = operations.size; s > i ; i++) {
			operations.get(i).clear();
		}

		operations.clear();
	}

	@Override
	public String toString() {
		return "Operative{" +
			"operations=" + operations +
			'}';
	}
}
