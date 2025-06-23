package me.moonboygamer.buffered.mixin;

import me.moonboygamer.buffered.addons.GameRendererAddon;
import me.moonboygamer.buffered.post.pipeline.BufferedRenderPipeline;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.render.GameRenderer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(GameRenderer.class)
public class GameRendererMixin implements GameRendererAddon {
	@Shadow
	@Nullable ShaderEffect shader;

	@Unique
	@Override
	public void buffered$setPostShader(BufferedRenderPipeline pipeline) {
		shader = pipeline.getPostShader();
	}
}
