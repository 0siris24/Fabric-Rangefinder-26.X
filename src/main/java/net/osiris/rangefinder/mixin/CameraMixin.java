package net.osiris.rangefinder.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.osiris.rangefinder.ZoomState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Camera.class)
public class CameraMixin {
    // Vanilla's spyglass already shrinks the view to this fraction.
    @Unique
    private static final double VANILLA_SPYGLASS_MULTIPLIER = 0.1;

    @ModifyReturnValue(method = "calculateFov", at = @At("RETURN"))
    private float rangefinder$modifyFov(float original) {
        Minecraft client = Minecraft.getInstance();
        LocalPlayer player = client.player;

        if (player == null || !player.isScoping() || !client.options.getCameraType().isFirstPerson()) {
            return original;
        }

        double ratio = ZoomState.getFovMultiplier() / VANILLA_SPYGLASS_MULTIPLIER;
        return (float) (original * ratio);
    }
}