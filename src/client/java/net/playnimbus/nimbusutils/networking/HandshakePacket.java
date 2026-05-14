package net.playnimbus.nimbusutils.networking;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.playnimbus.nimbusutils.NimbusUtils;

/**
 * The Handshake packet works in both C2S and S2C.
 * C2S: we do not care about serverType
 * S2C: we use serverType to enable and disable modules.
 *
 * @param state byte representation of {@link HandshakeState}
 * @param serverType byte representation of server's ServerType
 */
public record HandshakePacket(byte state, byte serverType) implements CustomPacketPayload {
	public static final Identifier HANDSHAKE_PAYLOAD_ID = Identifier.fromNamespaceAndPath(NimbusUtils.MOD_ID, "handshake");
	public static final CustomPacketPayload.Type<HandshakePacket> TYPE = new CustomPacketPayload.Type<>(HANDSHAKE_PAYLOAD_ID);
	public static final StreamCodec<RegistryFriendlyByteBuf, HandshakePacket> CODEC = StreamCodec.composite(
			ByteBufCodecs.BYTE, HandshakePacket::state,
			ByteBufCodecs.BYTE, HandshakePacket::serverType,
			HandshakePacket::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
