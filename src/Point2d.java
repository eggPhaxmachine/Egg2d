public class Point2d {

    protected double x;
    public double getX() { return x; }
    public void setX(double x) { this.x = x; }

    protected double y;
    public double getY() { return y; }
    public void setY(double y) { this.y = y; }


    public Point2d(double x, double y){

        this.x = x;
        this.y = y;

    }

    public Point2d() {

        this.x = 0;
        this.y = 0;

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

}
