package net.playnimbus.nimbusutils.network;

import net.minecraft.network.chat.Component;
import net.playnimbus.nimbusutils.network.nimnite.NimniteDamageIndicatorPacket;

import java.util.HashSet;
import java.util.Set;

public final class PacketRegistry {
    private static Set<Packet> registry = new HashSet<>();

    public static void registerAll() {
        registry.add(new HandshakePacket((byte) 0, (byte) 0));
        registry.add(new KeybindPacket(""));
        registry.add(new NimniteDamageIndicatorPacket(Component.empty()));

        // register all entries
        for (var entry : registry) {
            if (entry.serverbound())
                entry.registerC2S();

            if (entry.clientbound())
                entry.registerS2C();
        }

        registry.clear();
    }
}
