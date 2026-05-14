package net.playnimbus.nimbusutils.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Player;

public interface HotbarChangeEvent {
    Event<HotbarChangeEvent> EVENT = EventFactory.createArrayBacked(
            HotbarChangeEvent.class,
            (listeners) -> (player, oldSlot, newSlot) -> {
                for (HotbarChangeEvent listener : listeners) {
                    listener.onHotbarChange(player, oldSlot, newSlot);
                }
            }
    );

    void onHotbarChange(Player player, int oldSlot, int newSlot);
}
