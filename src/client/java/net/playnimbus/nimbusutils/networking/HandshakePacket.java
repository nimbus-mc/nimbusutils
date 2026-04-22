package net.playnimbus.nimbusutils.networking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.playnimbus.NimbusUtils;

// this packet informs the server that you are using the NimbusUtils mod
public record HandshakePacket(byte state, boolean enabled) implements CustomPayload {
	public static final Identifier HANDSHAKE_PAYLOAD_ID = Identifier.of(NimbusUtils.MOD_ID, "handshake");
	public static final CustomPayload.Id<HandshakePacket> ID = new CustomPayload.Id<>(HANDSHAKE_PAYLOAD_ID);
	public static final PacketCodec<PacketByteBuf, HandshakePacket> CODEC = PacketCodec.tuple(
			PacketCodecs.BYTE, HandshakePacket::state,
			PacketCodecs.BOOLEAN, HandshakePacket::enabled,
			HandshakePacket::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
