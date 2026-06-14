package core;

public class Tools {

    public static boolean between(double value, double min, double max) {

        boolean thingy = value >= min && value <= max;
        return thingy;

    }

    public static void realSleep(long millisecond) {
        try {
            Thread.sleep(millisecond);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(Thread.currentThread().getName() + " interrupted, ignoring " + e.getMessage());
        }
    }

    public static int rand(int min, int max){
        return (int)(Math.random() * (max - min + 1)) + min;
    }

    public static double rand(double min, double max){
        return (Math.random() * (max - min + 1)) + min;
    }

/*
    public static class CollisionDetection {

        public static boolean AABBCheck(core.physics.broadphase.AABB object1, core.physics.broadphase.AABB object2) {

            core.physics.space.Vector2d minimumVertex1 = object1.getMinimumVertex();
            core.physics.space.Vector2d maximumVertex1 = object1.getMinimumVertex();
            core.physics.space.Vector2d minimumVertex2 = object2.getMinimumVertex();
            core.physics.space.Vector2d maximumVertex2 = object2.getMaximumVertex();

            return (core.Tools.between(minimumVertex1.getX(), maximumVertex2.getX(), minimumVertex2.getX()) ||
                    core.Tools.between(maximumVertex1.getX(), maximumVertex2.getX(), minimumVertex2.getX()) &&
                    core.Tools.between(minimumVertex1.getY(), maximumVertex2.getY(), minimumVertex2.getX()) ||
                    core.Tools.between(maximumVertex1.getY(), maximumVertex2.getY(), minimumVertex2.getY()));
        }

        public static boolean AABBCheck(core.physics.space.Vector2d object1, core.physics.broadphase.AABB object2) {

            core.physics.space.Vector2d minimumVertex2 = object2.getMinimumVertex();
            core.physics.space.Vector2d maximumVertex2 = object2.getMaximumVertex();

            return (core.Tools.between(object1.getX(), maximumVertex2.getX(), minimumVertex2.getX()) ||
                    core.Tools.between(object1.getX(), maximumVertex2.getX(), minimumVertex2.getX()) &&
                            core.Tools.between(object1.getY(), maximumVertex2.getY(), minimumVertex2.getX()) ||
                    core.Tools.between(object1.getY(), maximumVertex2.getY(), minimumVertex2.getY()));
        }


        public static class GJKAlgorithm {

            public static final core.physics.space.Vector2d INITIAL_VECTOR = new core.physics.space.Vector2d(1,0);

            public static core.physics.narrowphase.PPCollision NarrowDetection(core.physics.shapes.Polygon2d object1, core.physics.shapes.Polygon2d object2) {

                ArrayList<core.physics.space.Vector2d> simplexVertices = new ArrayList<>(3);
                core.physics.space.Vector2d p = INITIAL_VECTOR;

                simplexVertices.addFirst(object1.GJKSupportFunction(p).add(object2.GJKSupportFunction(core.physics.space.Vector2d.multiply(p, -1))));

                p = new core.physics.space.Vector2d(simplexVertices.getFirst(), new core.physics.space.Vector2d());

                simplexVertices.addFirst(object1.GJKSupportFunction(p).add(object2.GJKSupportFunction(core.physics.space.Vector2d.multiply(p, -1))));

                core.physics.space.Vector2d ab = new core.physics.space.Vector2d(simplexVertices.get(0), simplexVertices.get(1));
                p = new core.physics.space.Vector2d(core.physics.space.Vector2d.tripleProduct2d(ab, new core.physics.space.Vector2d(simplexVertices.getFirst(), new core.physics.space.Vector2d()), ab));

                while(true) {

                    simplexVertices.addFirst(object1.GJKSupportFunction(p).add(object2.GJKSupportFunction(core.physics.space.Vector2d.multiply(p, -1))));

                    if(!sameDirection(p, simplexVertices.getFirst())){
                        return new core.physics.narrowphase.PPCollision(false);
                    } else if (checkTriangle(simplexVertices, p)) {
                        return new core.physics.narrowphase.PPCollision(true);
                    }
                }
            }

            //deprecated
            public static boolean checkSimplex(ArrayList<core.physics.space.Vector2d> simplex, core.physics.space.Vector2d p){
                return switch (simplex.size()){
                    case 2 -> checkLine(simplex, p);
                    case 3 -> checkTriangle(simplex, p);
                    default -> throw new InputMismatchException("simplex size of " + simplex.size() + " outside of expected range of [2,3]");
                };
            }

            //deprecated
            public static boolean checkLine(ArrayList<core.physics.space.Vector2d> simplex, core.physics.space.Vector2d p){

                core.physics.space.Vector2d ab = new core.physics.space.Vector2d(simplex.get(0), simplex.get(1));
                core.physics.space.Vector2d ao = new core.physics.space.Vector2d(simplex.get(0), new core.physics.space.Vector2d());

                p = core.physics.space.Vector2d.tripleProduct2d(ab, ao, ab);

                return false;

            }



            //deprecated
            public static core.physics.space.Vector2d supportFunction(core.physics.shapes.Polygon2d object1, core.physics.shapes.Polygon2d object2, core.physics.space.Vector2d p){

                int obj1VertexID = 0;
                int obj2VertexID = 0;

                double dot;
                double obj1Max = 0;
                double obj2Max = 0;

                for (int i = 0; i < object1.getVerticies().length; i++){

                    dot = core.physics.space.Vector2d.dot(object1.getVerticies()[i], p);
                    if (obj1Max < dot){
                        obj1Max = dot;
                        obj1VertexID = i;
                    }

                }

                p.multiply(-1);

                for (int i = 1; i < object2.getVerticies().length; i++){

                    dot = core.physics.space.Vector2d.dot(object2.getVerticies()[i], p);
                    if(obj2Max > dot){
                        obj2Max = dot;
                        obj2VertexID = i;
                    }

                }

                try {
                    return core.physics.space.Vector2d.add(object1.getVerticies()[obj1VertexID], object2.getVerticies()[obj2VertexID]);
                } catch (Exception e){
                    throw new InputMismatchException("one or more polygons size out of range");
                }

            }

        }
    }*/
}
