package net.osiris.rangefinder;

import java.util.OptionalDouble;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.client.gui.Font;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.HitResult;

public class SpyglassRangefinderClient implements ClientModInitializer {

    private static final double SCAN_RANGE = 128.0;
    private static final boolean BOLD_TEXT = true;
    private static final float TEXT_SCALE = 1.2f;

    @Override
    public void onInitializeClient() {
        SpyglassRangefinder.LOGGER.info("Rangefinder client loaded!");

        HudElementRegistry.attachElementBefore(
                VanillaHudElements.CHAT,
                SpyglassRangefinder.id("rangefinder_overlay"),
                SpyglassRangefinderClient::renderOverlay
        );
    }

    private static OptionalDouble getTargetDistance(LocalPlayer player, float partialTick) {
        HitResult hit = player.pick(SCAN_RANGE, partialTick, false);

        if (hit.getType() == HitResult.Type.MISS) {
            return OptionalDouble.empty();
        }

        double distance = hit.getLocation().distanceTo(player.getEyePosition(partialTick));
        return OptionalDouble.of(distance);
    }

    private static void renderOverlay(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        Minecraft client = Minecraft.getInstance();
        LocalPlayer player = client.player;

        if (player == null || !player.isScoping()) {
            return;
        }

        float partialTick = deltaTracker.getGameTimeDeltaPartialTick(false);
        OptionalDouble distance = getTargetDistance(player, partialTick);

        if (distance.isEmpty()) {
            drawCenteredText(graphics, client.font, "No target", 0xFFAAAAAA);
            return;
        }

        double value = distance.getAsDouble();
        String text = SpyglassRangefinder.formatDistance(value);
        int color = SpyglassRangefinder.isInRange(value) ? 0xFF55FF55 : 0xFFFF5555;

        drawCenteredText(graphics, client.font, text, color);
    }

    private static void drawCenteredText(GuiGraphicsExtractor graphics, Font font, String text, int color) {
        MutableComponent label = Component.literal(text);
        if (BOLD_TEXT) {
            label.withStyle(ChatFormatting.BOLD);
        }

        float scaledWidth = font.width(label) * TEXT_SCALE;
        float x = (graphics.guiWidth() - scaledWidth) / 2.0f;
        float y = graphics.guiHeight() / 2.0f + 12;

        graphics.pose().pushMatrix();
        graphics.pose().translate(x, y);
        graphics.pose().scale(TEXT_SCALE, TEXT_SCALE);

        graphics.fill(-2, -2, font.width(label) + 2, font.lineHeight + 1, 0x90000000);
        graphics.text(font, label, 0, 0, color, false);

        graphics.pose().popMatrix();
    }
}
