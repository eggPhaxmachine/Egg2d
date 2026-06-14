package core.physics.broadphase;

import java.util.HashSet;

public class AABBLeaf extends AABBNode {

    public final BroadPhaseObject object;
    public HashSet<Long> collisionsIds;

    boolean dynamic;

    public AABBLeaf(BroadPhaseObject object, boolean dynamic){

        this.object = object;
        this.dynamic = dynamic;

        if (dynamic) collisionsIds = new HashSet<>(4);
        this.aabb = object.fitAABB();

    }

}