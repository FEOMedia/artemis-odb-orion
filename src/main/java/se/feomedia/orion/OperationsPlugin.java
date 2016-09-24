package se.feomedia.orion;

import com.artemis.ArtemisPlugin;
import com.artemis.WorldConfigurationBuilder;
import se.feomedia.orion.system.OperationSystem;

/**
 * Scheduled operations on entities and components.
 * @see OperationSystem
 * @author Adrian Papari
 * @author Daan van Yperen (Integration)
 */
public class OperationsPlugin implements ArtemisPlugin {

    @Override
    public void setup(WorldConfigurationBuilder b) {
        b.dependsOn(WorldConfigurationBuilder.Priority.OPERATIONS, OperationSystem.class);
    }
}
