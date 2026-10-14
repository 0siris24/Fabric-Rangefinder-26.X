package net.osiris.rangefinder;

import java.util.OptionalDouble;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public final class RangefinderOverlay {

    private static final int COLOR_IN_RANGE = 0xFF55FF55;
    private static final int COLOR_OUT_OF_RANGE = 0xFFFF5555;
    private static final int COLOR_NO_TARGET = 0xFFAAAAAA;

    private RangefinderOverlay() {}

    public static void register() {
        HudElementRegistry.attachElementBefore(
                VanillaHudElements.CHAT,
                SpyglassRangefinder.id("rangefinder_overlay"),
                RangefinderOverlay::render
        );
    }

    private static void render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        Minecraft client = Minecraft.getInstance();
        LocalPlayer player = client.player;

        if (player == null || !player.isScoping()) {
            return;
        }

        String clubLabel = RangefinderConfig.get().club.getLabel();

        float partialTick = deltaTracker.getGameTimeDeltaPartialTick(false);
        OptionalDouble distance = TargetScanner.getTargetDistance(player, partialTick);

        if (distance.isEmpty()) {
            drawCenteredText(graphics, client.font, clubLabel + " \u00B7 No target", COLOR_NO_TARGET);
            return;
        }

        double value = distance.getAsDouble();
        String text = clubLabel + " \u00B7 " + DistanceUtil.formatDistance(value);
        int color = DistanceUtil.isInRange(value) ? COLOR_IN_RANGE : COLOR_OUT_OF_RANGE;

        drawCenteredText(graphics, client.font, text, color);
    }

    private static void drawCenteredText(GuiGraphicsExtractor graphics, Font font, String text, int color) {
        RangefinderConfig config = RangefinderConfig.get();

        MutableComponent label = Component.literal(text);
        if (config.boldText) {
            label.withStyle(ChatFormatting.BOLD);
        }

        float scaledWidth = font.width(label) * config.textScale;
        float x = (graphics.guiWidth() - scaledWidth) / 2.0f;
        float y = graphics.guiHeight() / 2.0f + 12;

        graphics.pose().pushMatrix();
        graphics.pose().translate(x, y);
        graphics.pose().scale(config.textScale, config.textScale);

        if (config.showBackdrop) {
            graphics.fill(-2, -2, font.width(label) + 2, font.lineHeight + 1, 0x90000000);
        }
        graphics.text(font, label, 0, 0, color, false);

        graphics.pose().popMatrix();
    }
}