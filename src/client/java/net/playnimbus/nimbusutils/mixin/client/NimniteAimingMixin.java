package net.playnimbus.nimbusutils.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Input;
import net.playnimbus.nimbusutils.modules.nimnite.NimniteKeybinds;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.playnimbus.nimbusutils.NimbusUtilsClient.CONFIG;
import static net.playnimbus.nimbusutils.NimbusUtilsClient.NIMNITE;
import static net.playnimbus.nimbusutils.modules.nimnite.NimniteKeybinds.adsActive;

@Mixin(LocalPlayer.class)
public abstract class NimniteAimingMixin {

    @Shadow public ClientInput input;
    @Final @Shadow protected Minecraft minecraft;
    @Unique private boolean lastRightClick = false;

    @Inject(method = "tick", at = @At("HEAD"))
    private void forceSneak(CallbackInfo ci) {
        if (!NIMNITE.isEnabled() || minecraft.player == null) return;

        boolean rightClick = NimniteKeybinds.isRightClickHeld();

        if (CONFIG.nimniteToggleAds) {
            if (rightClick && !lastRightClick) {
                adsActive.set(!adsActive.get());
            }
            lastRightClick = rightClick;
        } else { // hold ads
            adsActive.set(rightClick);
        }

        boolean shouldSneak =
                NIMNITE.isHoldingGun() &&
                        adsActive.get();

        // todo: maybe find a different way of doing this
        // modify player input to force players sneak state.
        var pi = input.keyPresses;
        if (shouldSneak) {
            minecraft.options.keyShift.setDown(true);
            input.keyPresses = new Input(pi.forward(), pi.backward(), pi.left(), pi.right(), pi.jump(), true, pi.sprint());
        } else if (pi.shift()) {
            minecraft.options.keyShift.setDown(false);
            input.keyPresses = new Input(pi.forward(), pi.backward(), pi.left(), pi.right(), pi.jump(), false, pi.sprint());
        }
    }
}