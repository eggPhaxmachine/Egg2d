package core.physics.broadphase;

import core.Tools;
import core.physics.space.Vector2d;

public class AABB {

    private Vector2d minimumVertex;
    public Vector2d getMinimumVertex() { return minimumVertex; }
    private Vector2d maximumVertex;
    public Vector2d getMaximumVertex() { return maximumVertex; }


    private float sizeX;
    public float getSizeX() { return sizeX; }
    private float sizeY;
    public float getSizeY() { return sizeY; }


    public AABB(Vector2d minimumVertex, Vector2d maximumVertex){

        this.minimumVertex = minimumVertex;
        this.maximumVertex = maximumVertex;

        this.sizeX = maximumVertex.getX() - minimumVertex.getX();
        this.sizeY = maximumVertex.getY() - minimumVertex.getY();

    }

    public AABB(Vector2d location, float sizeX, float sizeY){

        this.minimumVertex = location;
        this.maximumVertex = new Vector2d(sizeX + location.getX(), sizeY + location.getY());

        this.sizeX = sizeX;
        this.sizeY = sizeY;

    }


    public float getSAHCost(){
        return 2 * (sizeX + sizeY);
    }

    public static boolean AABBCheck(AABB object1, AABB object2) {

        Vector2d minimumVertex1 = object1.getMinimumVertex();
        Vector2d maximumVertex1 = object1.getMaximumVertex();
        Vector2d minimumVertex2 = object2.getMinimumVertex();
        Vector2d maximumVertex2 = object2.getMaximumVertex();

        return ((Tools.between(minimumVertex1.getX(), minimumVertex2.getX(), maximumVertex2.getX()) ||
                Tools.between(maximumVertex1.getX(), minimumVertex2.getX(), maximumVertex2.getX())) &&
                (Tools.between(minimumVertex1.getY(), minimumVertex2.getY(), maximumVertex2.getY()) ||
                Tools.between(maximumVertex1.getY(), minimumVertex2.getY(), maximumVertex2.getY())));
    }

    public static boolean AABBCheck(Vector2d[] object1, AABB object2) {

        Vector2d minimumVertex2 = object2.getMinimumVertex();;
        Vector2d maximumVertex2 = object2.getMaximumVertex();;

        for(Vector2d point : object1) {
            if(!(Tools.between(point.getX(), minimumVertex2.getX(), maximumVertex2.getX()) && Tools.between(point.getY(), minimumVertex2.getY(), maximumVertex2.getY()))){
                return false;
            }
        }
        return true;
    }

    public static AABB fitBoundingBox(Vector2d[] points, float fattening){

        float maxX = points[0].getX();
        float minX = points[0].getX();
        float maxY = points[0].getY();
        float minY = points[0].getY();

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

        return new AABB(new Vector2d(minX - fattening, minY - fattening), new Vector2d(maxX + fattening, maxY + fattening));

    }
}
