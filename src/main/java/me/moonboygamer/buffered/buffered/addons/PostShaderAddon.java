package me.moonboygamer.buffered.buffered.addons;

import com.mojang.blaze3d.vertex.VertexBuffer;
import org.jetbrains.annotations.Nullable;

public interface PostShaderAddon {
	void buffered$renderPost(float tickDelta, @Nullable VertexBuffer buffer, boolean useDefaultVBO);
}
