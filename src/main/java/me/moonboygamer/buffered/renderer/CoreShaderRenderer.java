package me.moonboygamer.buffered.renderer;

import me.moonboygamer.buffered.core.BufferedCoreShader;
import net.minecraft.client.render.ShaderProgram;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;

public abstract class CoreShaderRenderer extends ShaderRenderer<BufferedCoreShader> {

	public abstract void onRender(MatrixStack matrices);
	@Override
	public void render(@Nullable MatrixStack stack, @Nullable Float tickDelta) {
		if(stack == null) throw new IllegalArgumentException("MatrixStack cannot be null");
		if(shader == null || mesh == null) throw new IllegalStateException("ShaderRenderer not initialized.");
		onRender(stack);
	}
}
