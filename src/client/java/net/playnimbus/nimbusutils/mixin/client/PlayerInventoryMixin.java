package net.playnimbus.nimbusutils.mixin.client;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.playnimbus.nimbusutils.NimbusUtilsClient.NIMNITE;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {
    @Inject(method = "swapStackWithHotbar", at = @At("HEAD"))
    private void disableOnBuildSwap(ItemStack stack, CallbackInfo ci) {
        var customData = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (customData != null) {
            var nbt = customData.copyNbt();
            var isHoldingGun = nbt.getBoolean("fort:item/is_gun", false);
            NIMNITE.setHoldingGun(isHoldingGun);
        }
    }
}
