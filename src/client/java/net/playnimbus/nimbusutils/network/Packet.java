package net.playnimbus.nimbusutils.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface Packet {
    default void registerC2S() {
        PayloadTypeRegistry.serverboundPlay().register(type(), codec());
    }

    default void registerS2C() {
        PayloadTypeRegistry.clientboundPlay().register(type(), codec());
    }

    default boolean serverbound() {
        return true;
    }

    default boolean clientbound() {
        return true;
    }

    CustomPacketPayload.Type<? extends CustomPacketPayload> type();
    <T> StreamCodec<? super RegistryFriendlyByteBuf, T> codec();
}
