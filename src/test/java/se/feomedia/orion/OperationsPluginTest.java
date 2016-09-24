package se.feomedia.orion;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import org.junit.Assert;
import org.junit.Test;
import se.feomedia.orion.system.OperationSystem;

import static org.junit.Assert.*;

/**
 * @author Daan van Yperen
 */
public class OperationsPluginTest {

    @Test
    public void When_registering_operation_plugin_Should_register_operationsystem() {
        World world = new World(new WorldConfigurationBuilder()
                .dependsOn(OperationsPlugin.class)
                .build());
        Assert.assertNotNull(world.getSystem(OperationSystem.class));
        world.process();
    }

}