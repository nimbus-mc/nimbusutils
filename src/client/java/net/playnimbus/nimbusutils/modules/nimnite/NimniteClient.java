package net.playnimbus.nimbusutils.modules.nimnite;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;
import net.playnimbus.nimbusutils.NimbusUtils;
import net.playnimbus.nimbusutils.NimbusUtilsClient;
import net.playnimbus.nimbusutils.events.HotbarChangeEvent;
import net.playnimbus.nimbusutils.events.SwapHandsEvent;
import net.playnimbus.nimbusutils.mixin.client.SendSequencedPacketAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static net.playnimbus.nimbusutils.modules.nimnite.NimniteKeybinds.*;

public class NimniteClient {
    private final static Logger LOGGER = LoggerFactory.getLogger(NimbusUtils.MOD_ID);
    private boolean enabled = false;
    private boolean holdingGun = false;
    private boolean lastLeftHeld = false;
    private boolean shouldShoot = false;
    private boolean lastShouldShoot = false;

    public NimniteClient() {
        HotbarChangeEvent.EVENT.register(this::onHotbarChange);
        SwapHandsEvent.EVENT.register(this::onSwapHands);

        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
    }

    public boolean isItemAGun(ItemStack item) {
        var customData = item.get(DataComponentTypes.CUSTOM_DATA);
        if (customData != null) {
            var nbt = customData.copyNbt();
            return nbt.getBoolean("fort:item/is_gun", false);
        }

        return false;
    }

    private void onHotbarChange(PlayerEntity player, int oldSlot, int newSlot) {
        if (!isEnabled()) return;
        ItemStack stack = player.getInventory().getStack(newSlot);

        this.setHoldingGun(isItemAGun(stack));
    }

    private void onSwapHands(ClientPlayerEntity player, ItemStack mainHand, ItemStack offHand) {
        if (isEnabled()) {
            this.setHoldingGun(isItemAGun(mainHand));
        }
    }

    public void tick(MinecraftClient client) {
        if (isEnabled() && client.player != null) {
            tickInternal(client);

            if (client.interactionManager != null && holdingGun) {
                if (shouldShoot) {
                    ((SendSequencedPacketAccessor) client.interactionManager).invokeSendSequencedPacket(client.world, (sequence -> new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, sequence, client.player.getYaw(), client.player.getPitch())));
//                    LOGGER.info("sent shoot packet");
                } else if (!lastShouldShoot && adsActive.get()) {
                    if (!isLeftClickHeld()) {
                        Objects.requireNonNull(client.getNetworkHandler())
                                .sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.RELEASE_USE_ITEM, client.player.getBlockPos(), client.player.getMovementDirection()));
                    }
                }
            }
        }
    }

    private void tickInternal(MinecraftClient client) {
        boolean isRightHeld = client.options.useKey.isPressed();
        boolean isLeftHeld = client.options.attackKey.isPressed();
        rightClickHeld.set(isRightHeld);
        leftClickHeld.set(isLeftHeld);

        boolean risingEdge = isLeftHeld && !lastLeftHeld;
        lastLeftHeld = isLeftHeld;

        lastShouldShoot = shouldShoot;
        shouldShoot = isRightHeld ? risingEdge : isLeftHeld;
//        LOGGER.info("tick | isLeftHeld={} isRightHeld={} risingEdge={} shouldShoot={} sending={}",
//                isLeftHeld, isRightHeld, risingEdge, shouldShoot, shouldShoot);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        LOGGER.info("NimniteClient enabled: {}", enabled);
    }

    public void setHoldingGun(boolean holdingGun) {
        this.holdingGun = holdingGun;
    }

    public boolean isHoldingGun() {
        return this.holdingGun;
    }

    public boolean isEnabled() {
        return NimbusUtilsClient.isEnabled() && enabled;
    }
}
