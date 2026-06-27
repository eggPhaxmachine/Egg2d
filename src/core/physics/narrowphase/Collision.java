package physics.narrowphase;

import physics.objects.DynamicBody2d;
import physics.objects.StaticBody2d;
import physics.shapes.Circle2d;
import physics.shapes.Polygon2d;
import physics.shapes.Shape2d;
import physics.space.Vector2d;

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
        this.objects = objects;

        try {
            narrowPhase = narrowPhaseDispatcher.get(objects[0].shapeType).get(objects[1].shapeType).apply(objects);
            if (narrowPhase == null) throw new ArrayIndexOutOfBoundsException();
        } catch (ArrayIndexOutOfBoundsException e){
            throw new UnsupportedOperationException("No narrow phase implementation for shape types: " + this.objects[0].shapeType + ", " + this.objects[1].shapeType);
        }

        try {
            physicsResolution = physicsResolutionDispatcher.get(objects[0].getBodyType() == 1 ? objects[0].getBodyType() : objects[1].getBodyType()).apply(objects);
            if (physicsResolution == null) throw new ArrayIndexOutOfBoundsException();
        } catch (ArrayIndexOutOfBoundsException e){
            throw new UnsupportedOperationException("No physics resolution implementation for body types: " + this.objects[0].shapeType + ", " + this.objects[1].shapeType);
        }

    }

    public Collision(Shape2d shape1, Shape2d shape2){
        this(new Shape2d[]{shape1, shape2});
    }


    public void check(){

        Vector2d penetrationVector = narrowPhase.check();

        penetration = penetrationVector.getMagnitude();
        collisionNormal = penetrationVector.multiply(1/penetration);

        if (penetration != 0) {
            collided = true;
            contacts = narrowPhase.generateContacts(collisionNormal, penetration);
        } else {
            collided = false;
        }

    }

    public void correct(){
        physicsResolution.correct(contacts, collisionNormal, penetration);
    }


    private final static ArrayList<ArrayList<Function<Shape2d[], NarrowPhaseDetection>>> narrowPhaseDispatcher = new ArrayList<>(2);
    private final static ArrayList<Function<Shape2d[], PhysicsResolution>> physicsResolutionDispatcher = new ArrayList<>(2);

    static {

        ArrayList<Function<Shape2d[], NarrowPhaseDetection>> curArray;

        curArray = new ArrayList<>(2);
        curArray.add((Shape2d[] shapes) -> new PPSATNarrowPhase(new Polygon2d[]{(Polygon2d) shapes[0], (Polygon2d) shapes[1]}));
        curArray.add((Shape2d[] shapes) -> new PCNarrowPhaseDetection((Polygon2d) shapes[0], (Circle2d) shapes[1]));
        narrowPhaseDispatcher.add(curArray);

        curArray = new ArrayList<>(2);
        curArray.add((Shape2d[] shapes) -> new PCNarrowPhaseDetection((Polygon2d) shapes[1], (Circle2d) shapes[0]));
        curArray.add((Shape2d[] shapes) -> new CCNarrowPhaseDetection(new Circle2d[]{(Circle2d) shapes[0], (Circle2d) shapes[1]}));
        narrowPhaseDispatcher.add(curArray);


        physicsResolutionDispatcher.add((Shape2d[] shapes) -> new DDPhysicsResolution(new DynamicBody2d[]{(DynamicBody2d) shapes[0], (DynamicBody2d) shapes[1]}));
        physicsResolutionDispatcher.add((Shape2d[] shapes) -> new DKPhysicsResolution());
        physicsResolutionDispatcher.add((Shape2d[] shapes) -> new DSPhysicsResolution((DynamicBody2d) shapes[0], (StaticBody2d) shapes[1]));

    }


}
