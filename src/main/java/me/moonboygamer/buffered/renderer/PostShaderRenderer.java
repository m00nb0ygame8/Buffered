package me.moonboygamer.buffered.renderer;

import me.moonboygamer.buffered.mesh.Mesh;
import me.moonboygamer.buffered.mesh.PostMesh;
import me.moonboygamer.buffered.post.BufferedPostShader;
import me.moonboygamer.buffered.program.BufferedShaderProgram;
import me.moonboygamer.buffered.program.PostShaderProgram;
import net.minecraft.client.gl.PostProcessShader;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;

public abstract class PostShaderRenderer extends ShaderRenderer<BufferedPostShader> {

	public abstract void onRender(float tickDelta);

	@Override
	public void render(@Nullable MatrixStack stack, @Nullable Float tickDelta) {
		if(tickDelta == null) throw new IllegalArgumentException("Tick delta cannot be null");
		if(shader == null || mesh == null) throw new IllegalStateException("ShaderRenderer not initialized.");
		onRender(tickDelta);
	}
}
