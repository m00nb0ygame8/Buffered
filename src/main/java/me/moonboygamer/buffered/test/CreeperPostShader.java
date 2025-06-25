package me.moonboygamer.buffered.test;

import me.moonboygamer.buffered.post.pipeline.BufferedRenderPipeline;
import me.moonboygamer.buffered.renderer.PostShaderRenderer;
import me.moonboygamer.buffered.util.PostShaderHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class CreeperPostShader extends PostShaderRenderer {
	@Override
	public BufferedRenderPipeline createPipeline() {
		return new BufferedRenderPipeline(PostShaderHelper.createPostShader(
			false,
			MinecraftClient.getInstance().getFramebuffer(),
			Objects.requireNonNull(Identifier.tryParse("buffered:shaders/test/test_creeper.json")),
			null
		));
	}
}
