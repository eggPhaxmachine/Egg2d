package physics.narrowphase;

import physics.objects.DynamicBody2d;
import physics.objects.StaticBody2d;
import physics.space.Vector2d;

public class DSPhysicsResolution implements PhysicsResolution{

    private final DynamicBody2d dynamicBody;
    private final StaticBody2d staticBody;
    public DSPhysicsResolution(DynamicBody2d dynamicBody, StaticBody2d staticBody) {
        this.dynamicBody = dynamicBody;
        this.staticBody = staticBody;
    }

    @Override
    public void correct(Vector2d[] contacts, Vector2d collisionNormal, float penetration) {

        float restitution = Math.min(dynamicBody.getRestitution(), staticBody.getRestitution());

        for (Vector2d contact : contacts) {

            Vector2d contactA = contact.copy().subtract(dynamicBody.getCenter());

            Vector2d relV = contactA.copy().perpendicular().multiply(dynamicBody.getAngularVelocity()).add(dynamicBody.getVelocity()).multiply(-1);

            float temp = relV.dot(collisionNormal);
            if (temp > 0) break;

            float numerator = relV.multiply(-(1 + restitution)).dot(collisionNormal);

            float denominator = 1 / dynamicBody.getMass();
            denominator += (float) Math.pow(contactA.cross(collisionNormal), 2) / dynamicBody.getInertialMoment();

            float j = numerator / denominator;

            Vector2d impulse = collisionNormal.copy().multiply(j);

            dynamicBody.getVelocity().subtract(impulse.copy().multiply(1 / dynamicBody.getMass()));
            dynamicBody.setAngularVelocity(dynamicBody.getAngularVelocity() - contactA.cross(impulse) / dynamicBody.getInertialMoment());

        }

        dynamicBody.translate(collisionNormal.copy().multiply(-1 * penetration));

    }
}
