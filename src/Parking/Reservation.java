package Parking;

public class Reservation {
    private Spot spot;
    private UserAccount user;
    private long startTime;
    private boolean checkedIn;

    private static final long TIME_LIMIT = 10 * 60 * 1000; // 10分钟

    public Reservation(Spot spot, UserAccount user) {
        this.spot = spot;
        this.user = user;
        this.startTime = System.currentTimeMillis();
        this.checkedIn = false;
    }

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
}
