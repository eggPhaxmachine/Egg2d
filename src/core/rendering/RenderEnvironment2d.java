package rendering;

import managment.Environment;
import managment.GameManager;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;

public class RenderEnvironment2d extends Environment {

    private final HashMap<Integer, Render> renders;

    public void addRender(Render render){
        renders.put(render.getId(), render);
        render.load();
    }
    public Render getRender(int id){
        return renders.get(id);
    }
    public Render removeRender(int id){
        return renders.remove(id);
    }

    public RenderEnvironment2d(){
        this.renders= new HashMap<>();
    }

    @Override
    public void update() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        for (Render render : renders.values()){
            render.render();
        }
        GLFW.glfwSwapBuffers(GameManager.windowManager.getWindow());
    }
}
