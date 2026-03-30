package net.playnimbus.networking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.playnimbus.NimbusUtils;

// this packet informs the server that you are using the NimbusUtils mod
public record HandshakePayload(byte state) implements CustomPayload {
	public static final Identifier HANDSHAKE_PAYLOAD_ID = Identifier.of(NimbusUtils.MOD_ID, "handshake");
	public static final CustomPayload.Id<HandshakePayload> ID = new CustomPayload.Id<>(HANDSHAKE_PAYLOAD_ID);
	public static final PacketCodec<PacketByteBuf, HandshakePayload> CODEC = PacketCodec.tuple(
			PacketCodecs.BYTE, HandshakePayload::state,
			HandshakePayload::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
