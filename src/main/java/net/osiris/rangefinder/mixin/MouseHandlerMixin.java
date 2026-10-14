package net.osiris.rangefinder.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import net.osiris.rangefinder.ZoomState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {
    @Inject(method = "onScroll", at = @At("HEAD"), cancellable = true)
    private void rangefinder$onScroll(long handle, double xoffset, double yoffset, CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        LocalPlayer player = client.player;

        if (player == null || client.gui.screen() != null || !player.isScoping()) {
            return;   // not our business, let vanilla handle it
        }

        ZoomState.adjust(Math.signum(yoffset));
        //SpyglassRangefinder.LOGGER.info("Zoom: {}", ZoomState.getZoom());
        ci.cancel();   // stop the hotbar from changing
    }
}