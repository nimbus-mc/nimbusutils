package net.playnimbus.nimbusutils.nimnite;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.player.ItemEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.playnimbus.NimbusUtils;
import net.playnimbus.nimbusutils.ModConfig;
import net.playnimbus.nimbusutils.NimbusUtilsClient;
import net.playnimbus.nimbusutils.events.HotbarChangeEvent;
import net.playnimbus.nimbusutils.mixin.client.MinecraftClientAccessor;
import net.playnimbus.nimbusutils.mixin.client.SendSequencedPacketAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import static net.playnimbus.nimbusutils.nimnite.NimniteKeybinds.*;

public class NimniteClient {
    private final static Logger LOGGER = LoggerFactory.getLogger(NimbusUtils.MOD_ID);
    private boolean enabled = false;
    private boolean holdingGun = false;
    private boolean canShoot = false;
    private boolean lastLeftHeld = false;
    private boolean shouldShoot = false;
    private boolean lastShouldShoot = false;
    private AtomicBoolean threadActive = new AtomicBoolean(false);

    public NimniteClient() {
        HotbarChangeEvent.EVENT.register(this::onHotbarChange);
        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
    }

    private void onHotbarChange(PlayerEntity player, int oldSlot, int newSlot) {
        if (!isEnabled()) return;
        ItemStack stack = player.getInventory().getStack(newSlot);

        var customData = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (customData != null) {
            var nbt = customData.copyNbt();
            this.setHoldingGun(nbt.getBoolean("fort:item/is_gun", false));
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

    public boolean canShoot() {
        return canShoot;
    }

    public void setCanShoot(boolean canShoot) {
        this.canShoot = canShoot;
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
