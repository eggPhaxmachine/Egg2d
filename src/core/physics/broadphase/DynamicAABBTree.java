package core.physics.broadphase;

import core.physics.narrowphase.Collision;
import core.physics.shapes.Shape2d;
import core.physics.space.Vector2d;
import core.util.Stack;

import java.util.ArrayList;
import java.util.HashMap;

public class DynamicAABBTree {

    private AABBNode root;

    private final HashMap<Integer, AABBLeaf> objects = new HashMap<>();
    private final ArrayList<AABBLeaf> dynamics = new ArrayList<>();

    public void addDynamic(BroadPhaseObject object) {

        AABBLeaf node = new AABBLeaf(object, true);

        dynamics.add(node);
        node.aabb = object.fitAABB();

        ArrayList<AABBLeaf> results = queryNode(node.aabb);
        for (AABBLeaf result : results){

            long collisionId = ((long) object.getId() << 32) | result.object.getId();

            node.collisionsIds.add(collisionId);
            result.collisionsIds.add(collisionId);

            collisions.put(collisionId, new Collision((Shape2d) object, (Shape2d) result.object));

        }
        addLeaf(node);

    }
    public void addConstant(BroadPhaseObject object) {

        AABBLeaf node = new AABBLeaf(object, false);

        node.aabb = node.object.fitAABB();

        ArrayList<AABBLeaf> results = queryNode(node.aabb);
        for (AABBLeaf result : results){

            if (!result.dynamic) continue;

            long collisionId = ((long) object.getId() << 32) | result.object.getId();

            node.collisionsIds.add(collisionId);
            result.collisionsIds.add(collisionId);

            collisions.put(collisionId, new Collision((Shape2d) object, (Shape2d) result.object));

        }
        addLeaf(node);

    }

    private final HashMap<Long, Collision> collisions = new HashMap<>();
    public Collision[] getCollisions() {
        return collisions.values().toArray(Collision[]::new);
    }

    public void update(){

        for(AABBLeaf node : dynamics){
            if(!node.object.AABBCheck(node.aabb)){

                for (long collisionId : node.collisionsIds){

                    collisions.remove(collisionId);

                    int id1 = (int) (collisionId >> 32);
                    int id2 = (int) collisionId;

                    if (id1 == node.object.getId()){
                        objects.get(id2).collisionsIds.remove(collisionId);
                    } else {
                        objects.get(id1).collisionsIds.remove(collisionId);
                    }

                }
                node.collisionsIds.clear();

                removeLeaf(node);
                node.aabb = node.object.fitAABB();

                ArrayList<AABBLeaf> results = queryNode(node.aabb);
                for (AABBLeaf result : results){

                    long collisionId = ((long) node.object.getId() << 32) | result.object.getId();

                    node.collisionsIds.add(collisionId);
                    result.collisionsIds.add(collisionId);

                    collisions.put(collisionId, new Collision((Shape2d) node.object, (Shape2d) result.object));

                }

                addLeaf(node);

            }
        }
    }

    public BroadPhaseObject[] query(AABB aabb){

        ArrayList<AABBLeaf> results = queryNode(aabb);
        BroadPhaseObject[] output = new BroadPhaseObject[results.size()];

        for (int i = 0; i < results.size(); i++) {
            output[i] = results.get(i).object;
        }

        return output;

    }


    private void addLeaf(AABBLeaf leaf){

        objects.put(leaf.object.getId(), leaf);

        if(root == null){
            root = leaf;
            return;
        }

        AABBNode curNode = root;

        AABB costLeft = AABB.fitBoundingBox(new Vector2d[]{
                curNode.aabb.getMinimumVertex(),
                curNode.aabb.getMaximumVertex(),
                leaf.aabb.getMinimumVertex(),
                leaf.aabb.getMaximumVertex()
        }, 0);;
        AABB costRight;

        while (curNode.childLeft != null){

             curNode.aabb = costLeft;

             costLeft = AABB.fitBoundingBox(new Vector2d[]{
                     curNode.childLeft.aabb.getMinimumVertex(),
                     curNode.childLeft.aabb.getMaximumVertex(),
                     leaf.aabb.getMinimumVertex(),
                     leaf.aabb.getMaximumVertex()
             }, 0);

             costRight = AABB.fitBoundingBox(new Vector2d[]{
                     curNode.childRight.aabb.getMinimumVertex(),
                     curNode.childRight.aabb.getMaximumVertex(),
                     leaf.aabb.getMinimumVertex(),
                     leaf.aabb.getMaximumVertex()
             }, 0);

            if (costLeft.getSAHCost() - curNode.childLeft.aabb.getSAHCost() >  costRight.getSAHCost() - curNode.childRight.aabb.getSAHCost()){
                curNode = curNode.childRight;
                costLeft = costRight;
            } else {
                curNode = curNode.childLeft;
            }

        }

        if (curNode == root){
            root = new AABBNode(leaf, root);
            root.aabb = AABB.fitBoundingBox(new Vector2d[]{
                    curNode.aabb.getMinimumVertex(),
                    curNode.aabb.getMaximumVertex(),
                    leaf.aabb.getMinimumVertex(),
                    leaf.aabb.getMaximumVertex()
            }, 0);
            return;
        }

        if (curNode == curNode.parent.childLeft) curNode.parent.setChildLeft(new AABBNode(leaf, curNode));
        else curNode.parent.setChildRight(new AABBNode(leaf, curNode));
        leaf.parent.aabb = costLeft;

    }

    public void removeLeaf(AABBLeaf leaf){

        objects.remove(leaf.object.getId());

        if (leaf == root){

            root = null;

        } else if (leaf.parent == root) {

            if (leaf.parent.childLeft == leaf){
                root = leaf.parent.childRight;
            } else {
                root = leaf.parent.childLeft;
            }
            root.parent = null;

        } else {

            AABBNode curNode;

            if (leaf == leaf.parent.childLeft) {
                curNode = leaf.parent.childRight;
            } else {
                curNode = leaf.parent.childLeft;
            }

            if (leaf.parent == leaf.parent.parent.childLeft) {
                leaf.parent.parent.setChildLeft(curNode);
            } else {
                leaf.parent.parent.setChildRight(curNode);
            }

            while (curNode.parent != null) {

                curNode = curNode.parent;

                curNode.aabb = AABB.fitBoundingBox(new Vector2d[]{
                        curNode.childRight.aabb.getMinimumVertex(),
                        curNode.childRight.aabb.getMaximumVertex(),
                        curNode.childLeft.aabb.getMinimumVertex(),
                        curNode.childLeft.aabb.getMaximumVertex()
                }, 0);

            }
        }
    }

    private ArrayList<AABBLeaf> queryNode(AABB aabb){

        ArrayList<AABBLeaf> results = new ArrayList<>();

        if (root == null) return new ArrayList<>();

        AABBNode curNode;
        Stack<AABBNode> queryStack = new Stack<>();
        queryStack.add(root);

        while (!queryStack.isEmpty()){

            curNode = queryStack.pop();

            if(AABB.AABBCheck(aabb, curNode.aabb)){

                if(curNode.childLeft == null){

                    results.add((AABBLeaf) curNode);

                } else {
                    queryStack.add(curNode.childLeft);
                    queryStack.add(curNode.childRight);
                }

            }
        }

        return results;

    }
}