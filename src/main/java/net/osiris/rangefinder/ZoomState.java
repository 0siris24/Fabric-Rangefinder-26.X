package net.osiris.rangefinder;

public final class ZoomState {
    private static final double STEP = 1.0;

    // NaN means "not set yet", so the first read picks up the config's default.
    private static double zoom = Double.NaN;

    private ZoomState() {}

    public static double getZoom() {
        RangefinderConfig config = RangefinderConfig.get();
        if (Double.isNaN(zoom)) {
            zoom = config.defaultZoom;
        }
        return Math.clamp(zoom, config.minZoom, config.maxZoom);
    }

    // Positive scroll zooms in, negative zooms out.
    public static void adjust(double scrollAmount) {
        zoom = getZoom() + scrollAmount * STEP;
    }

    public static void reset() {
        zoom = Double.NaN;
    }

    public static double getFovMultiplier() {
        return 1.0 / getZoom();
    }
}