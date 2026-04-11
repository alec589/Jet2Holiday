package Parking;

import List.ListInterface;
import List.MyArrayList;

public class UserAccount {

    private String username;
    private String password;
    private boolean blacklisted;

    // Store all missed reservation times (in milliseconds)
    private ListInterface<Long> missedReservationTimes;

    // 30 days in milliseconds
    private static final long THIRTY_DAYS = 30L * 24 * 60 * 60 * 1000;
   
    //create user account with empty missed history
    public UserAccount(String username,String password) {
        this.username = username;
        this.password = password;
        this.blacklisted = false;
        this.missedReservationTimes = new MyArrayList<>();
    }
    
    public boolean checkPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }
    
    public String getUsername() {
        return username;
    }
    // update and return blacklist status
    public boolean isBlacklisted() {
        refreshBlacklistStatus();
        return blacklisted;
    }
 // return number of recent missed reservations
    public int getMissedReservationCount() {
        refreshBlacklistStatus();
        return missedReservationTimes.size();
    }

    /**
     * Record a missed reservation
     */
    public void addMissedReservation() {
        long now = System.currentTimeMillis(); // milliseconds

        // Remove expired records first
        removeExpiredMisses(now);

        // Add new missed reservation
        missedReservationTimes.add(now);

        // Update blacklist
        blacklisted = missedReservationTimes.size() > 2;
    }

    /**
     * Clear all history manually 
     */
    public void clearMissedReservationHistory() {
        missedReservationTimes.clear();
        blacklisted = false;
    }

    /**
     * Refresh blacklist status dynamically
     */
    public void refreshBlacklistStatus() {
        long now = System.currentTimeMillis();
        removeExpiredMisses(now);
        blacklisted = missedReservationTimes.size() > 2;
    }

    /**
     * Remove all records older than 30 days
     */
    private void removeExpiredMisses(long currentTime) {
        int i = 0;

        while (i < missedReservationTimes.size()) {
            long time = missedReservationTimes.get(i);

            if (currentTime - time > THIRTY_DAYS) {
                missedReservationTimes.remove(i);
            } else {
                i++;
            }
        }
    }

    @Override
    public String toString() {
        return "UserAccount{" +
               "username='" + username + '\'' +
               ", blacklisted=" + blacklisted +
               ", recentMissed=" + getMissedReservationCount() +
               '}';
    }
}