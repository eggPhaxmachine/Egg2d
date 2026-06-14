package core.physics.narrowphase;

import core.physics.objects.DynamicPolygon2d
import core.physics.space.Vector2d;
import core.util.MinHeap;

import java.util.ArrayList;

public class PPCollision extends Collision {

    private final DynamicPolygon2d[] objects;

    public PPCollision(DynamicPolygon2d obj1, DynamicPolygon2d obj2) {
        objects = new DynamicPolygon2d[]{obj1, obj2};
    }
+
    @Override
    public void check() {

        GJKHybrid();
        if (penetration != 0) {

            findContacts();

        }

    }

    @Override
    public void correct(float restitution) {

        if (penetration == 0) {
            return;
        }

        for (Vector2d contact : contacts){

            Vector2d contactA = contact.copy().subtract(objects[0].getCenter());
            Vector2d contactB = contact.copy().subtract(objects[1].getCenter());

            Vector2d relVA = contactA.copy().perpendicular().multiply(objects[0].getAngularVelocity()).add(objects[0].getVelocity());
            Vector2d relVB = contactB.copy().perpendicular().multiply(objects[1].getAngularVelocity()).add(objects[1].getVelocity());

            Vector2d relV = relVB.copy().subtract(relVA);

            float temp = relV.dot(collisionNormal);
            if (temp > 0) break;

//            float numerator = -(1 + restitution);
//            numerator *= temp;

            float numerator = relV.multiply(-(1 + restitution)).dot(collisionNormal);

            float denominator = 1/objects[0].mass;
            denominator += 1/objects[1].mass;
            denominator += (float) Math.pow(contactA.cross(collisionNormal), 2) / objects[0].inertialMoment;
            denominator += (float) Math.pow(contactB.cross(collisionNormal), 2) / objects[1].inertialMoment;

            float j = numerator/denominator;

            Vector2d impulse = collisionNormal.copy().multiply(j);

            objects[0].velocity.subtract(impulse.copy().multiply(1/objects[0].mass));
            objects[0].angularVelocity -= contactA.cross(impulse)/objects[0].inertialMoment;

            objects[1].velocity.add(impulse.copy().multiply(1/objects[1].mass));
            objects[1].angularVelocity += contactB.cross(impulse)/objects[1].inertialMoment;

        }

        float wA = 1/objects[0].mass;
        float wB = 1/objects[1].mass;

        float wTotal = wA + wB;

        objects[0].translate(collisionNormal.copy().multiply(-1 * wA/wTotal * penetration));

        objects[1].translate(collisionNormal.copy().multiply(wB/wTotal * penetration));

    }


    private void GJKHybrid(){

        Simplex simplex = new Simplex();

        simplex.d = Settings.Engine.INITIAL_VECTOR;
        simplex.addPoint();

        simplex.d = Vector2d.multiply(simplex.getPoint(0), -1);

        while (true) {

            simplex.addPoint();

            if (!Vector2d.sameDirection(simplex.d, simplex.getPoint(simplex.getNumVertices() - 1))) {
                collided = false;
                penetration = 0;
                return;
            } else if (checkSimplex(simplex)) {
                collided = true;
                EPAlgorithm(simplex);
                return;
            }

        }

    }

