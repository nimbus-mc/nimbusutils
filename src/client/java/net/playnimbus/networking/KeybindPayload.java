package net.playnimbus.networking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.playnimbus.NimbusUtils;

public record KeybindPayload(String key) implements CustomPayload {
	public static final Identifier KEYBIND_PAYLOAD_ID = Identifier.of(NimbusUtils.MOD_ID, "keybind");
	public static final CustomPayload.Id<KeybindPayload> ID = new CustomPayload.Id<>(KEYBIND_PAYLOAD_ID);
	public static final PacketCodec<PacketByteBuf, KeybindPayload> CODEC = PacketCodec.tuple(
			PacketCodecs.STRING, KeybindPayload::key,
			KeybindPayload::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
