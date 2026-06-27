package game;

import managment.GameManager;
import managment.Scene;
import physics.objects.StaticPolygon2d;
import physics.space.Vector2d;

public class TestScene extends Scene {

    @Override
    public void load() {

        SimplePolygon2d object;
        for (int i = 0; i < 50; i++) {

            object = new SimplePolygon2d(new Vector2d[]{new Vector2d(0, 0), new Vector2d(0, 50), new Vector2d(50, 50), new Vector2d(50, 0)}, new float[]{1, 0, 0, 0});
            object.setRestitution(1);
            object.setVelocity(new Vector2d(rand(-250, 250), rand(-250, 250)));
            object.setAngularVelocity((float) rand(-1, 1));
            object.translate(new Vector2d(rand(-(GameManager.Settings.Window.defaultWidth - 50)/2, (GameManager.Settings.Window.defaultWidth - 50)/2), rand(-(GameManager.Settings.Window.defaultHeight - 50)/2, (GameManager.Settings.Window.defaultHeight - 50)/2)));

            GameManager.getCurrentScene().physicsEnvironment.addDynamic(object);
            GameManager.getCurrentScene().renderEnvironment.addRender(object);

        }

        int width = GameManager.Settings.Window.defaultWidth;
        int height = GameManager.Settings.Window.defaultHeight;

        StaticPolygon2d border;

        border = new StaticPolygon2d(new Vector2d[]{new Vector2d(-width/2, -height/2 - 100), new Vector2d(-width/2, -height/2), new Vector2d(width/2, -height/2), new Vector2d(width/2, -height/2 - 100)});
        GameManager.getCurrentScene().physicsEnvironment.addConstant(border);

        border = new StaticPolygon2d(new Vector2d[]{new Vector2d(-width/2, height/2), new Vector2d(-width/2, height/2 + 100), new Vector2d(width/2, height/2 + 100), new Vector2d(width/2, height/2)});
        GameManager.getCurrentScene().physicsEnvironment.addConstant(border);

        border = new StaticPolygon2d(new Vector2d[]{new Vector2d(-width/2 - 100, -height/2), new Vector2d(-width/2 - 100, height/2), new Vector2d(-width/2, height/2), new Vector2d(-width/2, -height/2)});
        GameManager.getCurrentScene().physicsEnvironment.addConstant(border);

        border = new StaticPolygon2d(new Vector2d[]{new Vector2d(width/2, -height/2), new Vector2d(width/2, height/2), new Vector2d(width/2 + 100, height/2), new Vector2d(width/2 + 100, -height/2)});
        GameManager.getCurrentScene().physicsEnvironment.addConstant(border);

    }

    public static int rand(int min, int max){
        return (int)(Math.random() * (max - min + 1)) + min;
    }
    public static float rand(float min, float max){
        return (((float) Math.random() * (max - min + 1)) + min);
    }

}
