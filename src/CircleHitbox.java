import java.awt.*;

public class CircleHitbox extends Hitbox{

    private double radius;
    public double getRadius() { return radius; }

    private AABB AABB;
    @Override
    public AABB getBoundingBox() { return AABB; }

    private Point2d location;
    @Override
    public Point2d getLocation() { return location; }


    public CircleHitbox(Point2d location, double radius, boolean isUpdatable){

        super(HitboxType.CIRCLE ,isUpdatable, Color.BLACK);

        this.location = location;
        this.radius = radius;

        AABB = new AABB(new Point2d(location.getX() - radius, location.getY() - radius), new Point2d(location.getX() + radius, location.getY() + radius));

    }

    @Override
    public void translate(Vector2d translation){

        location.translate(translation);
        AABB.translate(translation);

    }

}
