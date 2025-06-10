package me.moonboygamer.buffered.mixin;

import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.render.ShaderProgram;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ShaderProgram.class)
public class ShaderProgramMixin {
	@ModifyVariable(
		method = "<init>",
		at = @At("STORE"),
		ordinal = 0
	)
	private Identifier buffered$createShaderProgram(Identifier original, ResourceFactory factory, String name, VertexFormat format) {
		Identifier identifier = new Identifier(name);
		if(!identifier.getNamespace().equals("minecraft")) {
			return identifier;
		}
		return original;
	}
}
