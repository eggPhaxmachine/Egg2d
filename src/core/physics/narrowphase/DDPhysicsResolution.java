package physics.narrowphase;

import physics.objects.DynamicBody2d;
import physics.space.Vector2d;

public class DDPhysicsResolution implements PhysicsResolution{

    protected final DynamicBody2d[] bodies;


    public DDPhysicsResolution(DynamicBody2d[] bodies) {
        this.bodies = bodies;
    }


    @Override
    public void correct(Vector2d[] contacts, Vector2d collisionNormal, float penetration) {

        float restitution = Math.min(bodies[0].getRestitution(), bodies[1].getRestitution());

        for (Vector2d contact : contacts){

            Vector2d contactA = contact.copy().subtract(bodies[0].getCenter());
            Vector2d contactB = contact.copy().subtract(bodies[1].getCenter());

            Vector2d relVA = contactA.copy().perpendicular().multiply(bodies[0].getAngularVelocity()).add(bodies[0].getVelocity());
            Vector2d relVB = contactB.copy().perpendicular().multiply(bodies[1].getAngularVelocity()).add(bodies[1].getVelocity());

            Vector2d relV = relVB.copy().subtract(relVA);

            float temp = relV.dot(collisionNormal);
            if (temp > 0) break;

            float numerator = relV.multiply(-(1 + restitution)).dot(collisionNormal);

            float denominator = 1/bodies[0].getMass();
            denominator += 1/bodies[1].getMass();
            denominator += (float) Math.pow(contactA.cross(collisionNormal), 2) / bodies[0].getInertialMoment();
            denominator += (float) Math.pow(contactB.cross(collisionNormal), 2) / bodies[1].getInertialMoment();

            float j = numerator/denominator;

            Vector2d impulse = collisionNormal.copy().multiply(j);

            bodies[0].getVelocity().subtract(impulse.copy().multiply(1/bodies[0].getMass()));
            bodies[0].setAngularVelocity(bodies[0].getAngularVelocity() - contactA.cross(impulse)/bodies[0].getInertialMoment());

            bodies[1].getVelocity().add(impulse.copy().multiply(1/bodies[1].getMass()));
            bodies[1].setAngularVelocity(bodies[1].getAngularVelocity() + contactB.cross(impulse)/bodies[1].getInertialMoment());

        }

        float wA = 1/bodies[0].getMass();
        float wB = 1/bodies[1].getMass();

        float wTotal = wA + wB;

        bodies[0].translate(collisionNormal.copy().multiply(-1 * wA/wTotal * penetration));

        bodies[1].translate(collisionNormal.copy().multiply(wB/wTotal * penetration));
    }
}
