package net.playnimbus.nimbusutils.mixin.client;

import net.minecraft.entity.player.PlayerInventory;
import net.playnimbus.nimbusutils.events.HotbarChangeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerInventory.class)
public abstract class HotbarChangeMixin {
    @Shadow
    private int selectedSlot;

    @Inject(method = "setSelectedSlot", at = @At(value = "HEAD"))
    private void emitHotbarEvent(int slot, CallbackInfo ci) {
        PlayerInventory inv = (PlayerInventory)(Object)this;
        HotbarChangeEvent.EVENT.invoker().onHotbarChange(inv.player, this.selectedSlot, slot);
    }
}
