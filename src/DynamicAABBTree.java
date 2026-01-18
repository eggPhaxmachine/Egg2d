import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class DynamicAABBTree implements Renderable {

    AABBNode tree;

    private ArrayList<AABBNode> objects = new ArrayList<>();
    private ArrayList<AABBNode> dynamicObjects = new ArrayList<>();
    private ArrayList<AABBNode> constantObjects = new ArrayList<>();

    public Hitbox[] getObjects(){
        Hitbox[] objects = new Hitbox[this.objects.size()];
        for (int i = 0; i < this.objects.size(); i++){
            objects[i] = this.objects.get(i).object;
        }
        return objects;
    }
    public Hitbox[] getDynamicObjects(){
        Hitbox[] dynamicObjects = new Hitbox[this.dynamicObjects.size()];
        for (int i = 0; i < this.dynamicObjects.size(); i++){
            dynamicObjects[i] = this.dynamicObjects.get(i).object;
        }
        return dynamicObjects;
    }
    public Hitbox[] getConstantObjects(){
        Hitbox[] constantObjects = new Hitbox[this.constantObjects.size()];
        for (int i = 0; i < this.constantObjects.size(); i++){
            constantObjects[i] = this.constantObjects.get(i).object;
        }
        return constantObjects;
    }
    public void addObject(Hitbox object){
        AABBNode objectNode = new AABBNode(object, objects.size());
        objects.add(objectNode);
        if(object.isUpdatable()) {
            dynamicObjects.add(objectNode);
        } else {
            constantObjects.add(objectNode);
        }
        addLeaf(objectNode);
    }

    public DynamicAABBTree(){

    }

    @Override
    public void update(){

        for(Hitbox object : getDynamicObjects()){
            object.update();
        }

        ArrayList<Collision> collisionCandidates = new ArrayList<>();

        for(AABBNode node : dynamicObjects){

            Hitbox object = node.object;

            if(!object.AABBCheck(node.AABB)){
                continue;
            }

            node.AABB = object.fitAABB(Settings.Engine.AABB_FATTENING);
            removeLeaf(node);
            queryNode(node);
            addLeaf(node);

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

        queryStack.addFirst(tree);
        AABBNode curNode;

        ArrayList<Collision> candidates = new ArrayList<>();

        while (!queryStack.isEmpty()){

            curNode = queryStack.getFirst();
            queryStack.removeFirst();

            if(AABB.AABBCheck(aabb.AABB, curNode.AABB)){
                if(curNode.childLeft != null){
                    new Collision(curNode.object, aabb.object);
                } else {
                    queryStack.addFirst(curNode.childLeft);
                    queryStack.addFirst(curNode.childRight);
                }
            }

        }

        return candidates.toArray(Collision[]::new);

    }

    @Override
    public void render(Graphics2D renderer) {

        for(Hitbox object : getDynamicObjects()){
            object.render(renderer);
        }

        for (Hitbox object : getConstantObjects()){
            object.render(renderer);
        }

    }

}
