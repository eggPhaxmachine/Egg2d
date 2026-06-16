package core.physics.narrowphase;

import core.physics.space.Vector2d;

public interface NarrowPhaseDetection {

    Vector2d check();

    Vector2d[] generateContacts(Vector2d collisionNormal, float penetration);

}
