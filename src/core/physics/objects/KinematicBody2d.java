package core.physics.objects;

public interface KinematicBody2d extends DynamicBody2d {

    @Override
    default int getBodyType() {
        return RigidBody2d.KINEMATIC;
    }

    @Override
    default float getDensity() {
        return Float.POSITIVE_INFINITY;
    }
    @Override
    default void setDensity(float density) {
        throw new UnsupportedOperationException("Static Bodies cannot change density");
    }

    @Override
    default float getMass() {
        return Float.POSITIVE_INFINITY;
    }
    @Override
    default void setMass(float mass) {
        throw new UnsupportedOperationException("Static Bodies cannot change mass");
    }

    @Override
    default float getInertialMoment() {
        return Float.POSITIVE_INFINITY;
    }

}
