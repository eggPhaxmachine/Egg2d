package core.rendering.shaders;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ShaderUtils {

    public static int compileShaders(Path shaderSource, int shaderType) throws IOException, RuntimeException {

        int shader = GL20.glCreateShader(shaderType);

        try {
            GL20.glShaderSource(shader, Files.readString(shaderSource));
        } catch (IOException e){
            GL20.glDeleteShader(shader);
            throw e;
        }

        GL20.glCompileShader(shader);

        if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE){
            GL20.glDeleteShader(shader);
            throw new ShaderCompileException("\nShader \"" + shaderSource + "\" failed to compile: \n" + GL20.glGetShaderInfoLog(shader));
        }

        return shader;

    }

}
