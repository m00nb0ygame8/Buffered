package me.moonboygamer.buffered.sampler;

import java.util.ArrayList;
import java.util.List;

public class BufferedSamplerManager {
	public static final BufferedSamplerManager MAIN_PIPELINE = new BufferedSamplerManager();

	private final List<me.moonboygamer.buffered.sampler.BufferedSampler> samplers = new ArrayList<>();

	public void addSampler(me.moonboygamer.buffered.sampler.BufferedSampler sampler) { samplers.add(sampler); }
	public me.moonboygamer.buffered.sampler.BufferedSampler getSampler(String name) { return samplers.stream().filter((sampler) -> sampler.name.equals(name)).findFirst().orElse(null); }

	public void bindAll() {
		for(int unit = 0; unit < samplers.size(); unit++) {
			samplers.get(unit).bind(unit);
		}
	}
	public void clear() { samplers.clear(); }
	public List<BufferedSampler> getSamplers() { return List.copyOf(samplers); }
}
