package net.osiris.rangefinder;

import net.fabricmc.api.ClientModInitializer;

public class SpyglassRangefinderClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SpyglassRangefinder.LOGGER.info("Rangefinder client loaded!");
    }
}
