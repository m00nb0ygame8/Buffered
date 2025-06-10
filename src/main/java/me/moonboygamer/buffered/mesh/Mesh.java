package me.moonboygamer.buffered.mesh;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexBuffer;
import me.moonboygamer.buffered.program.BufferedShaderProgram;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

public abstract class Mesh<T> {
    protected final VertexBuffer buffer;
    protected final BufferedShaderProgram<T> shader;

    public Mesh(BufferBuilder.RenderedBuffer rendered, BufferedShaderProgram<T> shader) {
        this.shader = shader;
        this.buffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        this.buffer.bind();
        this.buffer.upload(rendered);
        VertexBuffer.unbind();
    }

    protected Mesh(BufferedShaderProgram<T> shader) {
        this.shader = shader;
        this.buffer = null;
    }

    public abstract void draw(Matrix4f model, Matrix4f projection, float tickDelta);
}
