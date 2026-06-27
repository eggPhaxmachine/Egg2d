package physics.narrowphase;

import physics.shapes.Polygon2d;
import physics.space.Vector2d;

public class PPTest implements NarrowPhaseDetection{

    protected final Polygon2d[] polygons;

    private PPSATNarrowPhase narrow1;
    private PPGJKNarrowPhase narrow2;

    public PPTest(Polygon2d[] polygons){
        this.polygons = polygons;

        narrow1 = new PPSATNarrowPhase(polygons);
        narrow2 = new PPGJKNarrowPhase(polygons);
    }

    @Override
    public Vector2d check() {
        Vector2d result1 = narrow1.check();
        Vector2d result2 = narrow2.check();

        if (!result1.equals(result2, 0.5f)){
            return result1;
        }

        return result1;

    }

    @Override
    public Vector2d[] generateContacts(Vector2d collisionNormal, float penetration) {
        return narrow1.generateContacts(collisionNormal, penetration);
    }
}
