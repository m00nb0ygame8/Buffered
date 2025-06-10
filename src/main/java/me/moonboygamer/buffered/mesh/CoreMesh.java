package me.moonboygamer.buffered.mesh;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexBuffer;
import me.moonboygamer.buffered.core.BufferedCoreShader;
import me.moonboygamer.buffered.program.BufferedShaderProgram;
import net.minecraft.client.render.ShaderProgram;
import org.joml.Matrix4f;

public class CoreMesh extends Mesh<BufferedCoreShader> {
    public CoreMesh(BufferBuilder.RenderedBuffer rendered, BufferedShaderProgram<BufferedCoreShader> shader) {
        super(rendered, shader);
    }

    @Override
    public void draw(Matrix4f model, Matrix4f projection, float tickDelta) {
		shader.getStoredShader().compileUniforms();
		RenderSystem.setShader(shader.getStoredShader()::getShader);
        buffer.bind();
        buffer.draw(model, projection, shader.getStoredShader().getShader());
        VertexBuffer.unbind();
    }
}
