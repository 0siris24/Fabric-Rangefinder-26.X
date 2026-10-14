package net.osiris.rangefinder;

public final class DistanceUtil {
    public static final double MAX_RANGE = 50.0;

    private DistanceUtil() {}

    public static String formatDistance(double distance) {
        return String.format("%.1f blocks", distance);
    }

    public static boolean isInRange(double distance) {
        return distance <= MAX_RANGE;
    }
}