public class Point2d {

    protected double x;
    public double getX() { return x; }
    public void setX(double x) { this.x = x; }

    protected double y;
    public double getY() { return y; }
    public void setY(double y) { this.y = y; }

    public void set(double x, double y){
        setX(x);
        setY(y);
    }
    public void set(Point2d p){
        setX(p.getX());
        setY(p.getY());
    }

    public Point2d(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Point2d() {
        this(0,0);
    }

    public Point2d(Point2d p1, Point2d p2){
        this(p2.getX() - p1.getX(), p2.getY() - p1.getY());
    }

    public Point2d translate(Point2d translation){
        setX(getX() + translation.getX());
        setY(getY() + translation.getY());
        return this;
    }

    public Point2d multiply(double multiplier){
        setX(x * multiplier);
        setY(y * multiplier);
        return this;
    }

    public double getMagnitude() {
        return Math.pow((Math.pow(getX(), 2) + Math.pow(getY(), 2)), 0.5);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public boolean equals(Point2d p) {
        return getY() == p.getY() && getX() == p.getX();
    }

    public static Point2d origin = new Point2d();

    public static Point2d add(Point2d p1, Point2d p2){
        return new Point2d(p1.getX() + p2.getX(), p1.getY() + p2.getY());
    }

    public static Point2d multiply(Point2d p1, double multiplier){
        return new Point2d(p1.getX() * multiplier, p1.getY() * multiplier);
    }

    public static double dotProduct(Point2d point1, Point2d point2){
        return (point1.getX() * point2.getX()) + (point1.getY() * point2.getY());
    }

    public static Point2d tripleProduct2d(Point2d p1, Point2d p2, Point2d p3){
        return multiply(p2, dotProduct(p1, p3)).translate(multiply(p3, -dotProduct(p1, p2)));
    }

    public static boolean sameDirection(Point2d d, Point2d target){
        return Point2d.dotProduct(d, target) > 0;
    }

    public static Point2d lineToPoint(Point2d a, Point2d b, Point2d p){
        Point2d ab = new Point2d(a, b);
        Point2d ap = new Point2d(a, p);
        double scalar = Point2d.dotProduct(ap, ab);
        scalar /= Math.pow(ab.getMagnitude(), 2);
        ab.multiply(scalar);
        return new Point2d(Point2d.origin, Point2d.add(a, ab));
    }
}
