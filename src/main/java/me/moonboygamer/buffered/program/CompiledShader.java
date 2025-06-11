package me.moonboygamer.buffered.program;

public interface CompiledShader<T> {
	T getStoredShader();

	default void dispose() {
		// Default implementation does nothing, can be overridden if needed
		// This is useful for cleaning up resources when the shader is no longer needed
	}
}