    private void EPAlgorithm(Simplex simplex){

        MinHeap<EPAFeature> features = new MinHeap<>(objects[0].numVertices + objects[1].numVertices);
        Vector2d center = new Vector2d();

        for (int i = 0; i < 3; i++){
            center.add(simplex.getPoint(i));
            EPAFeature feature = new EPAFeature(simplex.getPoint(i), simplex.getPoint((i + 1) % 3));
            features.add(feature, feature.penetrationVector);
        }

        int numVertices = 3;

        while (true) {

            EPAFeature curFeature = features.popRoot();
            Vector2d searchVector = curFeature.normal;

            if (curFeature.penetrationVector < 0.5) {

                searchVector = new Vector2d(curFeature.p1, curFeature.p2).perpendicular();

                if (Vector2d.sameDirection(searchVector, center.multiply(1.0f/numVertices))){
                    searchVector.multiply(-1);
                }
                center.multiply(numVertices);

                curFeature.normal = new Vector2d();
                curFeature.penetrationVector = 0;

            }

            Vector2d searchResult = new Vector2d((objects[1].GJKSupportFunction(Vector2d.multiply(searchVector, -1))), objects[0].GJKSupportFunction(searchVector));

            if (searchResult.equals(curFeature.p1) || searchResult.equals(curFeature.p2)){
                collisionNormal = curFeature.normal;
                penetration = curFeature.penetrationVector;
                break;
            }

            center.add(searchResult);
            numVertices++;

            EPAFeature tempFeature = new EPAFeature(curFeature.p1, searchResult);
            features.add(tempFeature, tempFeature.penetrationVector);

            tempFeature = new EPAFeature(searchResult, curFeature.p2);
            features.add(tempFeature, tempFeature.penetrationVector);

        }

    }

    @Deprecated
    private void oldEPAlgorithm(Simplex simplex) {

        oldEPAFeature tempFeature;
        Vector2d tempPoint;

        MinHeap<oldEPAFeature> features = new MinHeap<>();
        oldEPAFeature curFeature;
        for (int i = 0; i < 3; i++) {
            curFeature = new oldEPAFeature(simplex.getPoint(i), simplex.getPoint((i + 1) % 3));
            features.add(curFeature, curFeature.penetration.getMagnitude());
        }

        while (true) {

            curFeature = features.popRoot();

            //edge case
            if (curFeature.penetration.getMagnitude() <= 0.5) {

                Vector2d perpendicular = curFeature.p2.copy().subtract(curFeature.p1).perpendicular();
                tempPoint = new Vector2d((objects[1].GJKSupportFunction(Vector2d.multiply(perpendicular, -1))), objects[0].GJKSupportFunction(perpendicular));

                if (tempPoint.equals(curFeature.p1) || tempPoint.equals(curFeature.p2)) {
                    curFeature.penetration = Vector2d.origin.copy();
                    break;
                }

                perpendicular.multiply(-1);
                tempPoint = new Vector2d((objects[1].GJKSupportFunction(Vector2d.multiply(perpendicular, -1))), objects[0].GJKSupportFunction(perpendicular));

                if (tempPoint.equals(curFeature.p1) || tempPoint.equals(curFeature.p2)) {
                    curFeature.penetration = Vector2d.origin.copy();
                    break;
                }

                continue;

            }

            tempPoint = new Vector2d((objects[1].GJKSupportFunction(Vector2d.multiply(curFeature.penetration, -1))), objects[0].GJKSupportFunction(curFeature.penetration));

            if (tempPoint.equals(curFeature.p1) || tempPoint.equals(curFeature.p2)) {
                break;
            }

            tempFeature = new oldEPAFeature(curFeature.p1, tempPoint);
            features.add(tempFeature, tempFeature.penetration.getMagnitude());

            tempFeature = new oldEPAFeature(curFeature.p2, tempPoint);
            features.add(tempFeature, tempFeature.penetration.getMagnitude());

        }

        penetration = curFeature.penetration.getMagnitude();
        collisionNormal = curFeature.penetration.copy().normalize();

    }

