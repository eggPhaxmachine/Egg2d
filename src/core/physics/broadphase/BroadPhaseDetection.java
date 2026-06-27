package physics.broadphase;

import physics.narrowphase.Collision;
import physics.objects.DynamicBody2d;
import physics.objects.StaticBody2d;

public abstract class BroadPhaseDetection{

    public abstract void update();

    public abstract Collision[] getCollisions();

    abstract BroadPhaseObject[] query(AABB aabb);

    public abstract void addDynamic(BroadPhaseObject object);

    public abstract void addConstant(BroadPhaseObject object);

}
