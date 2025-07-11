package me.moonboygamer.buffered.mixin;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.blaze3d.framebuffer.Framebuffer;
import com.mojang.blaze3d.shader.GlUniform;
import me.moonboygamer.buffered.post.DynamicPostShader;
import me.moonboygamer.buffered.post.PostProgramShaderRenderer;
import me.moonboygamer.buffered.program.BufferedProgramShader;
import me.moonboygamer.buffered.shader.BufferedShaderManager;
import net.minecraft.client.gl.PostProcessShader;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.gl.ShaderParseException;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Mixin(ShaderEffect.class)
public abstract class ShaderEffectMixin {
	//TODO: replace render() with custom code to handle BufferedProgramShader rendering
	@Shadow protected abstract void parseEffect(TextureManager textureManager, Identifier location) throws IOException;

	@Shadow
	public abstract void addTarget(String name, int width, int height);

	@Shadow
	protected abstract void parseTarget(JsonElement jsonTarget) throws ShaderParseException;

	@Shadow
	private int width;
	@Shadow
	private int height;

	@Shadow
	public abstract PostProcessShader addPass(String programName, Framebuffer source, Framebuffer dest) throws IOException;

	@Shadow
	protected abstract void parsePass(TextureManager textureManager, JsonElement jsonPass) throws IOException;

	@Shadow
	protected abstract void parseUniform(JsonElement jsonUniform) throws ShaderParseException;

	@Shadow
	private float lastTickDelta;
	@Shadow
	private float time;
	@Shadow
	@Final
	private List<PostProcessShader> passes;
	@Unique
	private static final Supplier<Boolean> useDynamicShader = BufferedShaderManager::isUseDynamicShader;
	@Unique
	private static final Supplier<DynamicPostShader> dynamicPostShader = BufferedShaderManager::getCurrentDynamicShader;
	@Unique
	private static final ThreadLocal<DynamicPostShader.PassDef> currentPass = new ThreadLocal<>();

	@ModifyVariable(
		method = "parseUniform",
		at = @At("STORE"),
		ordinal = 0
	)
	private GlUniform buffered$getUniform(GlUniform original, JsonElement jsonUniform) {
		if(!useDynamicShader.get()) return original;
		String name = JsonHelper.isString(jsonUniform) ? jsonUniform.getAsString() : JsonHelper.getString(jsonUniform.getAsJsonObject(), "name");
		if(name == null) throw new IllegalStateException("Uniform was not passed in correctly. Ensure you are using the correct method to parse uniforms.");
		if(currentPass.get() == null) throw new IllegalStateException("Current pass not set. This should not happen, please report this issue.");
		BufferedProgramShader shader = currentPass.get().shader;
		return shader.getUniform(name);
	}
	@Redirect(
		method = "<init>",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/ShaderEffect;parseEffect(Lnet/minecraft/client/texture/TextureManager;Lnet/minecraft/util/Identifier;)V")
	)
	private void buffered$createShaderEffect(ShaderEffect instance, TextureManager textureManager, Identifier location) {
		if(useDynamicShader.get()) {
			if(dynamicPostShader.get() == null) {
				throw new IllegalArgumentException("Dynamic shader not set. Use BufferedShaderManager.setDynamicPostShader() to set it.");
			}
			buffered$parseDynamicShader(textureManager);
		} else {
			try {
				parseEffect(textureManager, location);
			} catch (IOException e) {
				throw new RuntimeException("Failed to parse shader effect: " + location, e);
			}
		}
	}

	/**
	 * @author MoonBoyGamer
	 * @reason Override the render method to handle BufferedProgramShader rendering.
	 */
	@Overwrite
	public void render(float tickDelta) {
		if (tickDelta < this.lastTickDelta) {
			this.time += 1.0F - this.lastTickDelta;
			this.time += tickDelta;
		} else {
			this.time += tickDelta - this.lastTickDelta;
		}

		for(this.lastTickDelta = tickDelta; this.time > 20.0F; this.time -= 20.0F) {

		}

		for(PostProcessShader postProcessShader : this.passes) {
			PostProgramShaderRenderer renderer = new PostProgramShaderRenderer(
				new BufferedProgramShader(postProcessShader),
				postProcessShader.input,
				postProcessShader.output
			);
			renderer.init();
			//postProcessShader.render(this.time / 20.0F);
			renderer.render(null, this.time / 20.0F);
		}
	}


	@Unique
	private void buffered$parseDynamicShader(TextureManager textureManager) {
		if(dynamicPostShader.get() == null) {
			throw new IllegalArgumentException("Dynamic shader not set. Use ShaderEffectAddon.setDynamicPostShader() to set it.");
		}
		DynamicPostShader shader = dynamicPostShader.get();
		Map<String, DynamicPostShader.TargetDef> targets = shader.getTargets();
		for(Map.Entry<String, DynamicPostShader.TargetDef> entry : targets.entrySet()) {
			DynamicPostShader.TargetDef target = entry.getValue();
			if(target == null) {
				addTarget(entry.getKey(), width, height);
			} else {
				addTarget(entry.getKey(), target.width(), target.height());
			}
		}
		List<DynamicPostShader.PassDef> passes = shader.getPasses();
		for(DynamicPostShader.PassDef pass : passes) {
			try {
				currentPass.set(pass);
				parsePass(textureManager, pass.asJson());
			} catch (JsonParseException | IOException e) {
				throw new RuntimeException("Failed to parse pass: " + pass.passName, e);
			} finally {
				if(currentPass.get() != null) currentPass.remove();
			}
		}
	}
}
