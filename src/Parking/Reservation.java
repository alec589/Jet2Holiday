package Parking;

public class Reservation {
    private Spot spot;
    private UserAccount user;
    private long startTime;
    private boolean checkedIn;

    private static final long TIME_LIMIT = 10 * 60 * 1000; // 10min

    public Reservation(Spot spot, UserAccount user) {
        this.spot = spot;
        this.user = user;
        this.startTime = System.currentTimeMillis();
        this.checkedIn = false;
    }
//no checkIn and exceed timelimit
    public boolean isExpired() {
        long now = System.currentTimeMillis();
        return !checkedIn && (now - startTime > TIME_LIMIT);
    }

    public void checkIn() {
        this.checkedIn = true;
    }

    public boolean isCheckedIn() {
        return checkedIn;
    }

    public Spot getSpot() {
        return spot;
    }

    public UserAccount getUser() {
        return user;
    }
    public long getStartTime() {
        return startTime;
    }
//get remaining time （ms）
    public long getRemainingTimeMillis() {
        long remaining = TIME_LIMIT - (System.currentTimeMillis() - startTime);
        return Math.max(0, remaining);
    }
}
