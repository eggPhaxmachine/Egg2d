import javax.swing.*;

public class OutputWindow extends JFrame {

    static OutputPanel outputPanel;

    public static boolean active;

    OutputWindow(Renderable[] renderables){

        super("test window");

        setUndecorated(true);

        outputPanel = new OutputPanel(renderables);
        add(outputPanel);
        pack();

        setVisible(true);

    }

    public static void main(String[] args){

        active = true;

        OutputWindow window = new OutputWindow(new Renderable[]{});
        Tools.realSleep(2000);
        Collision collision;
        collision =  new Collision(OutputPanel.polygon1, OutputPanel.polygon2);
        System.out.println(collision.isCollided());
        collision.correct();
        outputPanel.repaint();

        //window.start();
/*
        while (active){

            window.update();
            Tools.realSleep(100);

        }
*/
    }

    private void start(){



    }

    private void update(){

        outputPanel.update();
        outputPanel.repaint();

    }

}