package me.moonboygamer.buffered.addons;

import me.moonboygamer.buffered.post.DynamicPostShader;
import me.moonboygamer.buffered.post.pipeline.BufferedRenderPipeline;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public interface GameRendererAddon {
	void buffered$setPostShader(BufferedRenderPipeline pipeline);
	void buffered$loadPostShader(Identifier id, @Nullable DynamicPostShader shader);
}
