package net.osiris.rangefinder;

import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.Items;

public final class SpyglassToggle {
     static final KeyMapping.Category CATEGORY =
            KeyMapping.Category.register(SpyglassRangefinder.id("controls"));

    private static KeyMapping toggleKey;
    private static boolean locked = false;

    private SpyglassToggle() {}

    public static void register() {
        toggleKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.rangefindermod.toggle_spyglass",
                InputConstants.Type.KEYBOARD,
                InputConstants.KEY_R,
                CATEGORY
        ));

        ClientTickEvents.START_CLIENT_TICK.register(SpyglassToggle::onTick);
    }

    private static void onTick(Minecraft client) {
        while (toggleKey.consumeClick()) {
            locked = !locked;
            if (!locked) {
                client.options.keyUse.setDown(false);   // let go
            }
        }

        LocalPlayer player = client.player;
        boolean canHold = player != null && holdingSpyglass(player);

        if (locked && !canHold) {
            locked = false;
            client.options.keyUse.setDown(false);
        }

        if (locked) {
            client.options.keyUse.setDown(true);
        }
    }

    private static boolean holdingSpyglass(LocalPlayer player) {
        return player.getMainHandItem().is(Items.SPYGLASS)
                || player.getOffhandItem().is(Items.SPYGLASS);
    }
}