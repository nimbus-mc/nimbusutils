package net.playnimbus.nimbusutils.network.nimnite;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.playnimbus.nimbusutils.NimbusUtils;
import net.playnimbus.nimbusutils.network.Packet;
import org.jspecify.annotations.NonNull;

public record NimniteDamageIndicatorPacket(Component component) implements CustomPacketPayload, Packet {
    public static final Identifier DAMAGE_INDICATOR_PAYLOAD_ID = Identifier.fromNamespaceAndPath(NimbusUtils.MOD_ID, "nimnite/damage_indicator");
    public static final CustomPacketPayload.Type<NimniteDamageIndicatorPacket> TYPE = new CustomPacketPayload.Type<>(DAMAGE_INDICATOR_PAYLOAD_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, NimniteDamageIndicatorPacket> CODEC = StreamCodec.composite(
            ComponentSerialization.STREAM_CODEC, NimniteDamageIndicatorPacket::component,
            NimniteDamageIndicatorPacket::new
    );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @SuppressWarnings("unchecked")
    @Override
    public StreamCodec<RegistryFriendlyByteBuf, NimniteDamageIndicatorPacket> codec() {
        return CODEC;
    }
}