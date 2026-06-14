package core.physics.shapes;

import core.managment.GameObject;
import core.physics.space.Vector2d;

public abstract class Shape2d extends GameObject {

    public final int type;

    protected float area;
    public float getArea() {
        return area;
    }

    protected Vector2d center;
    public Vector2d getCenter() {
        return center;
    }

    public Shape2d(int id, int type){

        super(id);
        this.type = type;

    }


    public static final int POLYGON = 0;
    public static final int CIRCLE = 1;
    public static final int TRIANGLE = 2;

}
