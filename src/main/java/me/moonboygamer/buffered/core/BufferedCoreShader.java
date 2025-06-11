package me.moonboygamer.buffered.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.shader.GlUniform;
import me.moonboygamer.buffered.program.BufferedShader;
import net.minecraft.client.gl.PostProcessShader;
import net.minecraft.client.gl.ShaderParseException;
import net.minecraft.client.render.ShaderProgram;

import java.util.ArrayList;
import java.util.List;

public class BufferedCoreShader implements BufferedShader {
	private final Uniforms uniforms;
	private final ShaderProgram shader;

	public BufferedCoreShader(ShaderProgram shader) {
		this.shader = shader;
		this.uniforms = new Uniforms();
	}

	public ShaderProgram getShader() {
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

		public GlUniform getUniform(String name, ShaderProgram shader) {
			if(!compiled) compileUniforms(shader);
			return shader.getUniform(name);
		}

		public void compileUniforms(ShaderProgram shader) {
			for(JsonElement element : uniforms) {
				try {
					shader.addUniform(element);
				} catch (ShaderParseException e) {
					throw new RuntimeException(e);
				}
			}
			compiled = true;
		}
	}
}
