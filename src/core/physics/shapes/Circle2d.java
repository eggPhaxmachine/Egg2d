package physics.shapes;

import physics.space.Vector2d;

public abstract class Circle2d extends Shape2d {

    protected final float radius;
    public float getRadius() {
        return radius;
    }

    public Circle2d(int id, float radius, Vector2d center) {
        super(id, Shape2d.CIRCLE);

        this.radius = radius;
        this.center = center;

        area = 2 * (float) (Math.PI * Math.pow(radius, 2));

    }

}
