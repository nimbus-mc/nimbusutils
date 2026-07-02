package net.playnimbus.nimbusutils.modules.nimnite.hud;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.playnimbus.nimbusutils.modules.nimnite.ClientPackFonts;

// todo: fix yaw text scaling (currently yaw text is big asf on small windows/monitors)
public final class CompassElement implements HudElement {
    private static final char COMPASS_ICON_CHAR = '\u0997';

    private static final int TEXT_TYPE = 0;
    private static final int ICON_TYPE = 1;
    private static final int WHITE = 0xFFFFFFFF;

    private static final int TEXT_Y = 4;
    private static final int ICON_Y = 14;

    private static final int X_OFFSET = 0;

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null) return;

        float partialTick = deltaTracker.getGameTimeDeltaPartialTick(true);
        float yaw = Mth.wrapDegrees(player.getViewYRot(partialTick)) + 180;

        Font font = minecraft.font;
        int centerX = minecraft.getWindow().getGuiScaledWidth() / 2 + X_OFFSET;

        // --- Yaw number ---
        String yawText = String.valueOf(Math.round(yaw));
        graphics.centeredText(font, Component.literal(yawText), centerX, TEXT_Y, WHITE);

        // --- Compass icon ---
        Component icon = Component.literal(String.valueOf(COMPASS_ICON_CHAR))
                .setStyle(Style.EMPTY.withFont(ClientPackFonts.ICONS));
        int iconShadow = packYawColor(yaw, ICON_TYPE);
        drawTextWithPackedShadow(graphics, font, icon, centerX, ICON_Y, WHITE, iconShadow);
    }

    private static void drawTextWithPackedShadow(
            GuiGraphicsExtractor graphics, Font font, Component text,
            int x, int y, int color, int packedShadowColor) {
        graphics.centeredText(font, text, x + 1, y + 1, packedShadowColor);
        graphics.centeredText(font, text, x, y, color);
    }

    /**
     * R = firstHalf, G = type, B = secondHalf, A = decimal (0-99).
     * Matches the existing getYawFromColor(vec4) shader decode exactly.
     */
    private static int packYawColor(float yaw, int type) {
        int yawWhole = (int) Math.floor(yaw);
        int firstHalf = yawWhole / 2;
        int secondHalf = Math.max(yawWhole - firstHalf, 0);
        int decimal = Math.max((int) ((yaw % 1) * 100), 1); // avoid alpha = 0
        return (decimal << 24) | (firstHalf << 16) | (type << 8) | secondHalf;
    }
}