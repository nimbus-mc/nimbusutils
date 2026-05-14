package net.playnimbus.nimbusutils.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.playnimbus.nimbusutils.NimbusUtils;
import net.playnimbus.nimbusutils.events.SwapHandsEvent;
import net.playnimbus.nimbusutils.modules.nimnite.NimniteKeybinds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.playnimbus.nimbusutils.NimbusUtilsClient.NIMNITE;

@Mixin(Minecraft.class)
public abstract class MinecraftClientMixin {
    @Unique private final Logger LOGGER = LoggerFactory.getLogger(NimbusUtils.MOD_ID);

    @Shadow
    private int rightClickDelay;

    @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
    private void cancelAttack(CallbackInfoReturnable<Boolean> cir) {
        if (NIMNITE.isEnabled() && NIMNITE.isHoldingGun()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "startUseItem", at = @At("HEAD"), cancellable = true)
    private void cancelItemUse(CallbackInfo ci) {
        if (NIMNITE.isEnabled() && NIMNITE.isHoldingGun() && NimniteKeybinds.adsActive.get()) {
            LOGGER.info("cancelled");
            ci.cancel();
        }
    }

    @Inject(method = "handleKeybinds", at = @At("HEAD"))
    private void disableGunOnSwap(CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        if (client.options.keySwapOffhand.isDown()) {
            // The swap hands key was just pressed
            LocalPlayer player = client.player;
            if (player != null) {
                ItemStack mainHand = player.getMainHandItem();
                ItemStack offHand = player.getOffhandItem();
                SwapHandsEvent.EVENT.invoker().onSwapHands(player, mainHand, offHand);
            }
        }
    }

    @Redirect(
            method = "handleKeybinds",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;startUseItem()V"
            )
    )
    private void redirectDoItemUse(Minecraft instance) {
        if (NIMNITE.isEnabled() && NIMNITE.isHoldingGun()) {
            this.rightClickDelay = 4;
            return;
        }
        ((MinecraftClientAccessor) instance).invokeStartUseItem();
    }
}
