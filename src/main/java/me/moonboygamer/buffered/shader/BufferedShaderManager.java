package me.moonboygamer.buffered.shader;

import me.moonboygamer.buffered.program.BufferedShader;
import me.moonboygamer.buffered.program.CompiledShader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BufferedShaderManager {
    private static boolean useDynamicShader = false;
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
}
