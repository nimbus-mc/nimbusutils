package net.playnimbus.nimbusutils.events;


import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface SwapHandsEvent {
    Event<SwapHandsEvent> EVENT = EventFactory.createArrayBacked(
            SwapHandsEvent.class,
            (listeners) -> (player, mainHand, offHand) -> {
                for (SwapHandsEvent listener : listeners) {
                    listener.onSwapHands(player, mainHand, offHand);
                }
            }
    );

    void onSwapHands(@NotNull LocalPlayer player, ItemStack mainHand, ItemStack offHand);
}
