package physics.narrowphase;

import physics.space.Vector2d;

public interface NarrowPhaseDetection {

    Vector2d check();

    Vector2d[] generateContacts(Vector2d collisionNormal, float penetration);

}
