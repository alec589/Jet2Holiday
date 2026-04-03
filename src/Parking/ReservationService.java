package Parking;

import Map.MapInterface;
import Map.MyHashMap;
import Stack.MyStack;

public class ReservationService {

    private MapInterface<String, Reservation> activeReservations = new MyHashMap<>();
    private MyStack<Reservation> undoStack = new MyStack<>();

    public boolean reserve(UserAccount user, Spot spot) {
        if (user.isBlacklisted()) return false;
        
        if (spot.isOccupied()) return false;

        Reservation r = new Reservation(spot, user);
        activeReservations.put(spot.getSpotId(), r);
        undoStack.push(r);
        
        spot.setOccupied(true);

        return true;
    }
    }