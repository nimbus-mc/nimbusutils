package net.playnimbus.nimbusutils;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.MinecraftClient;
import net.playnimbus.NimbusUtils;
import net.playnimbus.nimbusutils.nimnite.NimniteClient;
import net.playnimbus.nimbusutils.nimnite.NimniteKeybinds;
import net.playnimbus.nimbusutils.networking.HandshakePacket;
import net.playnimbus.nimbusutils.networking.HandshakeState;
import net.playnimbus.nimbusutils.networking.KeybindPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NimbusUtilsClient implements ClientModInitializer {
	private static final Logger LOGGER = LoggerFactory.getLogger(NimbusUtils.MOD_ID);
	public static HandshakeState STATE = HandshakeState.NONE;

	public static NimniteClient NIMNITE = new NimniteClient();

	public static ModConfig CONFIG;

	@Override
	public void onInitializeClient() {
		// register and acquire config
		AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
		CONFIG = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

		// register packets
		PayloadTypeRegistry.playC2S().register(HandshakePacket.ID, HandshakePacket.CODEC);
		PayloadTypeRegistry.playS2C().register(HandshakePacket.ID, HandshakePacket.CODEC);
		PayloadTypeRegistry.playC2S().register(KeybindPacket.ID, KeybindPacket.CODEC);

		// handle incoming handshake packet
		ClientPlayNetworking.registerGlobalReceiver(HandshakePacket.ID, (payload, ctx) -> {
			MinecraftClient client = ctx.client();

			client.execute(() -> {
				HandshakeState state = HandshakeState.getFromState(payload.state());
				STATE = state;

				switch (state) {
					case NIMNITE -> NIMNITE.setEnabled(true);
				}

				LOGGER.info("handshake received: {}", state);
			});
		});

		// send handshake packet on server join
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			HandshakePacket handshake = new HandshakePacket(HandshakeState.CONNECTING.getState(), CONFIG.modEnabled);
			ClientPlayNetworking.send(handshake);
		});

		// disable NimbusUtils on disconnect
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			STATE = HandshakeState.NONE;

			NIMNITE.setEnabled(false);
		});

		// register everything
		NimniteKeybinds.registerAll();
		ClientTickEvents.END_CLIENT_TICK.register(Keybind::tickAll);
		ClientTickEvents.END_CLIENT_TICK.register(NIMNITE::tick);
	}
}