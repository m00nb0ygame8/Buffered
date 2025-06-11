package me.moonboygamer.buffered.mixin;

import com.mojang.blaze3d.framebuffer.Framebuffer;
import net.minecraft.client.gl.PostProcessShader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PostProcessShader.class)
public interface PostShaderAccessor {
	@Accessor("input")
	Framebuffer getInputFramebuffer();
	@Accessor("output")
	Framebuffer getOutputFramebuffer();
}
