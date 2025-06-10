package me.moonboygamer.buffered.mesh;

import me.moonboygamer.buffered.post.BufferedPostShader;
import me.moonboygamer.buffered.program.BufferedShaderProgram;
import me.moonboygamer.buffered.program.PostShaderProgram;
import net.minecraft.client.gl.PostProcessShader;
import org.joml.Matrix4f;

public class PostMesh extends Mesh<BufferedPostShader> {
    public PostMesh(PostShaderProgram shader) {
        super(shader);
    }

    @Override
    public void draw(Matrix4f model, Matrix4f projection, float tickDelta) {
		((PostShaderProgram) shader).render(tickDelta);
    }
}
