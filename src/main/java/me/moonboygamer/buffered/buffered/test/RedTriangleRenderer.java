package me.moonboygamer.buffered.buffered.test;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormats;
import me.moonboygamer.buffered.core.BufferedCoreShader;
import me.moonboygamer.buffered.mesh.CoreMesh;
import me.moonboygamer.buffered.mesh.Mesh;
import me.moonboygamer.buffered.program.CompiledShader;
import me.moonboygamer.buffered.renderer.CoreShaderRenderer;
import me.moonboygamer.buffered.shader.BufferedShaderManager;
import me.moonboygamer.buffered.util.ShaderLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

public class RedTriangleRenderer extends CoreShaderRenderer {
	private final BufferedCoreShader redTriangleShader;

	public RedTriangleRenderer() {
		this.redTriangleShader = new BufferedCoreShader(
			ShaderLoader.loadCoreShader(
				new Identifier("buffered", "shaders/test/red_triangle.json"),
				VertexFormats.POSITION
			)
		);
	}
	@Override
	public void onRender(MatrixStack matrices) {
		RenderSystem.disableDepthTest();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();

		Matrix4f identity = new Matrix4f().identity();
		mesh.draw(identity, identity, 0.0f);
	}

	@Override
	public Mesh<BufferedCoreShader> createMesh(CompiledShader<BufferedCoreShader> shader) {
		BufferBuilder builder = new BufferBuilder(256);
		builder.begin(VertexFormat.DrawMode.TRIANGLES, VertexFormats.POSITION);

		builder.vertex(-0.1f, -0.1f, 0f).next();
		builder.vertex( 0.1f, -0.1f, 0f).next();
		builder.vertex( 0.0f,  0.2f, 0f).next();
		return new CoreMesh(builder.end(), shader);
	}

	@Override
	public CompiledShader<BufferedCoreShader> createShader() {
		return BufferedShaderManager.compileShader(redTriangleShader);
	}
}
