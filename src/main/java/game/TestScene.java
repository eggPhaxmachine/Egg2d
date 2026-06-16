package game;

import core.Tools;
import core.managment.GameManager;
import core.managment.Scene;
import core.physics.space.Vector2d;

public class TestScene extends Scene {

    @Override
    public void load() {

        SimplePolygon2d object;
        for (int i = 0; i < 35; i++) {

            object = new SimplePolygon2d(new Vector2d[]{new Vector2d(0, 0), new Vector2d(0, 100), new Vector2d(100, 100), new Vector2d(100, 0)});
            object.rotate((float) Tools.rand(0, Math.PI*2));
            object.setVelocity(new Vector2d(Tools.rand(-250, 250), Tools.rand(-250, 250)));
            object.setAngularVelocity((float) Tools.rand(-1, 1));
            object.translate(new Vector2d(Tools.rand(-(GameManager.Settings.Window.defaultWidth - 50)/2, (GameManager.Settings.Window.defaultWidth - 50)/2), Tools.rand(-(GameManager.Settings.Window.defaultHeight - 50)/2, (GameManager.Settings.Window.defaultHeight - 50)/2)));

            GameManager.getCurrentScene().physicsEnvironment.addDynamic(object);
            GameManager.getCurrentScene().renderEnvironment.addRender(object);

        }

        int width = GameManager.Settings.Window.defaultWidth;
        int height = GameManager.Settings.Window.defaultHeight;

        object = new SimplePolygon2d(new Vector2d[]{new Vector2d(0, 0), new Vector2d(0, 100), new Vector2d(width, 100), new Vector2d(width, 0)});
        object.translate(new Vector2d(-width/2, -height/2 - 100));
        object.setDensity(Float.POSITIVE_INFINITY);
        GameManager.getCurrentScene().physicsEnvironment.addDynamic(object);
        GameManager.getCurrentScene().renderEnvironment.addRender(object);

        object = new SimplePolygon2d(new Vector2d[]{new Vector2d(0, 0), new Vector2d(0, 100), new Vector2d(width, 100), new Vector2d(width, 0)});
        object.translate(new Vector2d(-width/2, height/2));
        object.setDensity(Float.POSITIVE_INFINITY);
        GameManager.getCurrentScene().physicsEnvironment.addDynamic(object);
        GameManager.getCurrentScene().renderEnvironment.addRender(object);

        object = new SimplePolygon2d(new Vector2d[]{new Vector2d(0, 0), new Vector2d(0, height), new Vector2d(100, height), new Vector2d(100, 0)});
        object.translate(new Vector2d(-width/2 - 100, -height/2));
        object.setDensity(Float.POSITIVE_INFINITY);
        GameManager.getCurrentScene().physicsEnvironment.addDynamic(object);
        GameManager.getCurrentScene().renderEnvironment.addRender(object);

        object = new SimplePolygon2d(new Vector2d[]{new Vector2d(0, 0), new Vector2d(0, height), new Vector2d(100, height), new Vector2d(100, 0)});
        object.translate(new Vector2d(width/2, -height/2));
        object.setDensity(Float.POSITIVE_INFINITY);
        GameManager.getCurrentScene().physicsEnvironment.addDynamic(object);
        GameManager.getCurrentScene().renderEnvironment.addRender(object);

    }
}
