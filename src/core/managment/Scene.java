package core.managment;

import core.physics.PhysicsEnvironment2d;
import core.rendering.RenderEnvironment2d;

import java.util.HashMap;

public abstract class Scene {

    public final PhysicsEnvironment2d physicsEnvironment;
    public final RenderEnvironment2d renderEnvironment;

    public final Environment[] hierarchy;

    private final HashMap<Integer, GameObject> gameObjects;
    public void addGameObject(GameObject object){
        gameObjects.put(object.id, object);
        if (object instanceof Updatable) addUpdatableObject((Updatable) object, object.id);
    }
    public GameObject getGameObject(int id){
        return gameObjects.get(id);
    }
    public GameObject removeGameObject(int id){
        if (getGameObject(id) instanceof Updatable) removeUpdatableObject(id);
        return gameObjects.remove(id);
    }

    private final HashMap<Integer, Updatable> updatableObjects;
    public void addUpdatableObject(Updatable object, int id){
        updatableObjects.put(id, object);
    }
    public Updatable getUpdatableObject(int id){
        return updatableObjects.get(id);
    }
    public Updatable removeUpdatableObject(int id){
        return updatableObjects.remove(id);
    }

    public Scene(){

        this.physicsEnvironment = new PhysicsEnvironment2d();
        this.renderEnvironment = new RenderEnvironment2d();

        this.gameObjects = new HashMap<>();
        this.updatableObjects = new HashMap<>();

        this.hierarchy = new Environment[]{
                physicsEnvironment,
                renderEnvironment
        };

    }

    public abstract void load();

    public void update(){
        for(Environment environment : hierarchy){
            environment.update();
        }
    }

}
