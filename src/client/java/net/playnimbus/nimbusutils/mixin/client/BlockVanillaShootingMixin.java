package net.playnimbus.nimbusutils.mixin.client;

import net.minecraft.client.player.LocalPlayer;
import net.playnimbus.nimbusutils.modules.nimnite.NimniteKeybinds;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.playnimbus.nimbusutils.NimbusUtilsClient.NIMNITE;

@Mixin(LocalPlayer.class)
public abstract class BlockVanillaShootingMixin {
    @Inject(method = "isHandsBusy", at = @At("HEAD"), cancellable = true)
    public void cancelUsingItem(CallbackInfoReturnable<Boolean> cir) {
        if (NIMNITE.isEnabled() && NIMNITE.isHoldingGun() && NimniteKeybinds.adsActive.get())
            cir.setReturnValue(true);
    }
}
