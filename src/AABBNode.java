public class AABBNode {

    public AABBNode parent;
    public AABBNode childLeft;
    public AABBNode childRight;

    public AABB aabb;
    public Hitbox object;

    public int id;

    public AABBNode(AABBNode childLeft, AABBNode childRight){

        this.childLeft = childLeft;
        this.childRight = childRight;
        aabb = AABB.fitBoundingBox(new Point2d[]{
                childLeft.aabb.getMinimumVertex(),
                childLeft.aabb.getMaximumVertex(),
                childRight.aabb.getMinimumVertex(),
                childRight.aabb.getMaximumVertex()
        }, 0);

    }

    public AABBNode(AABBNode parent, AABBNode childLeft, AABBNode childRight){

        this.parent = parent;
        this.childLeft = childLeft;
        this.childRight = childRight;
        aabb = AABB.fitBoundingBox(new Point2d[]{
                childLeft.aabb.getMinimumVertex(),
                childLeft.aabb.getMaximumVertex(),
                childRight.aabb.getMinimumVertex(),
                childRight.aabb.getMaximumVertex()
        }, 0);

    }

    public AABBNode(Hitbox object, int id){

        this.object = object;
        this.id = id;
        aabb = object.fitAABB(Settings.Engine.AABB_FATTENING);

    }
}
