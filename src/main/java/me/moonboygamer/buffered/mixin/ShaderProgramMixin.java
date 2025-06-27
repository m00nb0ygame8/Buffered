package me.moonboygamer.buffered.mixin;

import me.moonboygamer.buffered.BufferedConstants;
import net.minecraft.client.render.ShaderProgram;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@Mixin(ShaderProgram.class)
public class ShaderProgramMixin {

	@Redirect(
		method = "<init>",
		at = @At(
			value = "NEW",
			target = "net/minecraft/util/Identifier"
		)
	)
	private Identifier buffered$createShaderProgram(String id) {
		String strippedId = id.replace("shaders/core/", "").replaceFirst(".json", "");
		if(BufferedConstants.DEBUG) BufferedConstants.LOGGER.warn(strippedId);
		if(Objects.requireNonNull(Identifier.tryParse(strippedId)).getNamespace().equals("minecraft")) {
			Identifier identifier = new Identifier(id);
			if(BufferedConstants.DEBUG) BufferedConstants.LOGGER.warn("Default identifier: {}", identifier);
			return identifier;
		} else {
			Identifier identifier = Identifier.tryParse(strippedId);
			if(BufferedConstants.DEBUG) BufferedConstants.LOGGER.warn("Parsed identifier: {}", identifier);
			return identifier;
		}
	}
}
