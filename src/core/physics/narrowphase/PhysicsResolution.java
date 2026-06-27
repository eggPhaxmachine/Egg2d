package physics.narrowphase;

import physics.space.Vector2d;

public interface PhysicsResolution {

    void correct(Vector2d[] contacts, Vector2d collisionNormal, float penetration);

}
