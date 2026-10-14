package net.osiris.rangefinder;

public enum ClubRange {
    NO_CLUB("No Club", 50.0),
    WEDGE("Wedge", 150.0),
    IRON("Iron", 200.0),
    DRIVER("Driver", 280.0),
    FREE_LOOK("Free Look", TargetScanner.SCAN_RANGE);

    private final String label;
    private final double maxRange;

    ClubRange(String label, double maxRange) {
        this.label = label;
        this.maxRange = maxRange;
    }

    public String getLabel() {
        return label;
    }

    public double getMaxRange() {
        return maxRange;
    }

    // The next club in the list, wrapping from the last back to the first.
    public ClubRange next() {
        ClubRange[] all = values();
        return all[(ordinal() + 1) % all.length];
    }

    // The previous club in the list, wrapping from the first back to the last.
    public ClubRange previous() {
        ClubRange[] all = values();
        return all[(ordinal() + all.length - 1) % all.length];
    }
}