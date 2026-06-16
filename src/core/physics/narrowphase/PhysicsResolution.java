package core.physics.narrowphase;

import core.physics.space.Vector2d;

public interface PhysicsResolution {

    void correct(Vector2d[] contacts, Vector2d collisionNormal, float penetration);

}
