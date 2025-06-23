package me.moonboygamer.buffered.buffered.renderer;

import me.moonboygamer.buffered.mesh.Mesh;
import me.moonboygamer.buffered.program.CompiledShader;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;

public abstract class ShaderRenderer<T> {
	protected Mesh<T> mesh;
	protected CompiledShader<T> shader;

	protected ShaderRenderer() {}

	public void init() {
		if (shader == null)
			shader = createShader();
		mesh = createMesh(shader);
	}

	public abstract Mesh<T> createMesh(CompiledShader<T> shader);
	public abstract CompiledShader<T> createShader();
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
	public Mesh<T> getMesh() {
		if(mesh == null) throw new IllegalStateException("Mesh not initialized. Call createMesh() or init() first.");
		return mesh;
	}
}
