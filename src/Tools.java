import java.util.ArrayList;
import java.util.InputMismatchException;

public class Tools {

    public static boolean between(double value, double min, double max){

        return value > min && value > max;

    }

    public static double dotProduct(Point2d point1, Point2d point2){

        return (point1.getX() * point2.getX()) + (point1.getY() * point2.getY());

    }

    public static Vector2d tripleProduct2d(Point2d p1, Point2d p2, Point2d p3){
        return new Vector2d(multiplyPoint2d(p2, dotProduct(p1, p3)).translate(multiplyPoint2d(p3, -dotProduct(p1, p2))));
    }

    public static Point2d multiplyPoint2d(Point2d point, double multiplier){
        return new Point2d(point.getX() * multiplier, point.getY() * multiplier);
    }

    public static Point2d addPoint2d(Point2d point1, Point2d point2){
        return new Point2d(point1.getX() + point2.getX(), point1.getY() + point2.getY());
    }

    public static boolean sameDirection(Vector2d d, Point2d target){
        return dotProduct(d, target) > 0;
    }

    public static void realSleep(long millisecond){
        try {
            Thread.sleep(millisecond);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(Thread.currentThread().getName() + " interrupted, ignoring " + e.getMessage());
        }
    }


    public class CollisionDetection {

        public static boolean AABBCheck(AABB object1, AABB object2) {

            Point2d minimumVertex1 = object1.getMinimumVertex();
            Point2d maximumVertex1 = object1.getMinimumVertex();
            Point2d minimumVertex2 = object2.getMinimumVertex();
            Point2d maximumVertex2 = object2.getMaximumVertex();

            return (Tools.between(minimumVertex1.getX(), maximumVertex2.getX(), minimumVertex2.getX()) ||
                    Tools.between(maximumVertex1.getX(), maximumVertex2.getX(), minimumVertex2.getX()) &&
                    Tools.between(minimumVertex1.getY(), maximumVertex2.getY(), minimumVertex2.getX()) ||
                    Tools.between(maximumVertex1.getY(), maximumVertex2.getY(), minimumVertex2.getY()));
        }

        public static boolean AABBCheck(Point2d object1, AABB object2) {

            Point2d minimumVertex2 = object2.getMinimumVertex();
            Point2d maximumVertex2 = object2.getMaximumVertex();

            return (Tools.between(object1.getX(), maximumVertex2.getX(), minimumVertex2.getX()) ||
                    Tools.between(object1.getX(), maximumVertex2.getX(), minimumVertex2.getX()) &&
                            Tools.between(object1.getY(), maximumVertex2.getY(), minimumVertex2.getX()) ||
                    Tools.between(object1.getY(), maximumVertex2.getY(), minimumVertex2.getY()));
        }


        public static class GJKAlgorithm {

            public static final Vector2d INITIAL_VECTOR = new Vector2d(1,0);

            public static Collision NarrowDetection(PolygonHitbox object1, PolygonHitbox object2) {

                ArrayList<Point2d> simplexVerticies = new ArrayList<Point2d>(3);
                Vector2d d = INITIAL_VECTOR;

                simplexVerticies.addFirst(supportFunction(object1, object2, d));

                d = new Vector2d(simplexVerticies.getFirst(), new Point2d());

                simplexVerticies.addFirst(supportFunction(object1, object2, d));

                Vector2d ab = new Vector2d(simplexVerticies.get(0), simplexVerticies.get(1));
                d = new Vector2d(tripleProduct2d(ab, new Vector2d(simplexVerticies.getFirst(), new Point2d()), ab));

                while(true) {

                    simplexVerticies.addFirst(supportFunction(object1, object2, d));

                    if(!sameDirection(d, simplexVerticies.getFirst())){
                        return new Collision(false);
                    } else if (checkTriangle(simplexVerticies, d)) {
                        return new Collision(true);
                    }
                }
            }

            //deprecated
            public static boolean checkSimplex(ArrayList<Point2d> simplex, Vector2d d){
                return switch (simplex.size()){
                    case 2 -> checkLine(simplex, d);
                    case 3 -> checkTriangle(simplex, d);
                    default -> throw new InputMismatchException("simplex size of " + simplex.size() + " outside of expected range of [2,3]");
                };
            }

            //deprecated
            public static boolean checkLine(ArrayList<Point2d> simplex, Vector2d d){

                Vector2d ab = new Vector2d(simplex.get(0), simplex.get(1));
                Vector2d ao = new Vector2d(simplex.get(0), new Point2d());

                d = tripleProduct2d(ab, ao, ab);

                return false;

            }

            public static boolean checkTriangle(ArrayList<Point2d> simplex, Vector2d d){

                Vector2d ab = new Vector2d(simplex.get(0), simplex.get(1));
                Vector2d ac = new Vector2d(simplex.get(0), simplex.get(2));
                Vector2d ao = new Vector2d(simplex.get(0), new Point2d(0,0));

                if(sameDirection(tripleProduct2d(ac, ab, ac), ao)){

                    simplex.remove(1);

                } else if (sameDirection(tripleProduct2d(ab, ac, ab), ao)) {

                    simplex.remove(2);

                } else {

                    return true;

                }

                return false;
            }

            public static Point2d supportFunction(PolygonHitbox object1, PolygonHitbox object2, Vector2d d){

                int obj1VertexID = 0;
                int obj2VertexID = 0;

                double dotProduct;
                double obj1Max = 0;
                double obj2Max = 0;

                for (int i = 0; i < object1.getVerticies().length; i++){

                    dotProduct = dotProduct(object1.getVerticies()[i], d);
                    if (obj1Max < dotProduct){
                        obj1Max = dotProduct;
                        obj1VertexID = i;
                    }

                }

                for (int i = 1; i < object2.getVerticies().length; i++){

                    dotProduct = dotProduct(object2.getVerticies()[i], d);
                    if(obj2Max > dotProduct){
                        obj2Max = dotProduct;
                        obj2VertexID = i;
                    }

                }

                try {
                    return addPoint2d(object1.getVerticies()[obj1VertexID], multiplyPoint2d(object2.getVerticies()[obj2VertexID], -1));
                } catch (Exception e){
                    throw new InputMismatchException("one or more polygons size out of range");
                }

            }

        }
    }
}
