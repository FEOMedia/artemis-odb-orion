package se.feomedia.orion;

import com.artemis.World;
import com.artemis.utils.IntBag;
import se.feomedia.orion.component.Operative;

import static com.artemis.Aspect.all;

public final class OperationTestUtil {
	private OperationTestUtil() {}

	public static IntBag operatives(World world) {
		return world.getAspectSubscriptionManager()
			.get(all(Operative.class))
			.getEntities();
	}

	public static void process(World world) {
		world.delta = 1f / 60f;
		world.process();
	}
}