    private Vector2d[] test(int steps){

        ArrayList<Vector2d> points = new ArrayList<>();
        Vector2d d = new Vector2d(1, 0);

        points.add(new Vector2d((objects[1].GJKSupportFunction(Vector2d.multiply(d, -1))), objects[0].GJKSupportFunction(d)));

        for (int i = 1; i < steps; i++){

            d.rotate((float) ((2 * Math.PI) / steps));
            Vector2d temp = new Vector2d((objects[1].GJKSupportFunction(Vector2d.multiply(d, -1))), objects[0].GJKSupportFunction(d));
            if (!temp.equals(points.getLast())){
                points.add(temp);
            }

        }

        return points.toArray(Vector2d[]::new);

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

    private void findContacts(){

        Vector2d[] edge1;
        Vector2d[] edge2;


        float edgeDotProduct1;
        float edgeDotProduct2;
        float temp;

        Vector2d collisionNormal = this.collisionNormal.copy();
        Vector2d[] vertices = objects[0].getVertices();

        int furthestID = 0;
        double max = Vector2d.dot(vertices[0], collisionNormal);
        for (int i = 1; i < vertices.length; i++){
            temp = Vector2d.dot(vertices[i], collisionNormal);
            if (max < temp){
                max = temp;
                furthestID = i;
            }
        }

        edgeDotProduct1 = Vector2d.subtract(vertices[furthestID], vertices[Math.floorMod(furthestID - 1, vertices.length)]).normalize().dot(collisionNormal);
        temp = Vector2d.subtract(vertices[furthestID], vertices[(furthestID + 1) % vertices.length]).normalize().dot(collisionNormal);
        if (edgeDotProduct1 <= temp){
            edge1 = new Vector2d[]{vertices[Math.floorMod(furthestID - 1, vertices.length)], vertices[furthestID], vertices[furthestID]};
        } else {
            edge1 = new Vector2d[]{vertices[furthestID], vertices[(furthestID + 1) % vertices.length], vertices[furthestID]};
            edgeDotProduct1 = temp;
        }


        collisionNormal.multiply(-1);
        vertices = objects[1].getVertices();

        furthestID = 0;
        max = Vector2d.dot(vertices[0], collisionNormal);
        for (int i = 1; i < vertices.length; i++){
            temp = Vector2d.dot(vertices[i], collisionNormal);
            if (max < temp){
                max = temp;
                furthestID = i;
            }
        }

        edgeDotProduct2 = Vector2d.subtract(vertices[furthestID], vertices[Math.floorMod(furthestID - 1, vertices.length)]).normalize().dot(collisionNormal);
        temp = Vector2d.subtract(vertices[furthestID], vertices[(furthestID + 1) % vertices.length]).normalize().dot(collisionNormal);
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
            contacts = new Vector2d[]{};
            return;
        }

        temp = refv.dot(edge1[1]);
        edge2 = clip(edge2[0], edge2[1], refv.multiply(-1), -temp);

        refv.perpendicular();

        if (!flipped) refv.multiply(-1);

        temp = refv.dot(edge1[2]);

        if (refv.dot(edge2[0]) - temp < 0.0){
            contacts = new Vector2d[]{edge2[1]};
        }
        if (refv.dot(edge2[1]) - temp < 0.0){
            contacts = new Vector2d[]{edge2[0]};
        }

        contacts = edge2;

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
            points.add(new Vector2d((objects[1].GJKSupportFunction(Vector2d.multiply(d, -1))), objects[0].GJKSupportFunction(d)));
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

        public Vector2d penetrationVector;

        public EPAFeature(Vector2d p1, Vector2d p2){

            this.p1 = p1;
            this.p2 = p2;

            Vector2d ab = new Vector2d(p1, p2);
            Vector2d ap = p1.copy().multiply(-1);
            float scalar = Vector2d.dot(ap, ab);
            scalar /= (float) Math.pow(ab.getMagnitude(), 2);
            ab.multiply(scalar);
            penetrationVector = ab.add(p1);

        }

    }

    @Deprecated
    private static class oldEPAFeature {

        public Vector2d p1;
        public Vector2d p2;
        public Vector2d penetration;

        oldEPAFeature(Vector2d p1, Vector2d p2){

            this.p1 = p1;
            this.p2 = p2;
            penetration = Vector2d.lineToPoint(p1, p2, Vector2d.origin);

        }

    }
}