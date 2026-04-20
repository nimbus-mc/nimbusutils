package net.playnimbus.nimbusutils.nimnite;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Hand;
import net.playnimbus.NimbusUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.playnimbus.nimbusutils.nimnite.NimniteKeybinds.*;

public class NimniteClient {
    private final static Logger LOGGER = LoggerFactory.getLogger(NimbusUtils.MOD_ID);
    private boolean enabled = false;
    private boolean holdingGun = false;
    private boolean ads = false;

    public NimniteClient() {
//        HotbarChangeEvent.EVENT.register(this::onHotbarChange);
        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
    }

//    private void onHotbarChange(PlayerEntity player, int oldSlot, int newSlot) {
//        if (!isEnabled()) return;
//    }

    public void tick(MinecraftClient client) {
        if (isEnabled() && client.player != null) {
            boolean isRightHeld = client.options.useKey.isPressed();
            boolean isLeftHeld = client.options.attackKey.isPressed();
            rightClickHeld.set(isRightHeld);
            leftClickHeld.set(isLeftHeld);

            if (client.interactionManager != null && isLeftHeld && holdingGun) {
                client.interactionManager.interactItem(client.player, Hand.MAIN_HAND);
            }
        }
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
        return enabled;
    }
}
