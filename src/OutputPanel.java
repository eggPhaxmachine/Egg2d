import javax.swing.JPanel;
import java.awt.*;

public class OutputPanel extends JPanel {

    Renderable[] Renderables;

    public OutputPanel(Renderable... Renderables){

        this.Renderables = Renderables;

        setPreferredSize(new Dimension(Settings.screen.getScreenSize()[0], Settings.screen.getScreenSize()[1]));

    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2d = (Graphics2D) graphics;

        for(Renderable Renderable : Renderables){
            Renderable.render(graphics2d);
        }
    }

}
