package net.playnimbus.nimbusutils.networking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.playnimbus.NimbusUtils;

/**
 * The Handshake packet works in both C2S and S2C.
 * C2S: we do not care about serverType
 * S2C: we use serverType to enable and disable modules.
 *
 * @param state byte representation of {@link HandshakeState}
 * @param serverType byte representation of server's ServerType
 */
public record HandshakePacket(byte state, byte serverType) implements CustomPayload {
	public static final Identifier HANDSHAKE_PAYLOAD_ID = Identifier.of(NimbusUtils.MOD_ID, "handshake");
	public static final CustomPayload.Id<HandshakePacket> ID = new CustomPayload.Id<>(HANDSHAKE_PAYLOAD_ID);
	public static final PacketCodec<PacketByteBuf, HandshakePacket> CODEC = PacketCodec.tuple(
			PacketCodecs.BYTE, HandshakePacket::state,
			PacketCodecs.BYTE, HandshakePacket::serverType,
			HandshakePacket::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
