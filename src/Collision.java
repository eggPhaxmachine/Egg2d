import java.util.ArrayList;

public class Collision {

    private Hitbox[] objects;
    public Hitbox[] getObjects() { return objects; }

    private Point2d penetration;
    public Point2d getPenetration() { return penetration; }

    private boolean collided;
    public boolean isCollided() { return collided; }

    public Collision(Hitbox obj1, Hitbox obj2){

        objects = new Hitbox[]{obj1, obj2};

        ArrayList<Point2d> simplexVertices = new ArrayList<>(3);
        Point2d d = Settings.Engine.INITIAL_VECTOR;

        simplexVertices.addFirst(obj1.GJKSupportFunction(d).translate(obj2.GJKSupportFunction(Point2d.multiply(d, -1))));

        d = new Vector2d(simplexVertices.getFirst(), new Point2d());

        simplexVertices.addFirst(obj1.GJKSupportFunction(d).translate(obj2.GJKSupportFunction(Point2d.multiply(d, -1))));

        Vector2d ab = new Vector2d(simplexVertices.get(0), simplexVertices.get(1));
        d = new Vector2d(Point2d.tripleProduct2d(ab, new Vector2d(simplexVertices.getFirst(), new Point2d()), ab));

        while(true) {

            simplexVertices.addFirst(obj1.GJKSupportFunction(d).translate(obj2.GJKSupportFunction(Point2d.multiply(d, -1))));

            if(!Point2d.sameDirection(d, simplexVertices.getFirst())){

                collided = false;
                penetration = new Point2d();
                return;

            } else if (checkTriangle(simplexVertices, d)) {

                collided = true;
                break;

            }
        }

        

    }

    public static boolean checkTriangle(ArrayList<Point2d> simplex, Point2d d){

        Vector2d ab = new Vector2d(simplex.get(0), simplex.get(1));
        Vector2d ac = new Vector2d(simplex.get(0), simplex.get(2));
        Vector2d ao = new Vector2d(simplex.get(0), new Point2d(0,0));

        if(Point2d.sameDirection(Point2d.tripleProduct2d(ac, ab, ac), ao)){

            simplex.remove(1);

        } else if (Point2d.sameDirection(Point2d.tripleProduct2d(ab, ac, ab), ao)) {

            simplex.remove(2);

        } else {

            return true;

        }

        return false;
    }
}