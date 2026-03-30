package net.playnimbus;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.MinecraftClient;
import net.playnimbus.keybinds.Keybind;
import net.playnimbus.keybinds.NimniteKeybinds;
import net.playnimbus.networking.HandshakePayload;
import net.playnimbus.networking.HandshakeState;
import net.playnimbus.networking.KeybindPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NimbusUtilsClient implements ClientModInitializer {
	private static Logger LOGGER = LoggerFactory.getLogger(NimbusUtils.MOD_ID);
	public static boolean ENABLED = false;

	@Override
	public void onInitializeClient() {
		// register packets
		PayloadTypeRegistry.playC2S().register(HandshakePayload.ID, HandshakePayload.CODEC);
		PayloadTypeRegistry.playS2C().register(HandshakePayload.ID, HandshakePayload.CODEC);
		PayloadTypeRegistry.playC2S().register(KeybindPayload.ID, KeybindPayload.CODEC);

		// handle incoming handshake packet
		ClientPlayNetworking.registerGlobalReceiver(HandshakePayload.ID, (payload, ctx) -> {
			MinecraftClient client = ctx.client();

			client.execute(() -> {
				HandshakeState state = HandshakeState.getFromState(payload.state());

				if (state == HandshakeState.SUCCESS) {
					ENABLED = true;
				}

				LOGGER.info("handshake received: {}", ENABLED);
			});
		});

		// send handshake packet on server join
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			HandshakePayload handshake = new HandshakePayload(HandshakeState.JOINED.getState());

			ClientPlayNetworking.send(handshake);
		});

		// disable NimbusUtils on disconnect
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			if (ENABLED) ENABLED = false;
		});

		// register keybinds
		NimniteKeybinds.registerAll();
		ClientTickEvents.END_CLIENT_TICK.register(Keybind::tickAll);
	}
}