package me.moonboygamer.buffered.mixin;

import me.moonboygamer.buffered.BufferedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.JsonEffectGlShader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.FileNotFoundException;
import java.util.Objects;

@Mixin(JsonEffectGlShader.class)
public class JsonGlShaderMixin {
	@Redirect(
		method = "<init>",
		at = @At(
			value = "NEW",
			target = "(Ljava/lang/String;)Lnet/minecraft/util/Identifier;"
		)
	)
	private Identifier buffered$createGlShader(String id) {
		String strippedId = id.replace("shaders/program/", "").replaceFirst(".json", "");
		if(BufferedConstants.DEBUG) BufferedConstants.LOGGER.warn(strippedId);
		if(Objects.requireNonNull(Identifier.tryParse(strippedId)).getNamespace().equals("minecraft")) {
			Identifier identifier = Identifier.tryParse(id);
			if(BufferedConstants.DEBUG) BufferedConstants.LOGGER.warn("Default identifier: {}", identifier);
			return identifier;
		} else {
			Identifier identifier = Identifier.tryParse(strippedId);
			if(BufferedConstants.DEBUG) BufferedConstants.LOGGER.warn("Parsed identifier: {}", identifier);
			return identifier;
		}
	}
	@Redirect(
		method = "loadEffect",
		at = @At(
			value = "NEW",
			target = "Lnet/minecraft/util/Identifier;"
		)
	)
	private static Identifier buffered$loadEffect(String id) {
		String strippedId = id.replace("shaders/program/", "");
		ResourceManager manager = MinecraftClient.getInstance().getResourceManager();
		if(BufferedConstants.DEBUG) BufferedConstants.LOGGER.warn(strippedId);
		if (Objects.requireNonNull(Identifier.tryParse(strippedId)).getNamespace().equals("minecraft")) {
			Identifier identifier = Identifier.tryParse(id);
			if(BufferedConstants.DEBUG) BufferedConstants.LOGGER.warn("Default program identifier: {}", identifier);
			try {
				manager.getResourceOrThrow(identifier);
			} catch (FileNotFoundException e) {
				throw new RuntimeException("Shader program not found: " + identifier, e);
			}
			return identifier;
		} else {
			String injectPath = "shaders/program/";
			int index = strippedId.indexOf(':');
			String specialId = strippedId;
			if (index != -1) {
				specialId = strippedId.substring(0, index + 1) + injectPath + strippedId.substring(index + 1);
			}
			Identifier identifier = Identifier.tryParse(specialId);
			if(BufferedConstants.DEBUG) BufferedConstants.LOGGER.warn("Parsed program identifier: {}", identifier);
			try {
				manager.getResourceOrThrow(identifier);
				if(BufferedConstants.DEBUG) BufferedConstants.LOGGER.warn("Shader program loaded successfully: {}", identifier);
			} catch (FileNotFoundException e) {
				throw new RuntimeException("Shader program not found: " + identifier, e);
			}
			return identifier;
		}
	}
}

