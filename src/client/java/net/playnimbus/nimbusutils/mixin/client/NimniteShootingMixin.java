package net.playnimbus.nimbusutils.mixin.client;

import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.playnimbus.nimbusutils.NimbusUtilsClient;
import net.playnimbus.nimbusutils.networking.HandshakeState;
import net.playnimbus.nimbusutils.nimnite.NimniteKeybinds;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.playnimbus.nimbusutils.NimbusUtilsClient.NIMNITE;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class NimniteShootingMixin {
    @Inject(method = "interactItem", at = @At("HEAD"), cancellable = true)
    private void onInteractItem(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (NimbusUtilsClient.STATE == HandshakeState.NIMNITE) {
            ItemStack stack = player.getStackInHand(hand);
            var customData = stack.get(DataComponentTypes.CUSTOM_DATA);

            if (customData != null) {
                var nbt = customData.copyNbt();
                boolean isGun = nbt.getBoolean("fort:item/is_gun", false);

                NIMNITE.setHoldingGun(isGun);

                boolean canShoot = isGun && NimniteKeybinds.isLeftClickHeld();
                if (isGun && !canShoot) {
                    cir.setReturnValue(ActionResult.FAIL);
                }
                return;
            }

            NIMNITE.setHoldingGun(false);
        }
    }
}
