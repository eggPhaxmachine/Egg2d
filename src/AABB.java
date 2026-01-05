public class AABB {

    private Point2d minimumVertex;
    public Point2d getMinimumVertex() { return minimumVertex; }
    private Point2d maximumVertex;
    public Point2d getMaximumVertex() { return maximumVertex; }


    private double sizeX;
    public double getSizeX() { return sizeX; }
    private double sizeY;
    public double getSizeY() { return sizeY; }


    public AABB(Point2d minimumVertex, Point2d maximumVertex){

        this.minimumVertex = minimumVertex;
        this.maximumVertex = maximumVertex;

        this.sizeX = maximumVertex.getX() - minimumVertex.getX();
        this.sizeY = maximumVertex.getY() - minimumVertex.getY();

    }

    public AABB(Point2d location, double sizeX, double sizeY){

        this.minimumVertex = location;
        this.maximumVertex = new Point2d(sizeX + location.getX(), sizeY + location.getY());

        this.sizeX = sizeX;
        this.sizeY = sizeY;

    }


    public double getSAHCost(){
        return 2 * (sizeX + sizeY);
    }

    public void translate(Vector2d translation){
        minimumVertex.translate(translation);
        maximumVertex.translate(translation);
    }


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

    public static boolean AABBCheck(Point2d[] object1, AABB object2) {

        Point2d minimumVertex2 = object2.getMinimumVertex();;
        Point2d maximumVertex2 = object2.getMaximumVertex();;

        for(Point2d point : object1) {
            if(
                    Tools.between(point.getX(), maximumVertex2.getX(), minimumVertex2.getX()) ||
                    Tools.between(point.getX(), maximumVertex2.getX(), minimumVertex2.getX()) &&
                    Tools.between(point.getY(), maximumVertex2.getY(), minimumVertex2.getX()) ||
                    Tools.between(point.getY(), maximumVertex2.getY(), minimumVertex2.getY())
            ){
                return true;
            }
        }
        return false;
    }

    public static AABB fitBoundingBox(Point2d[] points, double fattening){

        double maxX = points[0].getX();
        double minX = points[0].getX();
        double maxY = points[0].getY();
        double minY = points[0].getY();

        for (int i = 1; i < points.length; i++){

            if(points[i].getX() > maxX){

                maxX = points[i].getX();

            } else if (points[i].getX() < minX) {

                minX = points[i].getX();

            }
            if (points[i].getY() > maxY){

                maxY = points[i].getY();

            } else if ( points[i].getY() < minY) {

                minY = points[i].getY();

            }
        }

        return new AABB(new Point2d(minX - fattening, minY - fattening), new Point2d(maxX + fattening, maxY + fattening));

    }
}
