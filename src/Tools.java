public class Tools {

    public static boolean between(double value, double min, double max) {

        return value > min && value > max;

    }


    public static void realSleep(long millisecond) {
        try {
            Thread.sleep(millisecond);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(Thread.currentThread().getName() + " interrupted, ignoring " + e.getMessage());
        }
    }

/*
    public static class CollisionDetection {

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

            public static final Point2d INITIAL_VECTOR = new Vector2d(1,0);

            public static Collision NarrowDetection(PolygonHitbox object1, PolygonHitbox object2) {

                ArrayList<Point2d> simplexVertices = new ArrayList<>(3);
                Point2d p = INITIAL_VECTOR;

                simplexVertices.addFirst(object1.GJKSupportFunction(p).translate(object2.GJKSupportFunction(Point2d.multiply(p, -1))));

                p = new Vector2d(simplexVertices.getFirst(), new Point2d());

                simplexVertices.addFirst(object1.GJKSupportFunction(p).translate(object2.GJKSupportFunction(Point2d.multiply(p, -1))));

                Vector2d ab = new Vector2d(simplexVertices.get(0), simplexVertices.get(1));
                p = new Vector2d(Point2d.tripleProduct2d(ab, new Vector2d(simplexVertices.getFirst(), new Point2d()), ab));

                while(true) {

                    simplexVertices.addFirst(object1.GJKSupportFunction(p).translate(object2.GJKSupportFunction(Point2d.multiply(p, -1))));

                    if(!sameDirection(p, simplexVertices.getFirst())){
                        return new Collision(false);
                    } else if (checkTriangle(simplexVertices, p)) {
                        return new Collision(true);
                    }
                }
            }

            //deprecated
            public static boolean checkSimplex(ArrayList<Point2d> simplex, Vector2d p){
                return switch (simplex.size()){
                    case 2 -> checkLine(simplex, p);
                    case 3 -> checkTriangle(simplex, p);
                    default -> throw new InputMismatchException("simplex size of " + simplex.size() + " outside of expected range of [2,3]");
                };
            }

            //deprecated
            public static boolean checkLine(ArrayList<Point2d> simplex, Point2d p){

                Vector2d ab = new Vector2d(simplex.get(0), simplex.get(1));
                Vector2d ao = new Vector2d(simplex.get(0), new Point2d());

                p = Point2d.tripleProduct2d(ab, ao, ab);

                return false;

            }



            //deprecated
            public static Point2d supportFunction(PolygonHitbox object1, PolygonHitbox object2, Point2d p){

                int obj1VertexID = 0;
                int obj2VertexID = 0;

                double dotProduct;
                double obj1Max = 0;
                double obj2Max = 0;

                for (int i = 0; i < object1.getVerticies().length; i++){

                    dotProduct = Point2d.dotProduct(object1.getVerticies()[i], p);
                    if (obj1Max < dotProduct){
                        obj1Max = dotProduct;
                        obj1VertexID = i;
                    }

                }

                p.multiply(-1);

                for (int i = 1; i < object2.getVerticies().length; i++){

                    dotProduct = Point2d.dotProduct(object2.getVerticies()[i], p);
                    if(obj2Max > dotProduct){
                        obj2Max = dotProduct;
                        obj2VertexID = i;
                    }

                }

                try {
                    return Point2d.add(object1.getVerticies()[obj1VertexID], object2.getVerticies()[obj2VertexID]);
                } catch (Exception e){
                    throw new InputMismatchException("one or more polygons size out of range");
                }

            }

        }
    }*/
}
