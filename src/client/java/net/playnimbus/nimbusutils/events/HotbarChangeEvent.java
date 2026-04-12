package net.playnimbus.nimbusutils.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;

public interface HotbarChangeEvent {
    Event<HotbarChangeEvent> EVENT = EventFactory.createArrayBacked(
            HotbarChangeEvent.class,
            (listeners) -> (player, oldSlot, newSlot) -> {
                for (HotbarChangeEvent listener : listeners) {
                    listener.onHotbarChange(player, oldSlot, newSlot);
                }
            }
    );

    void onHotbarChange(PlayerEntity player, int oldSlot, int newSlot);
}
