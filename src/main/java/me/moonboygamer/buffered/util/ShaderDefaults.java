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

		if(insertSamplers) insertPostSamplers(shader, in);;

		shader.hasUniform("ProjMat", (glUniform) -> {
			glUniform.setMat4x4(
				new Matrix4f().setOrtho(
					0.0F, (float) input.textureWidth,
					0.0F, (float) input.textureHeight,
					0.1F, 1000.0F
				)
			);
			shader.markUniformsDirty();
		});
		shader.hasUniform("InSize", (glUniform) -> {
			glUniform.setFloats(
				new float[] {
					(float) in.textureWidth,
					(float) in.textureHeight
				}
			);
			shader.markUniformsDirty();
		});
		shader.hasUniform("OutSize", (glUniform) -> {
			glUniform.setFloats(
				new float[]{
					(float) out.textureWidth,
					(float) out.textureHeight
				}
			);
			shader.markUniformsDirty();
		});
		shader.hasUniform("Time", (glUniform) -> {
			glUniform.setFloat(time);
			shader.markUniformsDirty();
		});
		MinecraftClient client = MinecraftClient.getInstance();
		shader.hasUniform("ScreenSize", (glUniform) -> {
			glUniform.setVec2(
				(float) client.getWindow().getFramebufferWidth(),
				(float) client.getWindow().getFramebufferHeight()
			);
			shader.markUniformsDirty();
		});

	}
	public static void insertPostSamplers(BufferedProgramShader shader, Framebuffer in) {
		shader.getShader().getProgram().bindSampler("TextureSampler", in::getColorAttachment);
		shader.getShader().getProgram().bindSampler("DepthSampler", in::getDepthAttachment);
	}
	public static VertexBuffer createDefaultPostVBO(int initialCapacity, Framebuffer out) {
		VertexBuffer vbo = VertexFormats.POSITION_TEXTURE_COLOR.getBuffer();
		float f = (float)out.textureWidth;
		float g = (float)out.textureHeight;
		BufferBuilder bufferBuilder = new BufferBuilder(initialCapacity);
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
		bufferBuilder.vertex(0.0F, 0.0F, 500.0F).uv(0.0F, 0.0F).color(255, 255, 255, 255).next();
		bufferBuilder.vertex(f, 0.0F, 500.0F).uv(0.0F, 0.0F).color(255, 255, 255, 255).next();
		bufferBuilder.vertex(f, g, 500.0F).uv(0.0F, 0.0F).color(255, 255, 255, 255).next();
		bufferBuilder.vertex(0.0F, g, 500.0F).uv(0.0F, 0.0F).color(255, 255, 255, 255).next();
		vbo.upload(bufferBuilder.end());
		return vbo;
	}
}
