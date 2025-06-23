package me.moonboygamer.buffered.buffered.renderer;

import me.moonboygamer.buffered.post.pipeline.BufferedRenderPipeline;

public abstract class PostShaderRenderer {
	private BufferedRenderPipeline pipeline;

	public PostShaderRenderer() {
		this.pipeline = createPipeline();
	}

	public abstract BufferedRenderPipeline createPipeline();

	public void render() {
		if(pipeline == null) throw new IllegalStateException("Pipeline is not initialized. This should not happen, please report this issue to the mod author.");
		pipeline.render();
	}
}
