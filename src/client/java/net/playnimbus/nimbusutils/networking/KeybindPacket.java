package net.playnimbus.nimbusutils.networking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.playnimbus.nimbusutils.NimbusUtils;

public record KeybindPacket(String key) implements CustomPayload {
	public static final Identifier KEYBIND_PAYLOAD_ID = Identifier.of(NimbusUtils.MOD_ID, "keybind");
	public static final CustomPayload.Id<KeybindPacket> ID = new CustomPayload.Id<>(KEYBIND_PAYLOAD_ID);
	public static final PacketCodec<PacketByteBuf, KeybindPacket> CODEC = PacketCodec.tuple(
			PacketCodecs.STRING, KeybindPacket::key,
			KeybindPacket::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
