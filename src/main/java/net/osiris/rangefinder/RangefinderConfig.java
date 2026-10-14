package net.osiris.rangefinder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

public final class RangefinderConfig {
    // --- Settings (public so Gson can read and write them) ---
    public ClubRange club = ClubRange.NO_CLUB;
    public float textScale = 1.5f;
    public boolean boldText = true;
    public boolean showBackdrop = true;
    public double defaultZoom = 10.0;
    public double minZoom = 2.0;
    public double maxZoom = 30.0;

    // --- Loading and saving machinery ---
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH =
            FabricLoader.getInstance().getConfigDir().resolve("rangefindermod.json");

    private static RangefinderConfig instance = new RangefinderConfig();

    public static RangefinderConfig get() {
        return instance;
    }

    public static void load() {
        if (Files.exists(PATH)) {
            try {
                RangefinderConfig loaded = GSON.fromJson(Files.readString(PATH), RangefinderConfig.class);
                if (loaded != null) {
                    instance = loaded;
                }
            } catch (IOException | RuntimeException e) {
                SpyglassRangefinder.LOGGER.warn("Could not read config, using defaults", e);
            }
        }
        instance.sanitize();
        save();   // writes defaults on first run and adds any new settings later
    }

    public static void save() {
        try {
            Files.writeString(PATH, GSON.toJson(instance));
        } catch (IOException e) {
            SpyglassRangefinder.LOGGER.warn("Could not save config", e);
        }
    }

    // Keeps hand-edited values inside sensible limits.
    private void sanitize() {
        if (club == null) {
            club = ClubRange.NO_CLUB;
        }
        textScale = Math.clamp(textScale, 0.5f, 4.0f);
        minZoom = Math.clamp(minZoom, 1.0, 100.0);
        maxZoom = Math.clamp(maxZoom, minZoom, 100.0);
        defaultZoom = Math.clamp(defaultZoom, minZoom, maxZoom);
    }
}