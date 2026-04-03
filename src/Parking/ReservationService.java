package Parking;

import List.MyArrayList;
import Map.MapInterface;
import Map.MyHashMap;
import Stack.MyStack;

public class ReservationService {
	//store the current valid reservation 
    private MapInterface<String, Reservation> activeReservations = new MyHashMap<>();
    //"Recent Operations" in the reservation History
    private MyStack<Reservation> undoStack = new MyStack<>();

    
    public boolean reserve(UserAccount user, Spot spot) {
        cleanupExpiredReservations();

        if (user.isBlacklisted()) return false;
        if (spot.isOccupied()) return false;
        if (activeReservations.containsKey(spot.getSpotId())) return false;

        Reservation r = new Reservation(spot, user);
        activeReservations.put(spot.getSpotId(), r);
        undoStack.push(r);

        spot.setOccupied(true);
        return true;
    }

    public boolean checkIn(String spotId) {
        cleanupExpiredReservations();

        Reservation r = activeReservations.get(spotId);
        if (r == null) return false;

        r.checkIn();
        return true;
    }

    public boolean cancelReservation(String spotId) {
        Reservation r = activeReservations.get(spotId);
        if (r == null) return false;

        r.getSpot().setOccupied(false);
        activeReservations.remove(spotId);
        return true;
    }

   
    public void cleanupExpiredReservations() {
        MyArrayList<String> expiredIds = new MyArrayList<>();

        for (String spotId : activeReservations.keySet()) {
            Reservation r = activeReservations.get(spotId);
            if (r != null && r.isExpired()) {
                expiredIds.add(spotId);
            }
        }

        for (String spotId : expiredIds) {
            Reservation r = activeReservations.get(spotId);
            if (r != null) {
                r.getSpot().setOccupied(false);
                r.getUser().addMissedReservation();
                activeReservations.remove(spotId);
            }
        }
    }
    //Cancel the last Reservation
    public boolean undoLastReservation() {
        if (undoStack.isEmpty()) return false;

        Reservation last = undoStack.pop();
        String spotId = last.getSpot().getSpotId();

        Reservation current = activeReservations.get(spotId);
        if (current == null) return false;
        if (current.isCheckedIn()) return false; // Once checked in, it is not possible to cancel.

        current.getSpot().setOccupied(false);
        activeReservations.remove(spotId);
        return true;
    }

    public Reservation getReservation(String spotId) {
        cleanupExpiredReservations();
        return activeReservations.get(spotId);
    }
}