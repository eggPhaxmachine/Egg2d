package core.physics;

import core.physics.broadphase.DynamicAABBTree;
import core.physics.objects.DynamicBody2d;
import core.physics.objects.RigidBody2d;
import core.physics.objects.StaticBody2d;

import java.util.ArrayList;
import java.util.InputMismatchException;

public class PhysicsEnvironment2d extends Environment {

    private final ArrayList<DynamicBody2d> dynamics = new ArrayList<>();
    private final ArrayList<StaticBody2d> constants = new ArrayList<>();

    public DynamicBody2d[] getDynamics(){
        return dynamics.toArray(DynamicBody2d[]::new);
    }

    public RigidBody2d[] getConstants(){
        return constants.toArray(RigidBody2d[]::new);
    }

    public void addDynamic(DynamicPolygon2d object){

        dynamics.add(object);
        candidates = broadPhase.addDynamic(object);

    }

    public void addConstant(StaticBody2d object){

        constants.add(object);
        candidates = broadPhase.addConstant(object);

    }

    private PPCollision[] candidates;
    private final DynamicAABBTree broadPhase = new DynamicAABBTree();

    private Vector2d constant = new Vector2d(0, 0);// -100);
    public Vector2d getConstant() {
        return constant;
    }
    public void setConstant(Vector2d constant) {
        this.constant = constant;
    }

    private float resistance = 0.0f;
    public float getResistance() {
        return resistance;
    }
    public void setResistance(float resistance) {
        if (resistance > 1 || resistance < 0) throw new InputMismatchException("Resistance must be between 0 and 1");
        this.resistance = resistance;
    }

    private float restitution = 1f;
    public float getRestitution() {
        return restitution;
    }
    public void setRestitution(float restitution) {
        if (restitution > 1 || restitution < 0) throw new InputMismatchException("Coefficient of restitution must be between 0 and 1");
        this.restitution = restitution;
    }

    public void update(){

        Vector2d updateTranslation = Vector2d.multiply(constant, GameManager.getDeltaTime());
        float updateResistance = (float) Math.pow(1 - resistance, GameManager.getDeltaTime());
        for (DynamicBody2d object : dynamics){
            Vector2d velocity = object.getVelocity();
            float angularVelocity = object.getAngularVelocity();

            velocity.add(updateTranslation);
            velocity.multiply(updateResistance);
            angularVelocity *= updateResistance;

            object.setVelocity(velocity);
            object.setAngularVelocity(angularVelocity);

            object.translate(Vector2d.multiply(velocity, GameManager.getDeltaTime()));
            object.rotate(angularVelocity * GameManager.getDeltaTime());

        }

        candidates = broadPhase.update();

        for (PPCollision candidate : candidates){
            candidate.check();
            candidate.correct(restitution);
        }

    }

}
