package physics.narrowphase;

import physics.shapes.Polygon2d;
import physics.space.Vector2d;
import util.MinHeap;

import java.util.ArrayList;

public class PPGJKNarrowPhase implements NarrowPhaseDetection {

    protected final Polygon2d[] polygons;

    public PPGJKNarrowPhase(Polygon2d[] polygons){
        this.polygons = polygons;
    }

    @Override
    public Vector2d check(){

        Simplex simplex = new Simplex();

        simplex.d = new Vector2d(1, 0);
        simplex.addPoint();

        simplex.d = Vector2d.multiply(simplex.getPoint(0), -1);

        while (true) {

            simplex.addPoint();

            if (!Vector2d.sameDirection(simplex.d, simplex.getPoint(simplex.getNumVertices() - 1))) {
                return new Vector2d();
            } else if (checkSimplex(simplex)) {
                return EPAlgorithm(simplex);
            }

        }

    }

    private Vector2d EPAlgorithm(Simplex simplex){

        MinHeap<EPAFeature> features = new MinHeap<>(polygons[0].getNumVertices() + polygons[1].getNumVertices());
        Vector2d center = new Vector2d();

        for (int i = 0; i < 3; i++){
            center.add(simplex.getPoint(i));
            EPAFeature feature = new EPAFeature(simplex.getPoint(i), simplex.getPoint((i + 1) % 3));
            features.add(feature, feature.penetration);
        }

        int numVertices = 3;

        while (true) {

            EPAFeature curFeature = features.popRoot();
            Vector2d searchVector = curFeature.normal;

            if (curFeature.penetration < 1) {

                searchVector = new Vector2d(curFeature.p1, curFeature.p2).perpendicular();

                if (Vector2d.sameDirection(searchVector, center.multiply(1.0f/numVertices))){
                    searchVector.multiply(-1);
                }
                center.multiply(numVertices);

                curFeature.normal = new Vector2d();
                curFeature.penetration = 0;

            }

            Vector2d searchResult = new Vector2d((polygons[1].GJKSupportFunction(Vector2d.multiply(searchVector, -1))), polygons[0].GJKSupportFunction(searchVector));

            if (searchResult.equals(curFeature.p1) || searchResult.equals(curFeature.p2)){
                return curFeature.normal.multiply(curFeature.penetration);
            }

            center.add(searchResult);
            numVertices++;

            EPAFeature tempFeature = new EPAFeature(curFeature.p1, searchResult);
            features.add(tempFeature, tempFeature.penetration);

            tempFeature = new EPAFeature(searchResult, curFeature.p2);
            features.add(tempFeature, tempFeature.penetration);

        }

    }

    private boolean checkSimplex(Simplex simplex) {
        return switch (simplex.getNumVertices()) {
            case (2) -> checkLine(simplex);
            case (3) -> checkTriangle(simplex);
            default -> throw new UnsupportedOperationException();
        };
    }
    private boolean checkLine(Simplex simplex) {

        Vector2d ab = new Vector2d(simplex.getPoint(1), simplex.getPoint(0));
        Vector2d ao = Vector2d.multiply(simplex.getPoint(1), -1);

        if (Vector2d.sameDirection(ao, ab)) {

            Vector2d temp = new Vector2d(ab.getY(), -ab.getX());
            if (!Vector2d.sameDirection(ao, temp)) {
                temp.multiply(-1);
            }
            simplex.d = temp;

        } else {
            simplex.removePoint(0);
            simplex.d = ao;
        }

        return false;

    }
    private boolean checkTriangle(Simplex simplex) {

        Vector2d ao = Vector2d.multiply(simplex.getPoint(2), -1);

        Vector2d ab = new Vector2d(simplex.getPoint(2), simplex.getPoint(1));
        Vector2d abp = new Vector2d(ab.getY() * -1, ab.getX());
        if (Vector2d.sameDirection(simplex.getPoint(0), abp)) {
            abp.multiply(-1);
        }

        Vector2d ac = new Vector2d(simplex.getPoint(2), simplex.getPoint(0));
        Vector2d acp = new Vector2d(ac.getY() * -1, ac.getX());
        if (Vector2d.sameDirection(simplex.getPoint(1), acp)) {
            acp.multiply(-1);
        }

        if (Vector2d.sameDirection(acp, ao)) {
            if (Vector2d.sameDirection(ac, ao)) {
                simplex.removePoint(1);
                simplex.d = acp;
            } else {
                simplex.removePoint(0);
                return checkLine(simplex);
            }
        } else if (Vector2d.sameDirection(abp, ao)) {
            simplex.removePoint(0);
            return checkLine(simplex);
        } else {
            return true;
        }

        return false;
    }


    @Override
    public Vector2d[] generateContacts(Vector2d collisionNormal, float penetration) {

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


    private class Simplex {

        public Vector2d d;

        private final ArrayList<Vector2d> points = new ArrayList<>();
        public void addPoint() {
            points.add(new Vector2d((polygons[1].GJKSupportFunction(Vector2d.multiply(d, -1))), polygons[0].GJKSupportFunction(d)));
        }
        public void removePoint(int i){
            points.remove(i);
        }
        public Vector2d getPoint(int i) {
            return points.get(i);
        }

        public int getNumVertices(){
            return points.size();
        }

    }
    private static class EPAFeature {

        public final Vector2d p1;
        public final Vector2d p2;

        public Vector2d normal;
        public float penetration;

        public EPAFeature(Vector2d p1, Vector2d p2){

            this.p1 = p1;
            this.p2 = p2;

            Vector2d ab = new Vector2d(p1, p2);
            Vector2d ap = p1.copy().multiply(-1);
            float scalar = Vector2d.dot(ap, ab);
            scalar /= (float) Math.pow(ab.getMagnitude(), 2);
            ab.multiply(scalar);
            normal = ab.add(p1);

            penetration = normal.getMagnitude();
            normal.multiply(1/penetration);

        }

    }
}
