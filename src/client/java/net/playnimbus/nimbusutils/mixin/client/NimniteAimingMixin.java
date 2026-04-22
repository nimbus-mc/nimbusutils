package net.playnimbus.nimbusutils.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.util.PlayerInput;
import net.playnimbus.nimbusutils.nimnite.NimniteKeybinds;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Objects;

import static net.playnimbus.nimbusutils.NimbusUtilsClient.CONFIG;
import static net.playnimbus.nimbusutils.NimbusUtilsClient.NIMNITE;
import static net.playnimbus.nimbusutils.nimnite.NimniteKeybinds.adsActive;

@Mixin(ClientPlayerEntity.class)
public abstract class NimniteAimingMixin {

    @Shadow public Input input;
    @Final @Shadow protected MinecraftClient client;
    @Unique private boolean lastRightClick = false;

    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void forceSneak(CallbackInfo ci) {
        if (!NIMNITE.isEnabled() || client.player == null) return;

        boolean rightClick = NimniteKeybinds.isRightClickHeld();

        if (CONFIG.toggleADS) {
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
        var pi = input.playerInput;
        if (shouldSneak) {
            client.options.sneakKey.setPressed(true);
            input.playerInput = new PlayerInput(pi.forward(), pi.backward(), pi.left(), pi.right(), pi.jump(), true, pi.sprint());
        } else if (pi.sneak()) {
            client.options.sneakKey.setPressed(false);
            input.playerInput = new PlayerInput(pi.forward(), pi.backward(), pi.left(), pi.right(), pi.jump(), false, pi.sprint());
        }
        Objects.requireNonNull(client.getNetworkHandler()).sendPacket(new PlayerInputC2SPacket(input.playerInput));
    }
}