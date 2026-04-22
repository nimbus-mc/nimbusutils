package net.playnimbus.nimbusutils.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.playnimbus.NimbusUtils;
import net.playnimbus.nimbusutils.nimnite.NimniteKeybinds;
import org.jspecify.annotations.Nullable;
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

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Unique private final Logger LOGGER = LoggerFactory.getLogger(NimbusUtils.MOD_ID);

    @Shadow
    private int itemUseCooldown;

    @Shadow
    public abstract @Nullable ClientPlayNetworkHandler getNetworkHandler();

    @Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
    private void cancelAttack(CallbackInfoReturnable<Boolean> cir) {
        if (NIMNITE.isEnabled() && NIMNITE.isHoldingGun()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "doItemUse", at = @At("HEAD"), cancellable = true)
    private void cancelItemUse(CallbackInfo ci) {
        if (NIMNITE.isEnabled() && NIMNITE.isHoldingGun() && NimniteKeybinds.adsActive.get()) {
            LOGGER.info("cancelled");
            ci.cancel();
        }
    }

    @Redirect(
            method = "handleInputEvents",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/MinecraftClient;doItemUse()V"
            )
    )
    private void redirectDoItemUse(MinecraftClient instance) {
        if (NIMNITE.isEnabled() && NIMNITE.isHoldingGun()) {
            this.itemUseCooldown = 4;
            return;
        }
        ((MinecraftClientAccessor) instance).invokeDoItemUse();
    }
}
