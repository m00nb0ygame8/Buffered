package me.moonboygamer.buffered.mixin;

import net.minecraft.client.gl.JsonEffectGlShader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(JsonEffectGlShader.class)
public class JsonGlShaderMixin {
	@ModifyVariable(
		method = "<init>",
		at = @At("STORE"),
		ordinal = 0
	)
	private Identifier buffered$createGlShader(Identifier original, ResourceManager resource, String name) {
		Identifier identifier = new Identifier(name);
		if (!identifier.getNamespace().equals("minecraft")) {
			return identifier;
		}
		return original;
	}
}

