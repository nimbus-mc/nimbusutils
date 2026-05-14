package net.playnimbus.nimbusutils.mixin.client;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.playnimbus.nimbusutils.NimbusUtilsClient.NIMNITE;

@Mixin(Player.class)
public class NimniteHoldingGunMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void checkHeldGun(CallbackInfo ci) {
        if (!NIMNITE.isEnabled()) return;

        Player self = (Player)(Object)this;
        if (!(self instanceof LocalPlayer)) return;

        ItemStack held = self.getMainHandItem();
        CustomData customData = held.get(DataComponents.CUSTOM_DATA);
        boolean isGun = false;
        if (customData != null) {
            var nbt = customData.copyTag();
            isGun = nbt.getBoolean("fort:item/is_gun").orElse(false);
        }
        NIMNITE.setHoldingGun(isGun);
    }
}