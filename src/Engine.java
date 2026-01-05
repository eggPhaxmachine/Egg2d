import java.awt.*;
import java.util.ArrayList;
import java.util.InputMismatchException;

public class Engine implements Renderable {

    private ArrayList<Hitbox> dynamicObjects = new ArrayList<>();
    private ArrayList<Hitbox> constantObjects = new ArrayList<>();
    private ArrayList<Hitbox> objects = new ArrayList<>();

    public Hitbox[] getDynamicObjects(){
        return dynamicObjects.toArray(Hitbox[]::new);
    }
    public Hitbox[] getConstantObjects(){
        return constantObjects.toArray(Hitbox[]::new);
    }
    public Hitbox[] getObjects() {
        return objects.toArray(Hitbox[]::new);
    }
    public void addObject(Hitbox object){
        if(object.isUpdatable()) {
            dynamicObjects.add(object);
        } else {
            constantObjects.add(object);
        }

        objects.add(object);
    }

    private Vector2d constant;
    private double resistance;


    public Engine (Hitbox[] objects ,Vector2d constant, double resistance){

        for(Hitbox object : objects){
            addObject(object);
        }

        this.constant = constant;
        this.resistance = 1 - Math.clamp(resistance, 0, 1);

    }

    public Engine (Vector2d constant, double resistance, Hitbox... objects){

        this(objects, constant, resistance);

    }


    public void update(){

        for(Hitbox object : getDynamicObjects()){
            object.velocityVector.multiply(resistance);
            object.velocityVector.translate(constant);

            object.update();
        }

        //new Branch();

    }

    public void render(Graphics2D renderer){

        for(Hitbox object : getObjects()){

            renderer.setColor(object.color);

            switch (object.type){
                case Hitbox.HitboxType.POLYGON:

                    PolygonHitbox polygon = (PolygonHitbox)object;
                    int[][] vertices = polygon.getIntVertices();

                    renderer.fillPolygon(vertices[0], vertices[1], polygon.getNumVertices());

                    break;
                case Hitbox.HitboxType.CIRCLE: throw new InputMismatchException("Circle Type Unimplemented");
                default: throw new InputMismatchException("Unaccepted Hitbox Type");
            }
        }
    }

}
