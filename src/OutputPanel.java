import javax.swing.JPanel;
import java.awt.*;

public class OutputPanel extends JPanel {

    Renderable[] Renderables;

    public static PolygonHitbox polygon1;
    public static PolygonHitbox polygon2;

    public OutputPanel(Renderable... Renderables){

        this.Renderables = Renderables;

        setPreferredSize(new Dimension(Settings.screen.getScreenSize()[0], Settings.screen.getScreenSize()[1]));

        polygon1 = new PolygonHitbox(new Point2d[]{new Point2d(100, 100), new Point2d(300, 100), new Point2d(100, 200)}, true, Color.BLUE);
        polygon2 = new PolygonHitbox(new Point2d[]{new Point2d(200, 100), new Point2d(400, 100), new Point2d(200, 200)}, true, Color.RED);

    }

    public void update(){
        for(Renderable render : Renderables){
            render.update();
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2d = (Graphics2D) graphics;

        for(Renderable Renderable : Renderables){
            Renderable.render(graphics2d);
        }

        polygon1.render(graphics2d);
        polygon2.render(graphics2d);

    }

}
