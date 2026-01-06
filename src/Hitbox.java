import java.awt.*;

public abstract class Hitbox implements Renderable{

    public Vector2d velocityVector = new Vector2d();

    protected boolean updatable;
    public boolean isUpdatable() { return updatable; }

    protected HitboxType type;
    public HitboxType getType() { return type; }

    public Color color;

    public enum HitboxType
    {
        CIRCLE,
        POLYGON
    }

    public Hitbox (HitboxType type, boolean updatable, Color color){

        this.type = type;
        this.updatable = updatable;
        this.color = color;

    }

    @Override
    public abstract void render(Graphics2D renderer);

    @Override
    public abstract void update();

    public abstract AABB fitAABB(double fattening);

    public abstract boolean AABBCheck(AABB aabb);

    //public abstract Point2d getLocation();
    public abstract void translate(Vector2d translation);

    public abstract Point2d GJKSupportFunction(Point2d d);

}
