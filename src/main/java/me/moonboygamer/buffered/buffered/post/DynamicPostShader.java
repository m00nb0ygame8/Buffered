package me.moonboygamer.buffered.buffered.post;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.moonboygamer.buffered.program.BufferedProgramShader;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DynamicPostShader {
	private final String name;
	private final Map<String, TargetDef> targets = new LinkedHashMap<>();
	private final List<PassDef> passes = new ArrayList<>();

	public DynamicPostShader(String name) {
		this.name = name;
	}

	public DynamicPostShader addTarget(String name) {
		targets.put(name, null);
		return this;
	}

	public DynamicPostShader addTarget(String name, int width, int height) {
		if (targets.containsKey(name)) throw new IllegalArgumentException(name + " already defined");
		targets.put(name, new TargetDef(name, width, height));
		return this;
	}

	public PassBuilder addPass(BufferedProgramShader shader) {
		PassDef pass = new PassDef(shader);
		passes.add(pass);
		return new PassBuilder(this, pass);
	}

	public Map<String, TargetDef> getTargets() { return targets; }
	public List<PassDef> getPasses() { return passes; }
	public String getName() { return name; }

	public record TargetDef(String name, int width, int height) {}

	public static class PassDef {
		public final BufferedProgramShader shader;
		public final String passName;
		public String inTarget;
		public String outTarget;
		public final Map<String, String> auxTargets = new LinkedHashMap<>();
		public final List<UniformDef> uniforms = new ArrayList<>();
		public PassDef(BufferedProgramShader shader) {
			this.shader = shader;
			this.passName = shader.getShader().getName();
		}

		public JsonElement asJson() {
			JsonObject obj = new JsonObject();
			obj.addProperty("name", passName);
			obj.addProperty("intarget", inTarget);
			obj.addProperty("outtarget", outTarget);

			if (!auxTargets.isEmpty()) {
				JsonArray auxArray = new JsonArray();
				for (Map.Entry<String, String> entry : auxTargets.entrySet()) {
					JsonObject auxObj = new JsonObject();
					auxObj.addProperty("name", entry.getKey());
					auxObj.addProperty("id", entry.getValue());
					auxArray.add(auxObj);
				}
				obj.add("auxtargets", auxArray);
			}

			// Uniforms
			if (!uniforms.isEmpty()) {
				JsonArray uniformsArray = new JsonArray();
				for (UniformDef u : uniforms) {
					JsonObject uObj = new JsonObject();
					uObj.addProperty("name", u.name);
					uObj.addProperty("type", u.type);
					uObj.addProperty("count", u.count);
					uObj.add("values", u.values);
					uniformsArray.add(uObj);
				}
				obj.add("uniforms", uniformsArray);
			}

			return obj;
		}

	}

	public static class UniformDef {
		public final String name, type;
		public final int count;
		public final JsonArray values;
		public UniformDef(String name, String type, int count, JsonArray values) {
			this.name = name; this.type = type; this.count = count; this.values = values;
		}
	}

	public static class PassBuilder {
		private final DynamicPostShader parent;
		private final PassDef pass;
		PassBuilder(DynamicPostShader parent, PassDef pass) {
			this.parent = parent; this.pass = pass;
		}
		public PassBuilder in(String target) { pass.inTarget = target; return this; }
		public PassBuilder out(String target) { pass.outTarget = target; return this; }
		public PassBuilder aux(String name, String texId) {
			pass.auxTargets.put(name, texId); return this;
		}
		public PassBuilder uniform(String name, String type, int count, JsonArray values) {
			pass.uniforms.add(new UniformDef(name, type, count, values));
			return this;
		}
		public DynamicPostShader done() { return parent; }
	}
}
