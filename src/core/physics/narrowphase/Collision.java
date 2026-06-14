package core.physics.narrowphase;

import core.physics.objects.RigidBody2d;
import core.physics.shapes.Circle2d;
import core.physics.shapes.Polygon2d;
import core.physics.shapes.Shape2d;
import core.physics.space.Vector2d;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.function.Function;

public class Collision {

    public Shape2d[] objects;

    protected boolean collided;
    public boolean isCollided() {
        return collided;
    }

    protected float penetration;
    public float getPenetration() {
        return penetration;
    }

    protected Vector2d collisionNormal;
    public Vector2d getCollisionNormal() {
        return collisionNormal;
    }

    protected Vector2d[] contacts;
    public Vector2d[] getContact() {
        return contacts;
    }


    private final NarrowPhaseDetection narrowPhase;

    private final PhysicsResolution physicsResolution;

    public Collision(Shape2d[] objects){

        if (objects.length > 2) throw new InputMismatchException("Collisions can only compare two objects");
        if (objects[0].type < objects[1].type){
            objects = new Shape2d[]{objects[1], objects[0]};
        } else {
            this.objects = objects;
        }

        try {
            narrowPhase = narrowPhaseDispatcher.get(this.objects[0].type).get(this.objects[1].type).apply(objects);
            if (narrowPhase == null) throw new ArrayIndexOutOfBoundsException();
        } catch (ArrayIndexOutOfBoundsException e){
            throw new UnsupportedOperationException("No narrow phase implementation for shape types: " + this.objects[0].type + ", " + this.objects[1].type);
        }

        try {
            physicsResolution = physicsResolutionDispatcher.get(this.objects[0].type).get(this.objects[1].type).apply(objects);
            if (physicsResolution == null) throw new ArrayIndexOutOfBoundsException();
        } catch (ArrayIndexOutOfBoundsException e){
            throw new UnsupportedOperationException("No physics resolution implementation for body types: " + this.objects[0].type + ", " + this.objects[1].type);
        }

    }

    public Collision(Shape2d shape1, Shape2d shape2){
        this(new Shape2d[]{shape1, shape2});
    }


    private final static ArrayList<ArrayList<Function<Shape2d[], NarrowPhaseDetection>>> narrowPhaseDispatcher = new ArrayList<>(3);
    private final static ArrayList<ArrayList<Function<Shape2d[], PhysicsResolution>>> physicsResolutionDispatcher = new ArrayList<>(3);

    static {

        ArrayList<Function<Shape2d[], NarrowPhaseDetection>> curArray;

        curArray = new ArrayList<>(3);
        curArray.add((Shape2d[] shapes) -> new PPNarrowPhaseDetection((Polygon2d[]) shapes));
        curArray.add((Shape2d[] shapes) -> new PCNarrowPhaseDetection((Polygon2d) shapes[0], (Circle2d) shapes[1]));
        narrowPhaseDispatcher.add(curArray);

        curArray = new ArrayList<>(3);
        curArray.add(null);
        curArray.add((Shape2d[] shapes) -> new CCNarrowPhaseDetection((Circle2d[]) shapes));
        narrowPhaseDispatcher.add(curArray);

    }


}
