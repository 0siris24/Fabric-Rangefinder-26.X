package net.osiris.rangefinder;

import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

	// Test Comment
public class SpyglassRangefinder implements ModInitializer {
	public static final String MOD_ID = "rangefindermod";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");
		LOGGER.info(formatDistance(0));
		LOGGER.info(formatDistance(12.04));
		LOGGER.info(formatDistance(12.99));
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	public static String formatDistance(double distance) {
		return String.format("%.1f blocks", distance);
	}

	public static final double MAX_RANGE = 50.0;

	public static boolean isInRange(double distance) {
		return  distance <= MAX_RANGE;
	}
}
