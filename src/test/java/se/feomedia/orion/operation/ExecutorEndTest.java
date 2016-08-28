package se.feomedia.orion.operation;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.graphics.Cursor;
import com.sun.org.apache.xerces.internal.util.ShadowedSymbolTable;
import org.junit.Test;
import se.feomedia.orion.Executor;
import se.feomedia.orion.Operation;
import se.feomedia.orion.OperationTree;
import se.feomedia.orion.system.OperationSystem;

import static org.junit.Assert.assertEquals;
import static se.feomedia.orion.OperationFactory.*;

public class ExecutorEndTest {
    @Test
    public void end_test() {
        World world = new World(new WorldConfiguration()
                .setSystem(new OperationSystem()));

        FiniteOperation fo = operation(FiniteOperation.class);

        fo.register(world, world.create());

        world.process();

        assertEquals(1, fo.n);

        world.process();

        assertEquals(2, fo.n);

        world.process();

        assertEquals(-1, fo.n);

        world.process();
    }

    public static class FiniteOperation extends Operation {
        public int n;


        @Override
        public Class<? extends Executor> executorType() {
            return FiniteExecutor.class;
        }

        @Override
        protected boolean isComplete() {
            return n > 2;
        }

        @Override
        public void reset() {
        }


        @Wire
        public static class FiniteExecutor extends Executor<FiniteOperation> {


            @Override
            protected float act(float delta, FiniteOperation operation, OperationTree node) {
                operation.n++;
                return 0;
            }

            @Override
            protected void begin(FiniteOperation op, OperationTree node) {
                op.n = 0;
            }


            protected void end(FiniteOperation operation, OperationTree node) {
                operation.n = -1;
            }


        }
    }

}