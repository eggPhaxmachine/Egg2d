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
    
    public void check(){

        ArrayList<Point2d> simplexVertices = new ArrayList<>();
        Point2d d = Settings.Engine.INITIAL_VECTOR;

        simplexVertices.addFirst(new Point2d((objects[1].GJKSupportFunction(Point2d.multiply(d, -1))), objects[0].GJKSupportFunction(d)));

        d = Point2d.multiply(simplexVertices.getFirst(), -1);

        while(true) {

            simplexVertices.addFirst(new Point2d((objects[1].GJKSupportFunction(Point2d.multiply(d, -1))), objects[0].GJKSupportFunction(d)));

            if(!Point2d.sameDirection(d, simplexVertices.getFirst())){

                collided = false;
                penetration = new Vector2d(Point2d.origin);
                return;

            } else if (checkSimplex(simplexVertices, d)) {

                collided = true;
                break;

            }
        }

        MinHeap<Wrapper<Point2d[], Vector2d>>  features = new MinHeap<>();
        Wrapper<Point2d[], Vector2d>  curFeature;
        for(int i = 0; i < 3; i++){
            curFeature = new Wrapper<>(new Point2d[]{simplexVertices.get(i), simplexVertices.get((i + 1) % 3)}, null);
            curFeature.obj2 = Vector2d.lineToPoint(curFeature.obj1, Point2d.origin);
            features.add(curFeature, curFeature.obj2.getMagnitude());
        }

        while(true){

            curFeature = features.popRoot().object;
            Wrapper<Point2d[], Vector2d> tempFeature;
            d = new Point2d((objects[1].GJKSupportFunction(Point2d.multiply(curFeature.obj2, -1))), objects[0].GJKSupportFunction(curFeature.obj2));

            if (d == curFeature.obj1[0] || d == curFeature.obj1[1]) {
                penetration = curFeature.obj2;
                return;
            }

            tempFeature = new Wrapper<>(new Point2d[]{curFeature.obj1[0], d}, null);
            tempFeature.obj2 = Vector2d.lineToPoint(tempFeature.obj1, Point2d.origin);
            features.add(tempFeature, tempFeature.obj2.getMagnitude());

            tempFeature = new Wrapper<>(new Point2d[]{curFeature.obj1[1], d}, null);
            tempFeature.obj2 = Vector2d.lineToPoint(tempFeature.obj1, Point2d.origin);
            features.add(tempFeature, tempFeature.obj2.getMagnitude());

        }
    }

    public void correct(){
        objects[0].translate(penetration);
    }

    private static boolean checkSimplex(ArrayList<Point2d> simplexVertices, Point2d d){
        return switch (simplexVertices.size()) {
            case (2) -> checkLine(simplexVertices, d);
            case (3) -> checkTriangle(simplexVertices, d);
            default -> throw new UnsupportedOperationException();
        };
    }

    private static boolean checkLine(ArrayList<Point2d> simplex, Point2d d){

        Point2d ab = new Point2d(simplex.get(0), simplex.get(1));
        Point2d ao = Point2d.multiply(simplex.get(0), -1);

        if(Point2d.sameDirection(ao, ab)){

            Point2d temp = new Point2d(ab.getY(), -ab.getX());
            if (!Point2d.sameDirection(ao, temp)) {
                temp.multiply(-1);
            }
            d.set(temp);

        } else {
            simplex.removeLast();
            d.set(ao);
        }

        return false;

    }

    private static boolean checkTriangle(ArrayList<Point2d> simplex, Point2d d){

        Point2d ao = Point2d.multiply(simplex.get(0), -1);

        Point2d abp;
        Point2d ab;
        ab = new Point2d(simplex.get(0), simplex.get(1));
        abp = new Point2d(ab.getY() * -1, ab.getX());
        if (Point2d.sameDirection(simplex.get(2), abp)){
            abp.multiply(-1);
        }


        Point2d acp;
        Point2d ac;
        ac = new Point2d(simplex.get(0), simplex.get(2));
        acp = new Point2d(ac.getY() * -1, ac.getX());
        if (Point2d.sameDirection(simplex.get(1), acp)){
            acp.multiply(-1);
        }

        if(Point2d.sameDirection(acp, ao)){
            if (Point2d.sameDirection(ac, ao)){
                simplex.remove(1);
                d.set(acp);
            } else {
                simplex.removeLast();
                return checkLine(simplex, d);
            }
        } else if (Point2d.sameDirection(abp, ao)){
            simplex.removeLast();
            return checkLine(simplex, d);
        } else {
            return true;
        }

        return false;
    }
}