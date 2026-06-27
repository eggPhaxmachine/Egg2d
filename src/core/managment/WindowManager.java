package managment;

import rendering.shaders.ShaderCompileException;
import rendering.shaders.ShaderUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WindowManager {

    private final long window;
    public long getWindow() {
        return window;
    }

    public String getTitle() {
        return GLFW.glfwGetWindowTitle(window);
    }
    public void setTitle(String title){
        GLFW.glfwSetWindowTitle(window, title);
    }

    private int width;
    public int getWidth() {
        return width;
    }

    private int height;
    public int getHeight() {
        return height;
    }

    public final WindowSettings settings;

    public WindowManager(int height, int width, String title) {

        this.height = height;
        this.width = width;

        this.settings = new WindowSettings();

        GLFWErrorCallback.createPrint(System.err).set();

        if (!GLFW.glfwInit()) throw new RuntimeException("GLFW failed to initialize");

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_DEBUG_CONTEXT, GLFW.GLFW_TRUE);

        this.window = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
        if (window == MemoryUtil.NULL) throw new RuntimeException("GLFW failed to create window");

    }

    private boolean maximized;
    public boolean isMaximized() {
        return maximized;
    }

    private int programId;
    public int getProgramId() {
        return programId;
    }

    public void start(){

        this.maximized = false;
        programId = 0;

        GLFW.glfwSetFramebufferSizeCallback(window, (win, width, height) -> {
            GLFW.glfwMakeContextCurrent(window);
            this.width = width;
            this.height = height;
            GL30.glViewport(0, 0, width, height);
        });
        GLFW.glfwSetWindowMaximizeCallback(window, (window, maximized) -> {
            this.maximized = maximized;
        });

        GLFW.glfwMakeContextCurrent(window);
        GLFW.glfwShowWindow(window);

        GL.createCapabilities();

        GLUtil.setupDebugMessageCallback(System.out);

        GL30.glViewport(0, 0, width, height);

        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);

        GLFW.glfwSwapInterval(1);


        if (settings.getVertexShaderSource() == null) {
            System.err.println("Vertex shader source not set, using default: shaders/VertexShader.glsl");
            settings.setVertexShaderSource("shaders/VertexShader.glsl");
        }
        if (settings.getFragmentShaderSource() == null){
            System.err.println("Fragment shader source not set, using default: shaders/FragmentShader.glsl");
            settings.setFragmentShaderSource("shaders/FragmentShader.glsl");
        }
        loadShaders();

        float[] color = settings.getClearColor();
        GL11.glClearColor(color[0], color[1], color[2], color[3]);

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

    }

    public boolean shouldClose(){
        return GLFW.glfwWindowShouldClose(window);
    }

    public boolean isPressed(int keycode){
        return GLFW.glfwGetKey(window, keycode) == GLFW.GLFW_PRESS;
    }

    public void reloadShaders(){

        GLFW.glfwMakeContextCurrent(window);

        GL20.glUseProgram(0);
        GL20.glDeleteProgram(GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM));

        loadShaders();

    }

    private void loadShaders() {

        int vertexShaderId;
        int fragmentShaderId;

        try {
            vertexShaderId = ShaderUtils.compileShaders(settings.getVertexShaderSource(), GL20.GL_VERTEX_SHADER);
        } catch (ShaderCompileException | IOException e){
            System.out.println(e.getMessage() + "\nShaders unbound");
            return;
        }

        try {
            fragmentShaderId = ShaderUtils.compileShaders(settings.getFragmentShaderSource(), GL20.GL_FRAGMENT_SHADER);
        } catch (ShaderCompileException | IOException e){
            GL20.glDeleteShader(vertexShaderId);
            System.out.println(e.getMessage() + "Shaders unbound\n");
            return;
        }

        programId = GL20.glCreateProgram();
        GL20.glAttachShader(programId, vertexShaderId);
        GL20.glAttachShader(programId, fragmentShaderId);

        GL20.glLinkProgram(programId);
        if(GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS) == GL11.GL_FALSE){
            GL20.glDeleteShader(vertexShaderId);
            GL20.glDeleteShader(fragmentShaderId);
            GL20.glDeleteProgram(programId);

            System.out.println("Failed to link shaders\nShaders unbound");
            return;
        }

        GL20.glDeleteShader(vertexShaderId);
        GL20.glDeleteShader(fragmentShaderId);

        GL20.glUseProgram(programId);

    }

    private void update(){

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        GLFW.glfwMakeContextCurrent(window);
        GLFW.glfwPollEvents();

    }

    public void destroy(){
        GLFW.glfwDestroyWindow(window);
    }

    public class WindowSettings {

        float[] clearColor = new float[]{0f, 0f, 0f, 1f};
        public float[] getClearColor() {
            return clearColor;
        }
        public void setClearColor(float r, float g, float b, float a) {
            this.clearColor = new float[]{r, b, g, a};
            GLFW.glfwMakeContextCurrent(window);
            GL11.glClearColor(r, g, b, a);
        }

        private Path vertexShaderSource;
        public void setVertexShaderSource(Path vertexShaderSource) {
            this.vertexShaderSource = vertexShaderSource;
        }
        public void setVertexShaderSource(String pathString){
            Path path = Paths.get(pathString);
            if (!Files.isRegularFile(path)){
                throw new RuntimeException("\nFile at \"" + pathString + "\" is inaccessible");
            }
            this.vertexShaderSource = path;
        }
        public Path getVertexShaderSource() {
            return vertexShaderSource;
        }

        private Path fragmentShaderSource;
        public void setFragmentShaderSource(Path fragmentShaderSource) {
            this.fragmentShaderSource = fragmentShaderSource;
        }
        public void setFragmentShaderSource(String pathString) {
            Path path = Paths.get(pathString);
            if (!Files.isRegularFile(path)){
                throw new RuntimeException("\nFile at \"" + pathString + "\" is inaccessible");
            }
            this.fragmentShaderSource = path;
        }
        public Path getFragmentShaderSource() {
            return fragmentShaderSource;
        }
    }

}