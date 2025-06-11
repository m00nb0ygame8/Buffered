package me.moonboygamer.buffered.util;

import com.mojang.blaze3d.framebuffer.Framebuffer;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormats;
import me.moonboygamer.buffered.mixin.PostShaderAccessor;
import me.moonboygamer.buffered.program.BufferedProgramShader;
import net.minecraft.client.MinecraftClient;
import org.joml.Matrix4f;

public class ShaderDefaults {

	public static void insertPostUniforms(BufferedProgramShader shader, float time, Framebuffer input, boolean insertSamplers) {
		Framebuffer in = ((PostShaderAccessor) shader.getShader()).getInputFramebuffer();
		Framebuffer out = ((PostShaderAccessor) shader.getShader()).getOutputFramebuffer();

		if(insertSamplers) insertPostSamplers(shader, in, out);

		shader.getUniform("ProjMat").setMat4x4(
			new Matrix4f()
				.setOrtho(0.0F, (float)input.textureWidth,
					0.0F, (float)input.textureHeight,
					0.1F,
					1000.0F
				)
		);
		shader.getUniform("InSize").setVec2(
			(float) in.textureWidth,
			(float) in.textureHeight
		);
		shader.getUniform("OutSize").setVec2(
			(float) out.textureWidth,
			(float) out.textureHeight
		);
		shader.getUniform("Time").setFloat(time);
		MinecraftClient client = MinecraftClient.getInstance();
		shader.getUniform("ScreenSize").setVec2(
			(float) client.getWindow().getFramebufferWidth(),
			(float) client.getWindow().getFramebufferHeight());

	}
	public static void insertPostSamplers(BufferedProgramShader shader, Framebuffer in, Framebuffer out) {
		shader.getShader().getProgram().bindSampler("TextureSampler", in::getColorAttachment);
		shader.getShader().getProgram().bindSampler("DepthSampler", in::getDepthAttachment);
		shader.getShader().getProgram().bindSampler("OutputSampler", out::getColorAttachment);
	}
	public static VertexBuffer createDefaultPostVBO(int initialCapacity, Framebuffer out) {
		VertexBuffer vbo = VertexFormats.POSITION.getBuffer();
		float f = (float)out.textureWidth;
		float g = (float)out.textureHeight;
		BufferBuilder bufferBuilder = new BufferBuilder(initialCapacity);
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
		bufferBuilder.vertex(0.0F, 0.0F, 500.0F).next();
		bufferBuilder.vertex(f, 0.0F, 500.0F).next();
		bufferBuilder.vertex(f, g, 500.0F).next();
		bufferBuilder.vertex(0.0F, g, 500.0F).next();
		vbo.upload(bufferBuilder.end());
		return vbo;
	}
}
