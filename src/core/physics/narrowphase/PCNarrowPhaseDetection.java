package core.physics.narrowphase;

import core.physics.shapes.Circle2d;
import core.physics.shapes.Polygon2d;
import core.physics.space.Vector2d;

public class PCNarrowPhaseDetection implements NarrowPhaseDetection {

    protected final Polygon2d polygon;
    protected final Circle2d circle;


    public PCNarrowPhaseDetection(Polygon2d polygon, Circle2d circle) {
        this.polygon = polygon;
        this.circle = circle;
    }


    @Override
    public Vector2d check() {

        boolean outside = false;
        Vector2d minPenetration = null;
        float minMagnitude = Float.POSITIVE_INFINITY;

        Vector2d[] curEdge = new Vector2d[2];
        Vector2d[] vertices = polygon.getVertices();
        Vector2d center = circle.getCenter();
        float radius = circle.getRadius();
        for (int i = 0; i < vertices.length; i++){
            curEdge[0] = vertices[i];
            curEdge[1] = vertices[(i + 1) % vertices.length];

            Vector2d ab = new Vector2d(curEdge[0], curEdge[1]);
            Vector2d ac = new Vector2d(curEdge[0], center);

            if (Vector2d.sameDirection(ab.perpendicular(), ac)){
                outside = true;
            }
            ab.perpendicular(true);

            float scalar = Math.clamp(ab.dot(ac) / ab.dot(ab), 0, 1);
            ab.multiply(scalar).add(curEdge[0]).subtract(center);

            float magnitude = ab.getMagnitude();

            if (magnitude < minMagnitude) {
                minPenetration = ab;
                minMagnitude = magnitude;
            }

        }

        if (outside) {
            if (minMagnitude < radius){
                minPenetration.multiply(1 - (radius));
                if (minPenetration.getMagnitude() < 1) minPenetration.multiply(0);
            } else {
                minPenetration.multiply(0);
            }
        } else {
            minPenetration.multiply(-(1 + radius / minMagnitude));
        }

        return minPenetration;
    }
}
