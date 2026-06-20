package net.playnimbus.nimbusutils.network;

import net.playnimbus.nimbusutils.network.nimnite.NimniteDamageInfoPacket;

import java.util.HashSet;
import java.util.Set;

public final class PacketRegistry {
    private static Set<Packet> registry = new HashSet<>();

    public static void registerAll() {
        registry.add(new HandshakePacket((byte) 0, (byte) 0));
        registry.add(new KeybindPacket(""));
        registry.add(new NimniteDamageInfoPacket(0, 0));

        // register all entries
        for (var entry : registry) {
            if (entry.serverbound())
                entry.registerC2S();

            if (entry.clientbound())
                entry.registerS2C();
        }

        registry = null;
    }
}
