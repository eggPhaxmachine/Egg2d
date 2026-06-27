package managment;

import physics.PhysicsEnvironment2d;
import rendering.RenderEnvironment2d;

import java.util.HashMap;

public abstract class Scene {

    public final PhysicsEnvironment2d physicsEnvironment;
    public final RenderEnvironment2d renderEnvironment;

    public final Environment[] hierarchy;

    private final HashMap<Integer, GameObject> gameObjects;
    public void addGameObject(GameObject object){
        gameObjects.put(object.id, object);
    }
    public GameObject getGameObject(int id){
        return gameObjects.get(id);
    }
    public GameObject removeGameObject(int id){
        return gameObjects.remove(id);
    }

    public Scene(){

        this.physicsEnvironment = new PhysicsEnvironment2d();
        this.renderEnvironment = new RenderEnvironment2d();

        this.gameObjects = new HashMap<>();

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
