import java.awt.*;

public class PolygonHitbox extends Hitbox {

    private Point2d[] rawVerticies;
    public Vector2d updateVector = new Vector2d();
    public Point2d[] getVertices()
    {
        if(updateVector.getX() != 0 || updateVector.getY() != 0){
            for(Point2d vertex : rawVerticies){
                vertex.translate(updateVector);
            }
        }
        updateVector.multiply(0);
        return rawVerticies;
    }

    private int numVertices;
    public int getNumVertices(){ return numVertices; }

    //private AABB boundingBox;
    //@Override
    //public AABB getBoundingBox() { return boundingBox; }

    //private Point2d location;
    //@Override
    //public Point2d getLocation() { return location; }

    private int[][] intVertices;
    public int[][] getIntVertices(){

        Point2d[] vertices = getVertices();

        for(int i = 0; i < numVertices; i++){
            intVertices[0][i] = (int) (vertices[i].getX() + 0.5);
            intVertices[1][i] = (int) (vertices[i].getY() + 0.5);
        }
        return intVertices;
    }


    public PolygonHitbox(Point2d[] vertices, boolean updatable, Color color){

        super(HitboxType.POLYGON, updatable, color);

        this.rawVerticies = vertices;
        numVertices = vertices.length;
        //this.boundingBox = AABB.fitBoundingBox(vertices, updatable? Settings.Engine.AABB_FATTENING : 0);
        this.intVertices = new int[2][numVertices];

    }

    public PolygonHitbox(Point2d[] vertices, boolean updatable){
        this(vertices, updatable, Color.BLACK);
    }

    @Override
    public void render(Graphics2D renderer) {
        renderer.setColor(color);
        int[][] vertices = getIntVertices();
        renderer.drawPolygon(vertices[0], vertices[1], getNumVertices());
    }

    @Override
    public AABB fitAABB(double fattening) {
        return AABB.fitBoundingBox(getVertices(), fattening);
    }

    @Override
    public boolean AABBCheck(AABB aabb) {
        return AABB.AABBCheck(getVertices(), aabb);
    }

    public void translate(Vector2d translation){
        //location.translate(translation);
        //boundingBox.translate(translation);
        updateVector.translate(translation);
    }

    @Override
    public Point2d GJKSupportFunction(Point2d d) {

        int vertexID = 0;
        double dotProduct;
        double max = 0;

        for (int i = 0; i < getVertices().length; i++){

            dotProduct = Point2d.dotProduct(getVertices()[i], d);
            if (max < dotProduct){
                max = dotProduct;
                vertexID = i;
            }

        }

        return getVertices()[vertexID];

    }
}
