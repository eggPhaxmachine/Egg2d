package core.physics.broadphase;

public class AABBNode {

    public AABBNode parent;
    public AABBNode childLeft;
    public AABBNode childRight;

    public AABB aabb;

    public AABBNode(){
        this.childLeft = null;
        this.childRight = null;
    }

    public AABBNode(AABBNode childLeft, AABBNode childRight){
        setChildLeft(childLeft);
        setChildRight(childRight);
    }

    public void setChildLeft(AABBNode newChild){
        newChild.parent = this;
        childLeft = newChild;
    }

    public void setChildRight(AABBNode newChild){
        newChild.parent = this;
        childRight = newChild;
    }

}
