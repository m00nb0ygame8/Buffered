package me.moonboygamer.buffered.buffered.program;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.shader.GlUniform;
import me.moonboygamer.buffered.program.BufferedShader;
import net.minecraft.client.gl.PostProcessShader;
import net.minecraft.client.gl.ShaderParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class BufferedProgramShader implements BufferedShader {
	private final Uniforms uniforms;
	private final PostProcessShader shader;

	public BufferedProgramShader(PostProcessShader shader) {
		this.shader = shader;
		this.uniforms = new Uniforms();
	}

	public PostProcessShader getShader() {
		return shader;
	}

	public void addUniform(String name, String type, int count, JsonArray values) {
		uniforms.addUniform(name, type, count, values);
	}

	public GlUniform getUniform(String name) {
		return uniforms.getUniform(name, shader);
	}
	public boolean hasUniform(String name) {
		return uniforms.hasUniform(name, shader);
	}
	public void hasUniform(String name, Consumer<GlUniform> consumer) {
		uniforms.hasUniform(name, shader, consumer);
	}

	public void compileUniforms() {
		uniforms.compileUniforms(shader);
	}

	public void markUniformsDirty() { uniforms.markDirty(); }

	private static class Uniforms {
		private List<JsonElement> uniforms = new ArrayList<>();
		private boolean compiled = false;

		public void addUniform(String name, String type, int count, JsonArray values) {
			JsonObject element = new JsonObject();
			element.addProperty("name", name);
			element.addProperty("type", type);
			element.addProperty("count", count);
			element.add("values", values);
			uniforms.add(element);
		}

		public GlUniform getUniform(String name, PostProcessShader shader) {
			if (!compiled) compileUniforms(shader);
			return shader.getProgram().getUniformByName(name);
		}
		public boolean hasUniform(String name, PostProcessShader shader) {
			if(!compiled) compileUniforms(shader);
			return shader.getProgram().getUniformByName(name) != null;
		}
		public void hasUniform(String name, PostProcessShader shader, Consumer<GlUniform> consumer) {
			if(!compiled) compileUniforms(shader);
			Optional.ofNullable(shader.getProgram().getUniformByName(name)).ifPresent(consumer);
		}

		public void compileUniforms(PostProcessShader shader) {
			for(JsonElement element : uniforms) {
				try {
					shader.getProgram().addUniform(element);
				} catch (ShaderParseException e) {
					throw new RuntimeException(e);
				}
			}
			compiled = true;
		}
		public void markDirty() { compiled = false; }
	}
}
