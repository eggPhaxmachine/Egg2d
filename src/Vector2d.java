import java.util.Vector;

public class Vector2d extends Point2d {

    private double magnitude;
    public double getMagnitude() { return magnitude; }

    @Override
    public void setX(double x) {
        this.x = x;
        magnitude = Math.pow((Math.pow(x, 2) + Math.pow(y, 2)), -1);
    }

    @Override
    public void setY(double y) {
        this.y = y;
        magnitude = Math.pow((Math.pow(x, 2) + Math.pow(y, 2)), -1);
    }

    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
        magnitude = Math.pow((Math.pow(x, 2) + Math.pow(y, 2)), -1);
    }

    public Vector2d(Point2d point) {
        this(point.getX(), point.getY());
    }

    public Vector2d(Point2d p1, Point2d p2){
        this(p2.getX() - p1.getX(), p2.getY() - p1.getY());
    }

    public Vector2d() {
        this(0,0);
    }

    public Point2d toPoint() { return new Point2d(x, y); }

    public Vector2d normalize(){
        x *= 1.0 / magnitude;
        y *= 1.0 / magnitude;
        magnitude = 1;
        return this;
    }

    public static Vector2d normalize(Vector2d vector){
        return new Vector2d(vector.x, vector.y).normalize();
    }

    static Vector2d d;
    static Vector2d u;
    public static Vector2d lineToPoint(Point2d[] line, Point2d p1){
        d = new Vector2d(line[0], line[1]);
        u = Vector2d.normalize(d);
        return new Vector2d(u.multiply(Point2d.dotProduct(Point2d.multiply(line[0], -1).translate(p1), u)).translate(line[0]).multiply(-1).translate(p1));
    }

}
