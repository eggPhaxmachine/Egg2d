import java.util.ArrayList;

public class Collision {

    private Hitbox[] objects;
    public Hitbox[] getObjects() { return objects; }

    private Vector2d penetration;
    public Point2d getPenetration() { return penetration; }

    private boolean collided;
    public boolean isCollided() { return collided; }

    public Collision(Hitbox obj1, Hitbox obj2){

        objects = new Hitbox[]{obj1, obj2};
        check();

    }

    Point2d origin = new Point2d();
    ArrayList<Point2d> simplexVertices;
    Point2d p;
    MinHeap<Wrapper<Point2d[], Vector2d>> features;
    Wrapper<Point2d[], Vector2d> curFeature;
    Wrapper<Point2d[], Vector2d> tempFeature;
    public void check(){

        simplexVertices = new ArrayList<>();
        p = Settings.Engine.INITIAL_VECTOR;

        simplexVertices.addFirst(Point2d.add(objects[0].GJKSupportFunction(p), (objects[1].GJKSupportFunction(Point2d.multiply(p, -1)))));

        p = new Vector2d(simplexVertices.getFirst(), origin);

        simplexVertices.addFirst(Point2d.add(objects[0].GJKSupportFunction(p), (objects[1].GJKSupportFunction(Point2d.multiply(p, -1)))));

        Vector2d ab = new Vector2d(simplexVertices.get(0), simplexVertices.get(1));
        p = Point2d.tripleProduct2d(ab, new Vector2d(simplexVertices.getFirst(), origin), ab);

        while(true) {

            simplexVertices.addFirst(Point2d.add(objects[0].GJKSupportFunction(p), (objects[1].GJKSupportFunction(Point2d.multiply(p, -1)))));

            if(!Point2d.sameDirection(p, simplexVertices.getFirst())){

                collided = false;
                penetration = new Vector2d(origin);
                return;

            } else if (checkTriangle(simplexVertices, p)) {

                collided = true;
                break;

            }
        }

        features = new MinHeap<>();
        for(int i = 0; i < 3; i++){
            curFeature = new Wrapper<>(new Point2d[]{simplexVertices.get(i), simplexVertices.get((i + 1) % 3)}, null);
            curFeature.obj2 = Vector2d.lineToPoint(curFeature.obj1, origin);
            features.add(curFeature, curFeature.obj2.getMagnitude());
        }

        while(true){

            curFeature = features.popRoot().object;
            p = Point2d.add(objects[0].GJKSupportFunction(curFeature.obj2), (objects[1].GJKSupportFunction(Point2d.multiply(curFeature.obj2, -1))));

            if (p == curFeature.obj1[0] || p == curFeature.obj1[1]) {
                penetration = curFeature.obj2;
                return;
            }

            tempFeature = new Wrapper<>(new Point2d[]{curFeature.obj1[0], p}, null);
            tempFeature.obj2 = Vector2d.lineToPoint(tempFeature.obj1, origin);
            features.add(tempFeature, tempFeature.obj2.getMagnitude());

            tempFeature = new Wrapper<>(new Point2d[]{curFeature.obj1[1], p}, null);
            tempFeature.obj2 = Vector2d.lineToPoint(tempFeature.obj1, origin);
            features.add(tempFeature, tempFeature.obj2.getMagnitude());

        }
    }

    public void correct(){
        objects[0].translate(penetration);
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