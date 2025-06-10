package me.moonboygamer.buffered.post;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.framebuffer.Framebuffer;
import com.mojang.blaze3d.shader.GlUniform;
import net.minecraft.client.gl.PostProcessShader;
import net.minecraft.client.gl.ShaderParseException;

import java.util.List;

public class BufferedPostShader {
	private final Uniforms uniforms;
	private final PostProcessShader shader;

	public BufferedPostShader(PostProcessShader shader) {
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

	public void compileUniforms() {
		uniforms.compileUniforms(shader);
	}

	private static class Uniforms {
		private List<JsonElement> uniforms;
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
			if(!compiled) compileUniforms(shader);
			return shader.getProgram().getUniformByName(name);
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
	}
}
