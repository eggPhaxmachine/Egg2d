package physics.broadphase;

import physics.narrowphase.Collision;
import physics.shapes.Shape2d;

import java.util.ArrayList;

public class BruteForceDetection extends BroadPhaseDetection {

    private final ArrayList<Element> dynamics = new ArrayList<>();
    private final ArrayList<Element> constants = new ArrayList<>();

    @Override
    public void addDynamic(BroadPhaseObject object) {
        Element temp = new Element(object);
        temp.aabb = object.fitAABB();
        dynamics.add(temp);
    }
    @Override
    public void addConstant(BroadPhaseObject object) {
        Element temp = new Element(object);
        temp.aabb = object.fitAABB();
        constants.add(temp);
    }

    @Override
    public void update() {
        for (Element element : dynamics){
            if (!element.object.AABBCheck(element.aabb)){
                element.aabb = element.object.fitAABB();
            }
        }
    }

    @Override
    public Collision[] getCollisions() {

        ArrayList<Collision> candidates = new ArrayList<>();

        for (int i = 0; i < dynamics.size(); i++) {
            for (int j = i + 1; j < dynamics.size(); j++) {
                if (AABB.AABBCheck(dynamics.get(i).aabb, dynamics.get(j).aabb)){
                    candidates.add(new Collision((Shape2d) dynamics.get(i).object, (Shape2d) dynamics.get(j).object));
                }
            }
            for (Element element : constants){
                if (AABB.AABBCheck(dynamics.get(i).aabb, element.aabb)){
                    candidates.add(new Collision((Shape2d) dynamics.get(i).object, (Shape2d) element.object));
                }
            }
        }

        return candidates.toArray(Collision[]::new);

    }

    @Override
    public BroadPhaseObject[] query(AABB aabb) {
        return new BroadPhaseObject[0];
    }

    private class Element {

        public final BroadPhaseObject object;
        public AABB aabb;

        public Element(BroadPhaseObject object) {
            this.object = object;
        }
    }

}
