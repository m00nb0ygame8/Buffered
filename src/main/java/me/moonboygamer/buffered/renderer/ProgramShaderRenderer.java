package me.moonboygamer.buffered.renderer;

import me.moonboygamer.buffered.program.BufferedProgramShader;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;

public abstract class ProgramShaderRenderer extends ShaderRenderer<BufferedProgramShader> {
	public abstract void onRender(float tickDelta);
	@Override
	public void render(@Nullable MatrixStack stack, @Nullable Float tickDelta) {
		if(tickDelta == null) throw new IllegalArgumentException("MatrixStack cannot be null");
		if(shader == null || mesh == null) throw new IllegalStateException("ShaderRenderer not initialized.");
		onRender(tickDelta);
	}
}
