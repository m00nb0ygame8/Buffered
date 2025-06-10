package me.moonboygamer.buffered.renderer;

import me.moonboygamer.buffered.mesh.Mesh;
import me.moonboygamer.buffered.program.BufferedShaderProgram;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;

public abstract class ShaderRenderer<T> {
	protected Mesh<T> mesh;
	protected BufferedShaderProgram<T> shader;

	protected ShaderRenderer() {}

	public void init() {
		if (shader == null)
			shader = createShader();
		mesh = createMesh();
	}

	public abstract Mesh<T> createMesh();
	public abstract BufferedShaderProgram<T> createShader();
	public abstract void render(@Nullable MatrixStack stack, @Nullable Float tickDelta);

	public static class Factory {
		public static <Q, T extends ShaderRenderer<Q>> T create(Class<T> clazz, boolean autoInit) {
			try {
				T instance = clazz.getDeclaredConstructor().newInstance();
				if (autoInit) instance.init();
				return instance;
			} catch (Exception e) {
				throw new RuntimeException("Failed to instantiate ShaderRenderer: " + clazz.getName(), e);
			}
		}
	}
}
