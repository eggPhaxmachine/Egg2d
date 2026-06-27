package physics;

import managment.GameManager;
import physics.broadphase.BroadPhaseDetection;
import physics.broadphase.BruteForceDetection;
import physics.broadphase.DynamicAABBTree;
import physics.narrowphase.Collision;
import physics.objects.DynamicBody2d;
import physics.objects.RigidBody2d;
import physics.objects.StaticBody2d;
import physics.space.Vector2d;
import managment.Environment;

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

    public void addDynamic(DynamicBody2d object){

        dynamics.add(object);
        broadPhase.addDynamic(object);

    }

    public void addConstant(StaticBody2d object){

        constants.add(object);
        broadPhase.addConstant(object);

    }

    private final BroadPhaseDetection broadPhase = new BruteForceDetection();

    private Vector2d constant = new Vector2d(0, -300);
    public Vector2d getConstant() {
        return constant;
    }
    public void setConstant(Vector2d constant) {
        this.constant = constant;
    }

    private float resistance = 0f;
    public float getResistance() {
        return resistance;
    }
    public void setResistance(float resistance) {
        if (resistance > 1 || resistance < 0) throw new InputMismatchException("Resistance must be between 0 and 1");
        this.resistance = resistance;
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

        broadPhase.update();

        Collision[] candidates = broadPhase.getCollisions();

        for (Collision candidate : candidates){
            candidate.check();
            if (candidate.isCollided()){
                candidate.correct();
                int i = 1;
            }
        }

    }

}
