package net.playnimbus.nimbusutils.mixin.client;

import net.minecraft.world.entity.player.Inventory;
import net.playnimbus.nimbusutils.events.HotbarChangeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Inventory.class)
public abstract class HotbarChangeMixin {
    @Shadow
    private int selected;

    @Inject(method = "setSelectedSlot", at = @At(value = "HEAD"))
    private void emitHotbarEvent(int selected, CallbackInfo ci) {
        Inventory inv = (Inventory)(Object)this;
        HotbarChangeEvent.EVENT.invoker().onHotbarChange(inv.player, this.selected, selected);
    }
}
