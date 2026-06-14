package core.physics.space;

public class Vector2d {

    public float x;
    @Deprecated
    public float getX() { return x; }
    @Deprecated
    public void setX(float x) { this.x = x; }

    public float y;
    @Deprecated
    public float getY() { return y; }
    @Deprecated
    public void setY(float y) { this.y = y; }

    public Vector2d(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Vector2d() {
        this(0,0);
    }

    public Vector2d(Vector2d p1, Vector2d p2){
        this(p2.getX() - p1.getX(), p2.getY() - p1.getY());
    }

    public Vector2d add(Vector2d translation){
        setX(getX() + translation.getX());
        setY(getY() + translation.getY());
        return this;
    }

    public Vector2d subtract(Vector2d translation){
        setX(getX() - translation.getX());
        setY(getY() - translation.getY());
        return this;
    }

    public Vector2d multiply(float multiplier){
        setX(x * multiplier);
        setY(y * multiplier);
        return this;
    }

    public float getMagnitude() {
        return (float) Math.pow((Math.pow(getX(), 2) + Math.pow(getY(), 2)), 0.5);
    }

    public float dot(Vector2d p1){
        return dot(this, p1);
    }

    public float cross(Vector2d p1){
        return cross(this, p1);
    }

    public Vector2d copy(){
        return new Vector2d(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public boolean equals(Vector2d p) {
        return getY() == p.getY() && getX() == p.getX();
    }



    public static Vector2d add(Vector2d p1, Vector2d p2){
        return new Vector2d(p1.getX() + p2.getX(), p1.getY() + p2.getY());
    }

    public static Vector2d subtract(Vector2d p1, Vector2d p2){
        return new Vector2d(p1.getX() - p2.getX(), p1.getY() - p2.getY());
    }

    public static Vector2d multiply(Vector2d p1, float multiplier){
        return new Vector2d(p1.getX() * multiplier, p1.getY() * multiplier);
    }

    public Vector2d normalize(){
        float magnitude = getMagnitude();
        if (magnitude == 0) return this;
        setX(getX() / magnitude);
        setY(getY() / magnitude);
        return this;
    }

    public Vector2d rotate(float theta){
        float x = getX();
        float y = getY();
        setX((float) (x * Math.cos(theta) - y * Math.sin(theta)));
        setY((float) (y * Math.cos(theta) + x * Math.sin(theta)));
        return this;
    }

    public Vector2d perpendicular(){
        return perpendicular(false);
    }

    public Vector2d perpendicular(boolean clockwise){

        float temp = x;
        x = y;
        y = temp;

        if (clockwise){
            y *= -1;
        } else {
            x *= -1;
        }

        return this;

    }

    public static float dot(Vector2d point1, Vector2d point2){
        return (point1.getX() * point2.getX()) + (point1.getY() * point2.getY());
    }

    public static float cross(Vector2d p1, Vector2d p2){
        return (p1.x * p2.y) - (p1.y * p2.x);
    }

    public static boolean sameDirection(Vector2d p1, Vector2d p2){
        return Vector2d.dot(p1, p2) > 0;
    }

    public static Vector2d normalize(Vector2d p){
        float magnitude = p.getMagnitude();
        if (magnitude == 0) return p.copy();
        float x = p.getX() / magnitude;
        float y = p.getY() / magnitude;
        return new Vector2d(x, y);
    }


    public static Vector2d origin = new Vector2d();


    @Deprecated
    public static Vector2d lineToPoint(Vector2d a, Vector2d b, Vector2d p){
        Vector2d ab = new Vector2d(a, b);
        Vector2d ap = new Vector2d(a, p);
        float scalar = Vector2d.dot(ap, ab);
        scalar /= (float) Math.pow(ab.getMagnitude(), 2);
        ab.multiply(scalar);
        return ab.add(a).subtract(p);
    }
}
