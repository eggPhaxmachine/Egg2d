package physics.broadphase;

public interface BroadPhaseObject {

    int getId();

    boolean AABBCheck(AABB aabb);
    AABB fitAABB();

}
