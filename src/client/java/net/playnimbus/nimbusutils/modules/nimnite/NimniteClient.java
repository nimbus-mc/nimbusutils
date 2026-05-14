package net.playnimbus.nimbusutils.modules.nimnite;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.playnimbus.nimbusutils.NimbusUtils;
import net.playnimbus.nimbusutils.NimbusUtilsClient;
import net.playnimbus.nimbusutils.events.HotbarChangeEvent;
import net.playnimbus.nimbusutils.events.SwapHandsEvent;
import net.playnimbus.nimbusutils.mixin.client.StartPredictionAccessor;
import org.jetbrains.annotations.UnknownNullability;
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
        var customData = item.get(DataComponents.CUSTOM_DATA);
        if (customData != null) {
            var nbt = customData.copyTag();
            return nbt.getBoolean("fort:item/is_gun").orElse(Boolean.FALSE);
        }

        return false;
    }

    private void onHotbarChange(Player player, int oldSlot, int newSlot) {
        if (!isEnabled()) return;
        ItemStack stack = player.getInventory().getItem(newSlot);

        this.setHoldingGun(isItemAGun(stack));
    }

    private void onSwapHands(LocalPlayer player, ItemStack mainHand, ItemStack offHand) {
        if (isEnabled()) {
            this.setHoldingGun(isItemAGun(mainHand));
        }
    }

    public void tick(Minecraft client) {
        if (isEnabled() && client.player != null) {
            tickInternal(client);

            if (client.gameMode != null && holdingGun) {
                if (shouldShoot) {
                    ((StartPredictionAccessor) client.gameMode).invokeStartPrediction(client.level, (sequence -> new ServerboundUseItemPacket(InteractionHand.MAIN_HAND, sequence, client.player.getYRot(), client.player.getXRot())));
//                    LOGGER.info("sent shoot packet");
                } else if (!lastShouldShoot && adsActive.get()) {
                    if (!isLeftClickHeld()) {
                        Objects.requireNonNull(client.getConnection())
                                .send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.RELEASE_USE_ITEM, client.player.getOnPos(), client.player.getMotionDirection()));
                    }
                }
            }
        }
    }

    private void tickInternal(@UnknownNullability Minecraft client) {
        boolean isRightHeld = client.options.keyUse.isDown();
        boolean isLeftHeld = client.options.keyAttack.isDown();
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
