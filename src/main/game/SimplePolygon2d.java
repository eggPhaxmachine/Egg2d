package game;

import managment.GameManager;
import managment.WindowManager;
import physics.objects.DynamicPolygon2d;
import physics.space.Vector2d;
import rendering.Render;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class SimplePolygon2d extends DynamicPolygon2d implements Render {

    int front;

    public SimplePolygon2d(Vector2d[] vertices, float[] color) {
        super(GameManager.getId(), vertices);
        this.color = color;
        restitution = 1;

        for (int i = 0; i < 3; i++) {
            if (color[i] > 0) {
                front = i;
                break;
            }
        }
    }


    WindowManager windowManager;

    int modelMatrixLocation;
    int viewMatrixLocation;
    int projectionMatrixLocation;

    int colorLocation;
    float[] color;

    int VAO;
    int VBO;
    int EBO;

    @Override
    public void load() {

        windowManager = GameManager.windowManager;

        GLFW.glfwMakeContextCurrent(windowManager.getWindow());

        VAO = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(VAO);

        VBO = GL15.glGenBuffers();
        EBO = GL15.glGenBuffers();

        FloatBuffer data = MemoryUtil.memAllocFloat(vertices.length * 2);
        for (int i = 0; i < vertices.length; i++) {
            data.put(vertices[i].getX() - center.getX());
            data.put(vertices[i].getY() - center.getY());
        }
        data.flip();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER ,VBO);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
        MemoryUtil.memFree(data);

        GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 0, 0L);

        IntBuffer indices = MemoryUtil.memAllocInt(indecies.length);
        for (int i : indecies){
            indices.put(i);
        }
        indices.flip();

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, EBO);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);
        MemoryUtil.memFree(indices);

        GL20.glEnableVertexAttribArray(0);

        modelMatrixLocation = GL20.glGetUniformLocation(windowManager.getProgramId(), "model");
        viewMatrixLocation = GL20.glGetUniformLocation(windowManager.getProgramId(), "view");
        projectionMatrixLocation = GL20.glGetUniformLocation(windowManager.getProgramId(), "projection");
        colorLocation = GL20.glGetUniformLocation(windowManager.getProgramId(), "color");

        Matrix4f projectionMatrix = new Matrix4f().ortho(-windowManager.getWidth()/2f, windowManager.getWidth()/2f, -windowManager.getHeight()/2f, windowManager.getHeight()/2f, 0, 1);
        data = MemoryUtil.memAllocFloat(16);
        projectionMatrix.get(data);

        GL20.glUniformMatrix4fv(projectionMatrixLocation,false, data);
        MemoryUtil.memFree(data);

        Matrix4f viewMatrix = new Matrix4f();
        data = MemoryUtil.memAllocFloat(16);
        viewMatrix.get(data);

        GL20.glUniformMatrix4fv(viewMatrixLocation,false, data);
        MemoryUtil.memFree(data);

        //GL30.glBindVertexArray(0);

    }

    float netRotation = 0;
    @Override
    public void rotate(float theta) {
        super.rotate(theta);
        netRotation += theta;
    }

    @Override
    public void render() {

        if (color[front] < 1){
            color[front] -= GameManager.getDeltaTime();
            if (color[front] <= 0) front = (front + 1) % 3;
        } else {
            if (color[(front + 1) % 3] > 1) {
                color[front] -= GameManager.getDeltaTime();
            } else {
                color[(front + 1) % 3] += GameManager.getDeltaTime();
            }
        }

        GL30.glBindVertexArray(VAO);

        Matrix4f modelMatrix = new Matrix4f().translate(getCenter().getX(), getCenter().getY(), 0).rotate(netRotation, new Vector3f(0, 0, 1));
        FloatBuffer data = MemoryUtil.memAllocFloat(16);
        modelMatrix.get(data);

        GL20.glUniformMatrix4fv(modelMatrixLocation,false, data);
        MemoryUtil.memFree(data);

        data = MemoryUtil.memAllocFloat(4);
        data.put(flag ? new float[]{1f, 0f, 0f, 0f} : color).flip();

        GL20.glUniform4fv(colorLocation, data);
        MemoryUtil.memFree(data);

        GL11.glDrawElements(GL11.GL_TRIANGLES, 3 * (vertices.length - 2), GL11.GL_UNSIGNED_INT, 0L);

        flag = false;

        //GL30.glBindVertexArray(0);

    }
}
