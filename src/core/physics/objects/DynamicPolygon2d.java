package core.physics.objects;

import core.physics.broadphase.AABB;
import core.physics.shapes.Polygon2d;
import core.physics.space.Vector2d;

import java.util.InputMismatchException;

public class DynamicPolygon2d extends Polygon2d implements DynamicBody2d {

    protected Vector2d updateVector = new Vector2d();
    protected float updateRotation = 0;

    @Override
    public Vector2d[] getVertices() {

        if(!(updateVector.equals(Vector2d.origin)) || updateRotation != 0){

            Vector2d in = Vector2d.multiply(center, -1);
            Vector2d out = Vector2d.add(center, updateVector);

            for(Vector2d vertex : vertices){

                vertex.add(in);
                vertex.rotate(updateRotation);
                vertex.add(out);

            }

            center.add(updateVector);

            updateRotation = 0;
            updateVector.setX(0);
            updateVector.setY(0);

        }

        return vertices;

    }

    @Override
    public Vector2d getCenter(){
        getVertices();
        return center;
    }

    protected float density = 1;
    @Override
    public float getDensity() {
        return density;
    }
    @Override
    public void setDensity(float density) {

        if (density < 0) throw new InputMismatchException("density cannot be less than zero");
        if (density == this.density) return;

        this.inertialMoment *= density/this.density;
        this.density = density;
        this.mass = area * density;

    }

    protected float mass;
    @Override
    public float getMass() {
        return mass;
    }
    @Override
    public void setMass(float mass){
        setDensity(mass/area);
    }

    protected float inertialMoment;
    @Override
    public float getInertialMoment() {
        return inertialMoment;
    }

    protected float restitution;
    @Override
    public float getRestitution() {
        return restitution;
    }
    public void setRestitution(float restitution) {
        this.restitution = restitution;
    }

    protected Vector2d velocity = new Vector2d();
    @Override
    public Vector2d getVelocity() {
        return velocity;
    }
    @Override
    public void setVelocity(Vector2d velocity) {
        this.velocity = velocity;
    }

    protected float angularVelocity;
    @Override
    public float getAngularVelocity() {
        return angularVelocity;
    }
    @Override
    public void setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    public DynamicPolygon2d(int id, Vector2d[] vertices) {
        super(id, vertices);

        mass = area * density;

        Vector2d p1 = vertices[0];
        Vector2d p2, p3;
        float triangleArea;
        for(int i = 0; i < numVertices - 2; i++){

            p2 = vertices[i+1].copy();
            p3 = vertices[i+2].copy();

            triangleArea = 0.5f * Math.abs(p1.x*(p2.y - p3.y) + p2.x*(p3.y - p1.y) + p3.x*(p1.y - p2.y));

            p2.subtract(p1);
            p3.subtract(p1);
            inertialMoment += triangleArea*density/6 * (float)(Math.pow(p2.getMagnitude(), 2) + Math.pow(p3.getMagnitude(), 2) + p2.dot(p3));

        }
        inertialMoment -= (float)Math.pow(Vector2d.subtract(p1, center).getMagnitude(), 2) * mass;
    }

    @Override
    public void translate(Vector2d translation){
        updateVector.add(translation);
    }

    @Override
    public void rotate(float theta) {
        updateRotation += theta;
    }

    @Override
    public AABB fitAABB() {

        Vector2d[] points = getVertices();

        float xMax = points[0].getX();
        float xMin = points[0].getX();
        float yMax = points[0].getY();
        float yMin = points[0].getY();

        for (int i = 1; i < points.length; i++){

            if(points[i].getX() > xMax) xMax = points[i].getX();
            else if (points[i].getX() < xMin) xMin = points[i].getX();

            if (points[i].getY() > yMax) yMax = points[i].getY();
            else if ( points[i].getY() < yMin) yMin = points[i].getY();

        }

        Vector2d normal = Vector2d.normalize(velocity);

        if (normal.x > 0) xMax += (normal.x * 50);
        else xMin += (normal.x * 50);

        if (normal.y > 0) yMax += (normal.y * 50);
        else yMin += (normal.y * 50);

        return new AABB(new Vector2d(xMin, yMin), new Vector2d(xMax, yMax));

    }

    @Override
    public boolean AABBCheck(AABB aabb) {
        return AABB.AABBCheck(getVertices(), aabb);
    }

}
