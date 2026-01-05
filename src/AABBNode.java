public class AABBNode {

    public AABBNode parent;
    public AABBNode childLeft;
    public AABBNode childRight;

    public AABB AABB;
    public Hitbox object;

    public AABBNode(AABBNode childLeft, AABBNode childRight){

        this.childLeft = childLeft;
        this.childRight = childRight;
        AABB = AABB.fitBoundingBox(new Point2d[]{
                childLeft.AABB.getMinimumVertex(),
                childLeft.AABB.getMaximumVertex(),
                childRight.AABB.getMinimumVertex(),
                childRight.AABB.getMaximumVertex()
        }, 0);

    }

    public AABBNode(AABBNode parent, AABBNode childLeft, AABBNode childRight){

        this.parent = parent;
        this.childLeft = childLeft;
        this.childRight = childRight;
        AABB = AABB.fitBoundingBox(new Point2d[]{
                childLeft.AABB.getMinimumVertex(),
                childLeft.AABB.getMaximumVertex(),
                childRight.AABB.getMinimumVertex(),
                childRight.AABB.getMaximumVertex()
        }, 0);

    }

    public AABBNode(Hitbox object){

        this.object = object;
        AABB = object.fitAABB(Settings.Engine.AABB_FATTENING);

    }
}
