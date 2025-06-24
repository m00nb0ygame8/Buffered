package me.moonboygamer.buffered.shader;

import me.moonboygamer.buffered.addons.GameRendererAddon;
import me.moonboygamer.buffered.mixin.GameRendererAccessor;
import me.moonboygamer.buffered.post.DynamicPostShader;
import me.moonboygamer.buffered.post.pipeline.BufferedRenderPipeline;
import me.moonboygamer.buffered.program.BufferedShader;
import me.moonboygamer.buffered.program.CompiledShader;
import net.minecraft.client.MinecraftClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BufferedShaderManager {
    private static boolean useDynamicShader = false;
	private static DynamicPostShader currentDynamicShader = null;
    private static final Map<String, CompiledShader<BufferedShader>> registry = new ConcurrentHashMap<>();

    public static boolean isUseDynamicShader() { return useDynamicShader; }
    public static void setUseDynamicShader(boolean isDynamicShader) { useDynamicShader = isDynamicShader; }

	@SuppressWarnings("unchecked")
    public static <T extends BufferedShader> CompiledShader<T> compileShader(T shader) {
        String key = shader.getClass().getName() + "@" + shader.hashCode();
		return (CompiledShader<T>) registry.computeIfAbsent(key, k -> () -> shader);
    }
	public static void release(CompiledShader<BufferedShader> shader) {
		String key = shader.getStoredShader().getClass().getName() + "@" + shader.getStoredShader().hashCode();
		CompiledShader<?> removed = registry.remove(key);
		if (removed != null) {
			removed.dispose();
		}
	}
    public static CompiledShader<BufferedShader> getShader(String key) {
        return registry.get(key);
    }

    public static void clearAll() {
        registry.clear();
    }

	public static DynamicPostShader getCurrentDynamicShader() {
		return currentDynamicShader;
	}

	public static void setCurrentDynamicShader(DynamicPostShader currentDynamicShader) {
		BufferedShaderManager.currentDynamicShader = currentDynamicShader;
	}

	public static void setPostShader(BufferedRenderPipeline pipeline) {
		((GameRendererAddon)MinecraftClient.getInstance().gameRenderer).buffered$setPostShader(pipeline);
	}

	public static void enablePostShader() {
		((GameRendererAccessor) MinecraftClient.getInstance().gameRenderer).setShadersEnabled(true);
	}
	public static void disablePostShader() {
		((GameRendererAccessor) MinecraftClient.getInstance().gameRenderer).setShadersEnabled(false);
	}
}
