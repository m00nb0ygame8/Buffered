package me.moonboygamer.buffered.client;

import me.moonboygamer.buffered.renderer.ShaderRenderer;
import me.moonboygamer.buffered.shader.BufferedShaderManager;
import me.moonboygamer.buffered.test.CreeperPostShader;
import me.moonboygamer.buffered.test.FisheyeRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class BufferedClient implements ClientModInitializer {
//	private static RedTriangleRenderer redTriangleRenderer;
	private static FisheyeRenderer fisheyeRenderer;
//	private static CreeperPostShader creeperPostShader;
	private static boolean init = false;
	@Override
	public void onInitializeClient(ModContainer mod) {

//		WorldRenderEvents.END.register(ctx -> {
//			if(!init && MinecraftClient.getInstance().getResourceManager() != null) {
//				redTriangleRenderer = ShaderRenderer.Factory.create(RedTriangleRenderer.class, true);
//				init = true;
//			}
//			if(init) redTriangleRenderer.render(ctx.matrixStack(), ctx.tickDelta());
//		});
		WorldRenderEvents.END.register(ctx -> {
			if(!init && MinecraftClient.getInstance().getResourceManager() != null) {
				fisheyeRenderer = ShaderRenderer.Factory.create(FisheyeRenderer.class, true);
				init = true;
			}
			if(init) fisheyeRenderer.render(ctx.matrixStack(), ctx.tickDelta());
		});
//		WorldRenderEvents.END.register(ctx -> {
//			0
//			BufferedShaderManager.renderPostShader(Identifier.tryParse("buffered:shaders/test/test_creeper.json"), null);
//		});

	}
}
