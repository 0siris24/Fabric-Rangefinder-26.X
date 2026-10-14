package net.osiris.rangefinder;

import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public final class ClubCycle {
    private static KeyMapping nextKey;
    private static KeyMapping previousKey;

    private ClubCycle() {}

    public static void register() {
        nextKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.rangefindermod.club_next",
                InputConstants.Type.KEYBOARD,
                InputConstants.KEY_X,
                SpyglassToggle.CATEGORY
        ));

        previousKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.rangefindermod.club_previous",
                InputConstants.Type.KEYBOARD,
                InputConstants.KEY_Z,
                SpyglassToggle.CATEGORY
        ));

        ClientTickEvents.END_CLIENT_TICK.register(ClubCycle::onTick);
    }

    private static void onTick(Minecraft client) {
        LocalPlayer player = client.player;
        boolean scoping = player != null && player.isScoping();

        // Always drain the key presses, even when we ignore them. Otherwise
        // presses made outside the spyglass would pile up and all fire at once
        // the moment you next raise it.
        while (nextKey.consumeClick()) {
            if (scoping) {
                changeClub(true);
            }
        }
        while (previousKey.consumeClick()) {
            if (scoping) {
                changeClub(false);
            }
        }
    }

    private static void changeClub(boolean forward) {
        RangefinderConfig config = RangefinderConfig.get();
        config.club = forward ? config.club.next() : config.club.previous();
        RangefinderConfig.save();
    }
}