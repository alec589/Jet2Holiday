package Parking;

import List.ListInterface;
import List.MyArrayList;
import Map.MapInterface;
import Map.MyHashMap;
import Stack.MyStack;

public class ReservationService {
	//store the current valid reservation 
    private MapInterface<String, Reservation> currenteReservations = new MyHashMap<>();
    //"Recent Operations" in the reservation History
    private MyStack<Reservation> undoStack = new MyStack<>();

    
    public boolean reserve(UserAccount user, Spot spot) {
        cleanupExpiredReservations();

        if (user.isBlacklisted()) return false;
        if (spot.isOccupied()) return false;
        if (currenteReservations.containsKey(spot.getSpotId())) return false;

        Reservation r = new Reservation(spot, user);
        currenteReservations.put(spot.getSpotId(), r);
        undoStack.push(r);

        spot.setOccupied(true);
        return true;
    }

    public boolean checkIn(String spotId) {
        cleanupExpiredReservations();

        Reservation r = currenteReservations.get(spotId);
        if (r == null) return false;

        r.checkIn();
        return true;
    }

    public boolean cancelReservation(String spotId) {
        Reservation r = currenteReservations.get(spotId);
        if (r == null) return false;

        r.getSpot().setOccupied(false);
        currenteReservations.remove(spotId);
        return true;
    }

   
    public void cleanupExpiredReservations() {
        MyArrayList<String> expiredIds = new MyArrayList<>();

        for (String spotId : currenteReservations.keySet()) {
            Reservation r = currenteReservations.get(spotId);
            if (r != null && r.isExpired()) {
                expiredIds.add(spotId);
            }
        }

        for (String spotId : expiredIds) {
            Reservation r = currenteReservations.get(spotId);
            if (r != null) {
                r.getSpot().setOccupied(false);
                r.getUser().addMissedReservation();
                currenteReservations.remove(spotId);
            }
        }
    }
    
    public ListInterface<Reservation> getReservationsByUser(UserAccount user) {
        cleanupExpiredReservations();

        ListInterface<Reservation> result = new MyArrayList<>();

        for (String spotId : currenteReservations.keySet()) {
            Reservation r = currenteReservations.get(spotId);

            if (r != null && r.getUser().getUsername().equals(user.getUsername())) {
                result.add(r);
            }
        }

        return result;
    }
    //Cancel the last Reservation
    public boolean undoLastReservation() {
        if (undoStack.isEmpty()) return false;

        Reservation last = undoStack.pop();
        String spotId = last.getSpot().getSpotId();

        Reservation current = currenteReservations.get(spotId);
        if (current == null) return false;
        if (current.isCheckedIn()) return false; // Once checked in, it is not possible to cancel.

        current.getSpot().setOccupied(false);
        currenteReservations.remove(spotId);
        return true;
    }

    public Reservation getReservation(String spotId) {
        cleanupExpiredReservations();
        return currenteReservations.get(spotId);
    }
}