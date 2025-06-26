package me.moonboygamer.buffered.mixin;


import com.mojang.blaze3d.shader.GlUniform;
import net.minecraft.client.gl.JsonEffectGlShader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(JsonEffectGlShader.class)
public interface JsonGlShaderAccessor {
	@Accessor
	Map<String, GlUniform> getUniformByName();
}
