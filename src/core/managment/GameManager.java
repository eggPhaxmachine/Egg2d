package core.managment;

import org.lwjgl.glfw.GLFW;

public class GameManager {

    public static final WindowManager windowManager = new WindowManager(Settings.Window.defaultHeight, Settings.Window.defaultWidth, Settings.Window.defaultTitle);

    public static final Scene defaultScene = new TestScene();
    private static Scene currentScene;
    public static Scene getCurrentScene() {
        return currentScene;
    }
    public static void loadScene(Scene scene){
        currentScene = scene;
        currentScene.load();
    }

    public static void main(String[] args){

        windowManager.start();

        currentScene = defaultScene;
        currentScene.load();

        lastTime = System.nanoTime();

        while (!windowManager.shouldClose()){

            GLFW.glfwPollEvents();
            deltaTime = (System.nanoTime() - lastTime)/1e9f;
            lastTime = System.nanoTime();
            //System.out.println(1/deltaTime);
            currentScene.update();

        }

        GLFW.glfwTerminate();

    }

    private static int nextId = -1;
    public static int getId(){
        nextId++;
        return nextId;
    }

    private static long lastTime;
    private static float deltaTime;
    public static float getDeltaTime() {
        return deltaTime;
    }

    public static final class Settings{

        public static final class Window {

            public static final int defaultHeight = 1080;
            public static final int defaultWidth = 1920;

            public static final String  defaultTitle = "";

        }

    }

}