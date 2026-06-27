package physics.objects;

import physics.broadphase.BroadPhaseObject;
import physics.shapes.Shape2d;

public interface RigidBody2d extends BroadPhaseObject {

    int getId();

    int getBodyType();

    float getDensity();
    void setDensity(float density);

    float getMass();
    void setMass(float mass);

    float getInertialMoment();

    float getRestitution();


    int DYNAMIC = 0;
    int KINEMATIC = 1;
    int STATIC = 2;

}
