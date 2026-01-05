import javax.swing.*;
import java.awt.*;
import java.lang.management.GarbageCollectorMXBean;

public class OutputWindow extends JFrame {

    static OutputPanel outputPanel;

    public static boolean active;

    OutputWindow(Engine physics2D){

        super("test window");

        setUndecorated(true);

        outputPanel = new OutputPanel(physics2D);
        add(outputPanel);
        pack();

        setVisible(true);

    }

    public static void main(String[] args){

        active = true;
        Start();

        outputPanel.repaint();
/*
        while (active){

            Update();
            Tools.realSleep(1000);
            System.out.println("updated");

        }
*/
    }

    private static void Start(){

        PolygonHitbox polygon1 = new PolygonHitbox(new Point2d[]{new Point2d(0,0), new Point2d(100,0), new Point2d(200,100), new Point2d(100,100)}, true, Color.BLUE);
        PolygonHitbox polygon2 = new PolygonHitbox(new Point2d[]{new Point2d(300,600), new Point2d(400,600), new Point2d(1000,1000), new Point2d(400,700)}, true, Color.RED);
        //PolygonHitbox polygon3 = new PolygonHitbox(new Point2d[]{new Point2d(0,0), new Point2d(1,0), new Point2d(2,1), new Point2d(1,1)}, true);
        //PolygonHitbox polygon4 = new PolygonHitbox(new Point2d[]{new Point2d(0,0), new Point2d(1,0), new Point2d(2,1), new Point2d(1,1)}, true);

        new OutputWindow(new Engine(new Vector2d(0, -1), 0.25, polygon1, polygon2));

    }

    private static void Update(){



    }

}