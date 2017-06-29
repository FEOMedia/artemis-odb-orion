package se.feomedia.orion.component;

import com.artemis.PooledComponent;
import com.artemis.annotations.DelayedComponentRemoval;
import com.badlogic.gdx.utils.Array;
import se.feomedia.orion.ForkOperation;

@DelayedComponentRemoval
public class Forked extends PooledComponent {
	public transient Array<ForkOperation> owners = new Array<>(false, 5);

	@Override
	protected void reset() {
		for (int i = 0, s = owners.size; s > i ; i++) {
			ForkOperation op = owners.get(i);
			if (op != null) {
				op.markCompleted();
			}
		}

		owners.clear();
	}

	@Override
	public String toString() {
		return "Forked{" +
			"owners=" + owners.size +
			'}';
	}
}
