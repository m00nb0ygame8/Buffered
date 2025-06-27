package me.moonboygamer.buffered.mixin;

import com.mojang.blaze3d.framebuffer.Framebuffer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexBuffer;
import me.moonboygamer.buffered.addons.PostShaderAddon;
import me.moonboygamer.buffered.program.BufferedProgramShader;
import me.moonboygamer.buffered.util.ShaderDefaults;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.JsonEffectGlShader;
import net.minecraft.client.gl.PostProcessShader;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;

import java.util.List;
import java.util.function.IntSupplier;

@Mixin(PostProcessShader.class)
public class PostProcessShaderMixin implements PostShaderAddon {
	@Final
	@Shadow
	private List<IntSupplier> samplerValues;
	@Final
	@Shadow
	private List<String> samplerNames;
	@Final
	@Shadow
	private List<Integer> samplerWidths;
	@Final
	@Shadow
	private List<Integer> samplerHeights;
	@Shadow
	@Final
	private JsonEffectGlShader program;
	@Shadow
	@Final
	public Framebuffer input;
	@Shadow
	@Final
	public Framebuffer output;
	@Unique
	private static final ThreadLocal<VertexBuffer> vbo = new ThreadLocal<>();
	@Unique
	private static boolean isCustomDraw = false;
	@Unique
	@Override
	public void buffered$renderPost(float tickDelta, @Nullable VertexBuffer buffer, boolean useDefaultVBO) {
		isCustomDraw = true;
		if(useDefaultVBO) {
			vbo.set(
				ShaderDefaults.createDefaultPostVBO(
					786432, // Should be changed for a more dynamic value in the future
					output
				)
			);
		} else {
			vbo.set(buffer);
		}
		((PostProcessShader)(Object) this).render(tickDelta);
		vbo.remove();
		isCustomDraw = false;
	}

	/**
	 * @author MoonBoyGamer
	 * @reason Needed to use vbo passed in from renderPost method, and to use ShaderDefaults for inserting uniforms.
	 */
	@Overwrite
	public void render(float tickDelta) {
		BufferedProgramShader bufferedSelf = new BufferedProgramShader((PostProcessShader)(Object) this);
//		PostProcessShader self = (PostProcessShader)(Object) this;
		input.endWrite();
//		for(Map.Entry<String, GlUniform> uniform : ((JsonGlShaderAccessor)self.getProgram()).getUniformByName().entrySet()) {
//			BufferedConstants.LOGGER.warn("Uniform: {} = {}", uniform.getKey(), uniform.getValue());
//		}
		ShaderDefaults.insertPostSamplers(bufferedSelf, input);
		for(int i = 0; i < samplerValues.size(); ++i) {
			program.bindSampler(this.samplerNames.get(i), this.samplerValues.get(i));
			program.getUniformByNameOrDummy("AuxSize" + i).setVec2((float) this.samplerWidths.get(i), (float) this.samplerHeights.get(i));
		}
		ShaderDefaults.insertPostUniforms(bufferedSelf, tickDelta, input, false);
//		for(Map.Entry<String, GlUniform> uniform : ((JsonGlShaderAccessor)self.getProgram()).getUniformByName().entrySet()) {
//			BufferedConstants.LOGGER.warn("Uniform: {} set to {} with type {}", uniform.getKey(), uniform.getValue().getFloatData(), fromType(uniform.getValue().getDataType()));
//		}
		program.enable();
		output.clear(MinecraftClient.IS_SYSTEM_MAC);
		output.beginWrite(false);
		RenderSystem.depthFunc(519);
		VertexBuffer postVbo = vbo.get() != null ? vbo.get() : null;
		if(!isCustomDraw && postVbo == null) {
			postVbo = ShaderDefaults.createDefaultPostVBO(786432, output);
		}
		if(postVbo == null) throw new IllegalStateException("Cannot retrieve VBO for post-processing shader. Ensure you are using the correct method to render post-processing shaders.");
		postVbo.bind();
		postVbo.drawElements();
		RenderSystem.depthFunc(515);
		program.disable();
		output.endWrite();
		input.endRead();

		for(Object object : samplerValues) {
			if (object instanceof Framebuffer) {
				((Framebuffer)object).endRead();
			}
		}
		//postVbo.close();
	}

//	private String fromType(int type) {
//		  final int TYPE_INT = 0;
//		  final int TYPE_IVEC2 = 1;
//		  final int TYPE_IVEC3 = 2;
//		  final int TYPE_IVEC4 = 3;
//		  final int TYPE_FLOAT = 4;
//		  final int TYPE_VEC2 = 5;
//		  final int TYPE_VEC3 = 6;
//		  final int TYPE_VEC4 = 7;
//		  final int TYPE_MAT2 = 8;
//		  final int TYPE_MAT3 = 9;
//		  final int TYPE_MAT4 = 10;
//
//		return switch (type) {
//			case TYPE_INT -> "int";
//			case TYPE_IVEC2 -> "ivec2";
//			case TYPE_IVEC3 -> "ivec3";
//			case TYPE_IVEC4 -> "ivec4";
//			case TYPE_FLOAT -> "float";
//			case TYPE_VEC2 -> "vec2";
//			case TYPE_VEC3 -> "vec3";
//			case TYPE_VEC4 -> "vec4";
//			case TYPE_MAT2 -> "mat2";
//			case TYPE_MAT3 -> "mat3";
//			case TYPE_MAT4 -> "mat4";
//			default -> throw new IllegalArgumentException("Unknown type: " + type);
//		};
//	}
}
