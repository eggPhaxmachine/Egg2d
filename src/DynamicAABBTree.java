import java.util.ArrayList;
import java.util.LinkedList;

public class DynamicAABBTree {

    AABBNode tree;

    private ArrayList<AABBNode> dynamicObjects = new ArrayList<>();
    private ArrayList<AABBNode> constantObjects = new ArrayList<>();

    public AABBNode[] getDynamicObjects(){
        return dynamicObjects.toArray(AABBNode[]::new);
    }
    public AABBNode[] getConstantObjects(){
        return constantObjects.toArray(AABBNode[]::new);
    }
    public void addObject(Hitbox object){

        AABBNode objectNode = new AABBNode(object);

        if(object.isUpdatable()) {
            dynamicObjects.add(objectNode);
        } else {
            constantObjects.add(objectNode);
        }
    }

    public DynamicAABBTree(){

    }

    public void update(){

        Collision[] collisionCandidates;

        for(AABBNode node : dynamicObjects){

            Hitbox object = node.object;

            if(!object.AABBCheck(node.AABB)){
                continue;
            }

            node.AABB = object.fitAABB(Settings.Engine.AABB_FATTENING);
            removeLeaf(node);
            addLeaf(node);

            collisionCandidates = queryNode(node);



        }

    }

    private void addLeaf(AABBNode leaf){

        if(tree == null){
            tree = new AABBNode(null, leaf);
            return;
        }

        AABBNode curNode = tree;

        AABB costLeft;
        AABB costRight;

        while(true){

            try{

                costLeft = AABB.fitBoundingBox(new Point2d[]{
                        curNode.childLeft.AABB.getMinimumVertex(),
                        curNode.childLeft.AABB.getMaximumVertex(),
                        leaf.AABB.getMinimumVertex(),
                        leaf.AABB.getMaximumVertex()
                }, 0);

                costRight = AABB.fitBoundingBox(new Point2d[]{
                        curNode.childRight.AABB.getMinimumVertex(),
                        curNode.childRight.AABB.getMaximumVertex(),
                        leaf.AABB.getMinimumVertex(),
                        leaf.AABB.getMaximumVertex()
                }, 0);

                if (costLeft.getSAHCost() > costRight.getSAHCost()){
                    curNode.AABB = costRight;
                    curNode = curNode.childRight;
                } else {
                    curNode.AABB = costLeft;
                    curNode = curNode.childLeft;
                }

            } catch (NullPointerException e){

                curNode.parent = new AABBNode(curNode.parent, curNode, leaf);
                leaf.parent = curNode.parent;
                return;

            }
        }
    }

    private void removeLeaf(AABBNode leaf){

        AABBNode curNode = leaf.parent;
        if (curNode.childLeft == leaf){
            curNode.AABB = curNode.childRight.AABB;
            curNode.object = curNode.childRight.object;
        } else {
            curNode.AABB = curNode.childRight.AABB;
            curNode.object = curNode.childRight.object;
        }

        curNode.childLeft = null;
        curNode.childRight = null;

        curNode = curNode.parent;
        while (true){

            curNode.AABB = AABB.fitBoundingBox(new Point2d[]{
                    curNode.childRight.AABB.getMinimumVertex(),
                    curNode.childRight.AABB.getMaximumVertex(),
                    curNode.childLeft.AABB.getMinimumVertex(),
                    curNode.childLeft.AABB.getMaximumVertex()
            }, 0);

            try {
                curNode = curNode.parent;
            } catch (NullPointerException e) {
                return;
            }

        }

    }

    private LinkedList<AABBNode> queryStack = new LinkedList<>();
    public Collision[] queryNode(AABBNode aabb){

        queryStack.add(tree);
        AABBNode curNode;

        ArrayList<Collision> candidates = new ArrayList<>();

        while (!queryStack.isEmpty()){

            curNode = queryStack.getFirst();
            queryStack.removeFirst();

            if(AABB.AABBCheck(aabb, curNode.AABB)){
                if(curNode.childLeft != null){
                    candidates.add(new Collision(curNode.object, aabb.object));
                } else {
                    queryStack.add(curNode.childLeft);
                    queryStack.add(curNode.childRight);
                }
            }

        }

        return candidates.toArray(Collision[]::new);

    }
}
