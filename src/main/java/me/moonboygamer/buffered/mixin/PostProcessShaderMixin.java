package me.moonboygamer.buffered.mixin;

import com.mojang.blaze3d.framebuffer.Framebuffer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexBuffer;
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
	@Override
	public void buffered$renderPost(float tickDelta, @Nullable VertexBuffer buffer, boolean useDefaultVBO) {
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
	}

	/**
	 * @author MoonBoyGamer
	 * @reason Needed to use vbo passed in from renderPost method, and to use ShaderDefaults for inserting uniforms.
	 */
	@Overwrite
	public void render(float tickDelta) {
		BufferedProgramShader bufferedSelf = new BufferedProgramShader((PostProcessShader)(Object) this);
		input.endWrite();
		ShaderDefaults.insertPostSamplers(bufferedSelf, input);
		for(int i = 0; i < samplerValues.size(); ++i) {
			program.bindSampler(this.samplerNames.get(i), this.samplerValues.get(i));
			program.getUniformByNameOrDummy("AuxSize" + i).setVec2((float) this.samplerWidths.get(i), (float) this.samplerHeights.get(i));
		}
		ShaderDefaults.insertPostUniforms(bufferedSelf, tickDelta, input, false);
		program.enable();
		output.clear(MinecraftClient.IS_SYSTEM_MAC);
		output.beginWrite(false);
		RenderSystem.depthFunc(519);
		VertexBuffer postVbo = vbo.get() != null ? vbo.get() : null;
		if(postVbo == null) throw new IllegalStateException("Cannot retrieve VBO for post-processing shader. Ensure you are using the correct method to render post-processing shaders.");
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
	}
}
