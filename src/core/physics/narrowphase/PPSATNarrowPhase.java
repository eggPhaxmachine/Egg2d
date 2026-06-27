package physics.narrowphase;

import physics.shapes.Polygon2d;
import physics.space.Vector2d;

public class PPSATNarrowPhase implements NarrowPhaseDetection {

    protected final Polygon2d[] polygons;

    public PPSATNarrowPhase(Polygon2d[] polygons){
        this.polygons = polygons;
    }

    @Override
    public Vector2d check() {

        float minOverlap = Float.POSITIVE_INFINITY;
        Vector2d minNormal = null;

        Vector2d[] vertices1 = polygons[0].getVertices();
        Vector2d[] vertices2 = polygons[1].getVertices();

        for (int i = 0; i < vertices1.length; i++){

            Vector2d normal = new Vector2d(vertices1[i], vertices1[(i + 1) % vertices1.length]).perpendicular().normalize();

            Projection p1 = project(vertices1, normal);
            Projection p2 = project(vertices2, normal);

            float temp1 = p1.products[0] - p2.products[1];
            float temp2 = p2.products[0] - p1.products[1];

            float overlap;
            if (temp1 < temp2){
                overlap = temp1;
            } else {
                overlap = temp2;
                normal.multiply(-1);
            }

            if (overlap < 1) {
                return new Vector2d();
            } else if (overlap < minOverlap){
                minOverlap = overlap;
                minNormal = normal;
            }

        }

        for (int i = 0; i < vertices2.length; i++){

            Vector2d normal = new Vector2d(vertices2[i], vertices2[(i + 1) % vertices2.length]).perpendicular().normalize();

            Projection p1 = project(vertices2, normal);
            Projection p2 = project(vertices1, normal);

            float temp1 = p1.products[0] - p2.products[1];
            float temp2 = p2.products[0] - p1.products[1];

            float overlap;
            if (temp1 < temp2){
                overlap = temp1;
                normal.multiply(-1);
            } else {
                overlap = temp2;
            }

            if (overlap < 1) {
                return new Vector2d();
            } else if (overlap < minOverlap){
                minOverlap = overlap;
                minNormal = normal;
            }

        }

        polygons[0].flag = true;
        polygons[1].flag = true;
        return minNormal.multiply(minOverlap);

    }
    private Projection project(Vector2d[] vertices, Vector2d normal){

        Projection projection = new Projection();

        projection.products[0] = vertices[0].dot(normal);
        projection.products[1] = projection.products[0];
        projection.indices[0] = 0;
        projection.indices[1] = 0;
        for (int i = 1; i < vertices.length; i++) {

            float temp = vertices[i].dot(normal);

            if (temp > projection.products[0]) {
                projection.products[0] = temp;
                projection.indices[0] = i;
            } else if (temp < projection.products[1]) {
                projection.products[1] = temp;
                projection.indices[1] = i;
            }

        }

        return  projection;

    }

    @Override
    public Vector2d[] generateContacts(Vector2d collisionNormal, float penetration) {

        if (false) {
            return new Vector2d[0];
        }

        Vector2d[] edge1;
        Vector2d[] edge2;


        float edgeDotProduct1;
        float edgeDotProduct2;
        float temp;

        Vector2d normal = collisionNormal.copy();
        Vector2d[] vertices = polygons[0].getVertices();

        int furthestID = 0;
        double max = Vector2d.dot(vertices[0], normal);
        for (int i = 1; i < vertices.length; i++){
            temp = Vector2d.dot(vertices[i], normal);
            if (max < temp){
                max = temp;
                furthestID = i;
            }
        }

        edgeDotProduct1 = Vector2d.subtract(vertices[furthestID], vertices[Math.floorMod(furthestID - 1, vertices.length)]).normalize().dot(normal);
        temp = Vector2d.subtract(vertices[furthestID], vertices[(furthestID + 1) % vertices.length]).normalize().dot(normal);
        if (edgeDotProduct1 <= temp){
            edge1 = new Vector2d[]{vertices[Math.floorMod(furthestID - 1, vertices.length)], vertices[furthestID], vertices[furthestID]};
        } else {
            edge1 = new Vector2d[]{vertices[furthestID], vertices[(furthestID + 1) % vertices.length], vertices[furthestID]};
            edgeDotProduct1 = temp;
        }


        normal.multiply(-1);
        vertices = polygons[1].getVertices();

        furthestID = 0;
        max = Vector2d.dot(vertices[0], normal);
        for (int i = 1; i < vertices.length; i++){
            temp = Vector2d.dot(vertices[i], normal);
            if (max < temp){
                max = temp;
                furthestID = i;
            }
        }

        edgeDotProduct2 = Vector2d.subtract(vertices[furthestID], vertices[Math.floorMod(furthestID - 1, vertices.length)]).normalize().dot(normal);
        temp = Vector2d.subtract(vertices[furthestID], vertices[(furthestID + 1) % vertices.length]).normalize().dot(normal);
        if (edgeDotProduct2 <= temp){
            edge2 = new Vector2d[]{vertices[Math.floorMod(furthestID - 1, vertices.length)], vertices[furthestID], vertices[furthestID]};
        } else {
            edge2 = new Vector2d[]{vertices[furthestID], vertices[(furthestID + 1) % vertices.length], vertices[furthestID]};
            edgeDotProduct2 = temp;
        }


        boolean flipped = false;
        if (Math.abs(edgeDotProduct1) > Math.abs(edgeDotProduct2)) {
            Vector2d[] swap = edge1;
            edge1 = edge2;
            edge2 = swap;
            flipped = true;
        }

        Vector2d refv = edge1[1].copy().subtract(edge1[0]);

        edge2 = clip(edge2[0], edge2[1], refv, refv.dot(edge1[0]));
        if (edge2[1] == null) {
            return new Vector2d[]{};
        }

        temp = refv.dot(edge1[1]);
        edge2 = clip(edge2[0], edge2[1], refv.multiply(-1), -temp);

        refv.perpendicular();

        if (!flipped) refv.multiply(-1);

        temp = refv.dot(edge1[2]);

        if (refv.dot(edge2[0]) - temp < 0.0){
            return new Vector2d[]{edge2[1]};
        }
        if (refv.dot(edge2[1]) - temp < 0.0){
            return new Vector2d[]{edge2[0]};
        }

        return edge2;

    }
    private Vector2d[] clip(Vector2d v1, Vector2d v2, Vector2d d, float o){

        int size = 0;
        Vector2d[] edge = new Vector2d[2];

        float d1 = d.dot(v1) - o;
        float d2 = d.dot(v2) - o;

        if (d1 >= 0) {
            edge[size] = v1;
            size++;
        }
        if (d2 >= 0) {
            edge[size] = v2;
            size++;
        }

        if (d1 * d2 < 0){
            edge[size] = v2.copy().subtract(v1).multiply(d1 / (d1 - d2)).add(v1);
        }

        return edge;

    }

    private static class Projection{

        float[] products = new float[2];
        int[] indices = new int[2];

    }
}
