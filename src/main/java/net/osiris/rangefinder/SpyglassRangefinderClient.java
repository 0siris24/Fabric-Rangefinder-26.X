package net.osiris.rangefinder;

import java.util.OptionalDouble;

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
            graphics.text(client.font, "No target", 10, 10, 0xFFAAAAAA);
            return;
        }

        double value = distance.getAsDouble();
        String text = SpyglassRangefinder.formatDistance(value);
        int color = SpyglassRangefinder.isInRange(value) ? 0xFF55FF55 : 0xFFFF5555;

        graphics.text(client.font, text, 10,10, color);
    }
}
