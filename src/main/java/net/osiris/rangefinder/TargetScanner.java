package net.osiris.rangefinder;

import java.util.OptionalDouble;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.HitResult;

public final class TargetScanner {
    // How far we search for a target. Must be larger than DistanceUtil.MAX_RANGE.
    private static final double SCAN_RANGE = 128.0;

    private TargetScanner() {}

    public static OptionalDouble getTargetDistance(LocalPlayer player, float partialTick) {
        HitResult hit = player.pick(SCAN_RANGE, partialTick, false);

        if (hit.getType() == HitResult.Type.MISS) {
            return OptionalDouble.empty();
        }

        double distance = hit.getLocation().distanceTo(player.getEyePosition(partialTick));
        return OptionalDouble.of(distance);
    }
}