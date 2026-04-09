package Parking;

import java.util.Random;

import Graph.MyGraph;
import Graph.Node;
import List.ListInterface;
import List.MyArrayList;
import Map.MapInterface;
import Map.MyHashMap;

public class ParkingData {
    private MapInterface<String, Spot> allSpots;
    private MapInterface<String, Destination> allDestinations;
    private MapInterface<String, RoadNode> allRoadNodes;

    public ParkingData() {
        allSpots = new MyHashMap<>();
        allDestinations = new MyHashMap<>();
        allRoadNodes = new MyHashMap<>();

        initializeSpots();
        initializeDestinations();
        initializeRoadNodes();
    }

    private void initializeSpots() {
        Random rand = new Random();
        int idCounter = 100;

        AreaType[] areas = {
            AreaType.BACKBAY,
            AreaType.FENWAY,
            AreaType.DOWNTOWN,
            AreaType.SEAPORT,
            AreaType.NEWTON
        };

        for (int areaIndex = 0; areaIndex < areas.length; areaIndex++) {
            AreaType area = areas[areaIndex];

            for (int i = 0; i < 12; i++) {
                String id = "BOS-" + idCounter++;
                
                // set some prices and availability for demo
                double price;
                boolean occupied;
                if (id.equals("BOS-101") || id.equals("BOS-102") 
                        || id.equals("BOS-107") || id.equals("BOS-100")) {
                    occupied = true;
                    price = 5.0;  
                }
                else if (id.equals("BOS-103")) {
                    occupied = false;
                    price = 9.0;  
                }
                else if (id.equals("BOS-108")) {
                    occupied = false;
                    price = 5.0;  
                }
                else if (id.equals("BOS-106")) {
                    occupied = false;
                    price = 3.0;  
                }
                else if (id.equals("BOS-109")) {
                    occupied = false;
                    price = 8.5;  
                }
                else {
                    price = 5.0;
                    occupied = rand.nextBoolean();
                }

                int x;
                int y;

                int[][] positions = {
                    {30, 60}, {140, 60}, {445, 60}, {555, 60}, {860, 60}, {970, 60},
                    {30, 300}, {190, 300}, {350, 300}, {650, 300}, {810, 300}, {970, 300}
                };

                x = positions[i][0];
                y = positions[i][1];

                int centerX = x + 50;   // 100 / 2
                int centerY = y + 75;   // 150 / 2

                Spot spot = new Spot(
                    id,
                    occupied,
                    new Coordinate(centerX, centerY),
                    price,
                    area
                );

                allSpots.put(id, spot);
            }
        }
    }

    private void initializeDestinations() {
    	// BACKBAY
        allDestinations.put("Prudential Center",
            new Destination("Prudential Center", new Coordinate(337, 130), AreaType.BACKBAY));
        allDestinations.put("Copley Square",
            new Destination("Copley Square", new Coordinate(752, 130), AreaType.BACKBAY));
        allDestinations.put("Newbury Street",
            new Destination("Newbury Street", new Coordinate(552, 375), AreaType.BACKBAY));

        // FENWAY
        allDestinations.put("Fenway Park",
            new Destination("Fenway Park", new Coordinate(337, 130), AreaType.FENWAY));
        allDestinations.put("Museum of Fine Arts",
            new Destination("Museum of Fine Arts", new Coordinate(752, 130), AreaType.FENWAY));
        allDestinations.put("Northeastern University",
            new Destination("Northeastern University", new Coordinate(552, 375), AreaType.FENWAY));

        // DOWNTOWN
        allDestinations.put("Boston Common",
            new Destination("Boston Common", new Coordinate(337, 130), AreaType.DOWNTOWN));
        allDestinations.put("Faneuil Hall",
            new Destination("Faneuil Hall", new Coordinate(752, 130), AreaType.DOWNTOWN));
        allDestinations.put("State Street Office",
            new Destination("State Street Office", new Coordinate(552, 375), AreaType.DOWNTOWN));

        // SEAPORT
        allDestinations.put("World Trade Center",
            new Destination("World Trade Center", new Coordinate(337, 130), AreaType.SEAPORT));
        allDestinations.put("ICA Museum",
            new Destination("ICA Museum", new Coordinate(752, 130), AreaType.SEAPORT));
        allDestinations.put("Harborwalk",
            new Destination("Harborwalk", new Coordinate(552, 375), AreaType.SEAPORT));

        // NEWTON
        allDestinations.put("Newton Centre",
            new Destination("Newton Centre", new Coordinate(337, 130), AreaType.NEWTON));
        allDestinations.put("Boston College",
            new Destination("Boston College", new Coordinate(752, 130), AreaType.NEWTON));
        allDestinations.put("Newton Library",
            new Destination("Newton Library", new Coordinate(552, 375), AreaType.NEWTON));
    }

    private void initializeRoadNodes() {
        initializeAreaRoadNodes("BACKBAY");
        initializeAreaRoadNodes("FENWAY");
        initializeAreaRoadNodes("DOWNTOWN");
        initializeAreaRoadNodes("SEAPORT");
        initializeAreaRoadNodes("NEWTON");
    }

    private void initializeAreaRoadNodes(String areaKey) {
        allRoadNodes.put(areaKey + "_R1",
            new RoadNode(areaKey + "_Main Road R1", new Coordinate(110, 250)));
        allRoadNodes.put(areaKey + "_R2",
            new RoadNode(areaKey + "_Main Road R2", new Coordinate(320, 250)));
        allRoadNodes.put(areaKey + "_R3",
            new RoadNode(areaKey + "_Main Road R3", new Coordinate(500, 250)));
        allRoadNodes.put(areaKey + "_R4",
            new RoadNode(areaKey + "_Main Road R4", new Coordinate(680, 250)));
        allRoadNodes.put(areaKey + "_R5",
            new RoadNode(areaKey + "_Main Road R5", new Coordinate(950, 250)));
    }

    public MapInterface<String, Spot> getAllSpots() {
        return allSpots;
    }

    public MapInterface<String, Destination> getAllDestinations() {
        return allDestinations;
    }

    public MapInterface<String, RoadNode> getAllRoadNodes() {
        return allRoadNodes;
    }

    public void updateSpotStatus(String id, boolean occupied) {
        if (allSpots.containsKey(id)) {
            allSpots.get(id).setOccupied(occupied);
        }
    }

    public ListInterface<Spot> getSpotsByArea(AreaType area) {
        ListInterface<Spot> result = new MyArrayList<>();

        for (Spot spot : allSpots.values()) {
            if (spot.getArea() == area) {
                result.add(spot);
            }
        }

        return result;
    }

    public ListInterface<Destination> getDestinationsByArea(AreaType area) {
        ListInterface<Destination> result = new MyArrayList<>();

        for (Destination destination : allDestinations.values()) {
            if (destination.getArea() == area) {
                result.add(destination);
            }
        }

        return result;
    }

    public Destination getDestinationByName(String name) {
        return allDestinations.get(name);
    }
    
}