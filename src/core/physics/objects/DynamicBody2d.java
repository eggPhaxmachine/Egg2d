package core.physics.objects;

import core.physics.space.Vector2d;

public interface DynamicBody2d extends RigidBody2d {

    @Override
    default int getBodyType() {
        return RigidBody2d.DYNAMIC;
    }

    Vector2d getVelocity();
    void setVelocity(Vector2d velocity);

    float getAngularVelocity();
    void setAngularVelocity(float angularVelocity);

    Vector2d getCenter();

    void translate(Vector2d translation);
    void rotate(float theta);

}
