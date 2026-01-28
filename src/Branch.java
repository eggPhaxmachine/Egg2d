public class Branch {

    //Deprecated

    /*
    private AABB boundingBox;
    public AABB getBoundingBox() { return boundingBox; }

    private int numObjects;
    private Hitbox[] objects;

    private int splitNum;
    public Branch[] children;

    public LinkedList<Collision> collisions = new LinkedList<>();


    public Branch(Point2d maximumVertex, Point2d minimumVertex, Hitbox[] objects, int splitNum){

        this.objects = checkObjects(objects);

        this.splitNum = splitNum;

        if (this.splitNum < Settings.Engine.MAX_SPLIT && objects.length > Settings.Engine.TARGET_OBJECTS){
            children = split();
        } else {

            for(int i = 0; i < objects.length; i++){
                for(int j = i + 1; j < objects.length; j++){
                    if (Tools.CollisionDetection.AABBCheck(objects[i].getBoundingBox(), objects[j].getBoundingBox())){
                        
                    }
                }
            }
        }
    }


    private Hitbox[] checkObjects(Hitbox[] objects){

        Hitbox[] correctObjects;

        for(Hitbox object : objects){
            if (Tools.CollisionDetection.AABBCheck(object.getBoundingBox(), boundingBox)){
                numObjects++;
                object = null;
            }
        }

        correctObjects = new Hitbox[numObjects];

        for(Hitbox object : objects){

            int i = 0;

            if (object != null){
                correctObjects[i] = object;
                i++;
            }
        }

        return correctObjects;
    }


    public Branch[] split(){

        Point2d minimumVertex = boundingBox.getMinimumVertex();
        double sizeX = boundingBox.getSizeX();
        double sizeY = boundingBox.getSizeY();

        return new Branch[] {
            new Branch(minimumVertex,  new Point2d(minimumVertex.getX() + sizeX/2, minimumVertex.getY() + sizeY/2), objects, splitNum++),
            new Branch(new Point2d(minimumVertex.getX() + sizeX/2 , minimumVertex.getY()), new Point2d(minimumVertex.getX() + sizeX, minimumVertex.getY() + sizeY/2), objects, splitNum++),
            new Branch(new Point2d(minimumVertex.getX() + sizeX/2, minimumVertex.getY() + sizeY/2), new Point2d(minimumVertex.getX() + sizeX, minimumVertex.getY() + sizeY), objects, splitNum++),
            new Branch(new Point2d(minimumVertex.getX(), minimumVertex.getY() + sizeY/2), new Point2d(minimumVertex.getX() + sizeX/2, minimumVertex.getY() +sizeY), objects, splitNum++)
        };

    }
     */
}
