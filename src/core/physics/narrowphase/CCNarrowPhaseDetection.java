package physics.narrowphase;

import physics.shapes.Circle2d;
import physics.space.Vector2d;

public class CCNarrowPhaseDetection implements NarrowPhaseDetection {

    protected final Circle2d[] circles;

    public CCNarrowPhaseDetection(Circle2d[] circles){
        this.circles = circles;
    }

    @Override
    public Vector2d check() {

        Vector2d ab = new Vector2d(circles[0].getCenter(), circles[1].getCenter());
        float minDist = circles[0].getRadius() + circles[1].getRadius();

        float penetration = minDist - ab.getMagnitude();

        if (penetration > 1){
            ab.multiply(penetration / minDist);
        } else {
            ab.multiply(0);
        }

        return ab;

    }

    @Override
    public Vector2d[] generateContacts(Vector2d collisionNormal, float penetration) {

        Vector2d[] contacts = new Vector2d[2];

        contacts[0] = Vector2d.add(circles[0].getCenter(), collisionNormal.multiply(circles[0].getRadius()));
        contacts[1] = Vector2d.add(circles[1].getCenter(), collisionNormal.multiply(-circles[1].getRadius()));

        return contacts;

    }

}
