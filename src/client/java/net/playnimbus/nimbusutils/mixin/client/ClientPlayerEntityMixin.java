package net.playnimbus.nimbusutils.mixin.client;

import net.minecraft.client.network.ClientPlayerEntity;
import net.playnimbus.nimbusutils.nimnite.NimniteKeybinds;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.playnimbus.nimbusutils.NimbusUtilsClient.NIMNITE;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {
    @Inject(method = "isUsingItem", at = @At("HEAD"), cancellable = true)
    public void cancelUsingItem(CallbackInfoReturnable<Boolean> cir) {
        if (NIMNITE.isEnabled() && NIMNITE.isHoldingGun() && NimniteKeybinds.adsActive.get())
            cir.setReturnValue(false);
    }
}
