package net.playnimbus.nimbusutils.networking;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.playnimbus.nimbusutils.NimbusUtils;

public record KeybindPacket(String key) implements CustomPacketPayload {
	public static final Identifier KEYBIND_PAYLOAD_ID = Identifier.fromNamespaceAndPath(NimbusUtils.MOD_ID, "keybind");
	public static final CustomPacketPayload.Type<KeybindPacket> TYPE = new CustomPacketPayload.Type<>(KEYBIND_PAYLOAD_ID);
	public static final StreamCodec<RegistryFriendlyByteBuf, KeybindPacket> CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8, KeybindPacket::key,
			KeybindPacket::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
