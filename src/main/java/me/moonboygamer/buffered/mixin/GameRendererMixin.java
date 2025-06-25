package me.moonboygamer.buffered.mixin;

import me.moonboygamer.buffered.addons.GameRendererAddon;
import me.moonboygamer.buffered.post.DynamicPostShader;
import me.moonboygamer.buffered.post.pipeline.BufferedRenderPipeline;
import me.moonboygamer.buffered.shader.BufferedShaderManager;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.function.Supplier;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin implements GameRendererAddon {
	@Shadow
	@Nullable ShaderEffect shader;

	@Shadow
	abstract void loadShader(Identifier id);

	@Unique
	private static final Supplier<Boolean> useDynamicShader = BufferedShaderManager::isUseDynamicShader;
	@Unique
	private static final Supplier<DynamicPostShader> dynamicPostShader = BufferedShaderManager::getCurrentDynamicShader;
	@Unique
	@Override
	public void buffered$setPostShader(BufferedRenderPipeline pipeline) {
		shader = pipeline.getPostShader();
	}
	@Unique
	@Override
	public void buffered$loadPostShader(Identifier id, @Nullable DynamicPostShader shader) {
		if(shader == null) {
			loadShader(id);
		} else {
			BufferedShaderManager.setCurrentDynamicShader(shader);
			BufferedShaderManager.setUseDynamicShader(true);
			loadShader(BufferedShaderManager.getDynamicPostId());
			BufferedShaderManager.setUseDynamicShader(false);
		}
	}
}
