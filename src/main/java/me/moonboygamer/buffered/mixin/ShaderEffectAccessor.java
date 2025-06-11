package me.moonboygamer.buffered.mixin;

import com.mojang.blaze3d.framebuffer.Framebuffer;
import net.minecraft.client.gl.ShaderEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ShaderEffect.class)
public interface ShaderEffectAccessor {
	@Accessor("mainTarget")
	Framebuffer getMainTarget();
}
