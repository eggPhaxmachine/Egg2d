public class Collision {

    private Hitbox[] objects;
    public Hitbox[] getObjects() { return objects }

    private double magnitude;
    public double getMagnitude() { return magnitude; }

    private boolean collided;
    public boolean isCollided() { return collided; }

    public

    public Collision(boolean collided){

        this.collided = collided;

    }
}