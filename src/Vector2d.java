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

}
