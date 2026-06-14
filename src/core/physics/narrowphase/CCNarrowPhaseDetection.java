package core.physics.narrowphase;

import core.physics.shapes.Circle2d;
import core.physics.space.Vector2d;

public class CCNarrowPhaseDetection implements NarrowPhaseDetection {

    protected final Circle2d[] objects;

    public CCNarrowPhaseDetection(Circle2d[] objects){
        this.objects = objects;
    }

    @Override
    public Vector2d check() {

        Vector2d ab = new Vector2d(objects[0].getCenter(), objects[1].getCenter());
        float minDist = objects[0].getRadius() + objects[1].getRadius();

        float penetration = minDist - ab.getMagnitude();

        if (penetration > 1){
            ab.multiply(penetration / minDist);
        } else {
            ab.multiply(0);
        }

        return ab;

    }

}
