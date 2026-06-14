package core.physics.shapes;

import core.physics.space.Vector2d;

import java.util.InputMismatchException;

public class Polygon2d extends Shape2d {

    protected final Vector2d[] vertices;
    public Vector2d[] getVertices() {
        return vertices;
    }

    protected final int[] indecies;
    public int[] getIndecies() {
        return indecies;
    }

    protected final int numVertices;
    public int getNumVertices(){ return numVertices; }

    public Polygon2d(int id, Vector2d[] vertices){

        super(id, Shape2d.POLYGON);

        this.vertices = vertices;
        numVertices = vertices.length;

        if (numVertices < 3) throw new InputMismatchException("At least 3 vertices needed for polygon construction");
        
        float sumX = 0;
        float sumY = 0;
        for(Vector2d vertex : vertices){
            sumX += vertex.getX();
            sumY += vertex.getY();
        }

        center = new Vector2d(sumX/vertices.length, sumY/vertices.length);

        Vector2d p1 = vertices[0], p2, p3;
        indecies = new int[(numVertices - 2) * 3];
        for (int i = 0; i < numVertices - 2; i++) {

            indecies[i*3] = 0;
            indecies[i*3+1] = i+1;
            indecies[i*3+2] = i+2;

            p2 = vertices[i+1];
            p3 = vertices[i+2];
            area += 0.5f * Math.abs(p1.x*(p2.y - p3.y) + p2.x*(p3.y - p1.y) + p3.x*(p1.y - p2.y));

        }

    }

    public Vector2d GJKSupportFunction(Vector2d d) {

        int vertexID = 0;
        double dotProduct;
        double max = Vector2d.dot(getVertices()[0], d);

        for (int i = 1; i < getVertices().length; i++){

            dotProduct = Vector2d.dot(getVertices()[i], d);
            if (max < dotProduct){
                max = dotProduct;
                vertexID = i;
            }

        }

        return getVertices()[vertexID];

    }
}
