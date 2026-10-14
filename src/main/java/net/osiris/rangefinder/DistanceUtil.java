package net.osiris.rangefinder;

public final class DistanceUtil {

    private DistanceUtil() {}

    public static String formatDistance(double distance) {
        return String.format("%.1f blocks", distance);
    }

    public static boolean isInRange(double distance) {
        return distance <= RangefinderConfig.get().club.getMaxRange();
    }
}