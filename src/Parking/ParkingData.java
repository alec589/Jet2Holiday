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
                double price = 2.0 + (4.0 * rand.nextDouble());
                boolean occupied = rand.nextBoolean();

                int x;
                int y;

                if (i < 6) {
                    x = 50 + i * 120;
                    y = 80;
                } else {
                    x = 50 + (i - 6) * 120;
                    y = 150;
                }

                Spot spot = new Spot(
                    id,
                    occupied,
                    new Coordinate(x, y),
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
            new Destination("Prudential Center", new Coordinate(760, 80), AreaType.BACKBAY));
        allDestinations.put("Copley Square",
            new Destination("Copley Square", new Coordinate(760, 140), AreaType.BACKBAY));
        allDestinations.put("Newbury Street",
            new Destination("Newbury Street", new Coordinate(760, 200), AreaType.BACKBAY));

        // FENWAY
        allDestinations.put("Fenway Park",
            new Destination("Fenway Park", new Coordinate(760, 80), AreaType.FENWAY));
        allDestinations.put("Museum of Fine Arts",
            new Destination("Museum of Fine Arts", new Coordinate(760, 140), AreaType.FENWAY));
        allDestinations.put("Northeastern University",
            new Destination("Northeastern University", new Coordinate(760, 200), AreaType.FENWAY));

        // DOWNTOWN
        allDestinations.put("Boston Common",
            new Destination("Boston Common", new Coordinate(760, 80), AreaType.DOWNTOWN));
        allDestinations.put("Faneuil Hall",
            new Destination("Faneuil Hall", new Coordinate(760, 140), AreaType.DOWNTOWN));
        allDestinations.put("State Street Office",
            new Destination("State Street Office", new Coordinate(760, 200), AreaType.DOWNTOWN));

        // SEAPORT
        allDestinations.put("Seaport World Trade Center",
            new Destination("Seaport World Trade Center", new Coordinate(760, 80), AreaType.SEAPORT));
        allDestinations.put("ICA Museum",
            new Destination("ICA Museum", new Coordinate(760, 140), AreaType.SEAPORT));
        allDestinations.put("Harborwalk",
            new Destination("Harborwalk", new Coordinate(760, 200), AreaType.SEAPORT));

        // NEWTON
        allDestinations.put("Newton Centre",
            new Destination("Newton Centre", new Coordinate(760, 80), AreaType.NEWTON));
        allDestinations.put("Boston College",
            new Destination("Boston College", new Coordinate(760, 140), AreaType.NEWTON));
        allDestinations.put("Newton Library",
            new Destination("Newton Library", new Coordinate(760, 200), AreaType.NEWTON));
        
    }

    private void initializeRoadNodes() {
    	allRoadNodes.put("BACKBAY",
    	        new RoadNode("BackBay Main Road", new Coordinate(450, 280)));

    	    allRoadNodes.put("FENWAY",
    	        new RoadNode("Fenway Main Road", new Coordinate(450, 280)));

    	    allRoadNodes.put("DOWNTOWN",
    	        new RoadNode("Downtown Main Road", new Coordinate(450, 280)));

    	    allRoadNodes.put("SEAPORT",
    	        new RoadNode("Seaport Main Road", new Coordinate(450, 280)));

    	    allRoadNodes.put("NEWTON",
    	        new RoadNode("Newton Main Road", new Coordinate(450, 280)));
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

    private void connectSpotsToMainRoad(MyGraph<Node> graph) {
        for (Spot spot : allSpots.values()) {

            RoadNode road = allRoadNodes.get(spot.getArea().name());

            double dist = spot.getCoordinate().distanceTo(road.getCoordinate());

            graph.addEdge(spot.toNode(), road.toNode(), dist);
        }
    }

    private void connectDestinationsToMainRoad(MyGraph<Node> graph) {
        for (Destination d : allDestinations.values()) {

            RoadNode road = allRoadNodes.get(d.getArea().name());

            double dist = d.getCoordinate().distanceTo(road.getCoordinate());

            graph.addEdge(d.toNode(), road.toNode(), dist);
        }
    }

    private void connectMainRoads(MyGraph<Node> graph) {
        connect(graph, "BACKBAY", "FENWAY");
        connect(graph, "FENWAY", "DOWNTOWN");
        connect(graph, "DOWNTOWN", "SEAPORT");
        connect(graph, "SEAPORT", "NEWTON");
    }

    private void connect(MyGraph<Node> graph, String a, String b) {
        RoadNode r1 = allRoadNodes.get(a);
        RoadNode r2 = allRoadNodes.get(b);

        double dist = r1.getCoordinate().distanceTo(r2.getCoordinate());

        graph.addEdge(r1.toNode(), r2.toNode(), dist);
    }
    
    public MyGraph<Node> buildGraph() {
        MyGraph<Node> graph = new MyGraph<>();

        // 1. add all spot nodes
        for (Spot spot : allSpots.values()) {
            graph.addNode(spot.toNode());
        }

        // 2. add all destination nodes
        for (Destination d : allDestinations.values()) {
            graph.addNode(d.toNode());
        }

        // 3. add all road nodes
        for (RoadNode road : allRoadNodes.values()) {
            graph.addNode(road.toNode());
        }

        // 4. connect roads
        connectMainRoads(graph);

        // 5. connect spots to their area's main road
        connectSpotsToMainRoad(graph);

        // 6. connect destinations to their area's main road
        connectDestinationsToMainRoad(graph);

        return graph;
    }
    
    public Spot findNearestAvailableSpot(String destinationName) {
        Destination destination = allDestinations.get(destinationName);

        if (destination == null) {
            return null;
        }

        MyGraph<Node> graph = buildGraph();

        Spot bestSpot = null;
        double bestDistance = Double.MAX_VALUE;

        for (Spot spot : allSpots.values()) {
            if (!spot.isOccupied()) {
                double distance = graph.shortestDistance(destination.toNode(), spot.toNode());

                if (distance < bestDistance) {
                    bestDistance = distance;
                    bestSpot = spot;
                }
            }
        }

        return bestSpot;
    }
    
    public double getDistanceToSpot(String destinationName, Spot spot) {
        Destination destination = allDestinations.get(destinationName);

        if (destination == null || spot == null) {
            return Double.MAX_VALUE;
        }

        MyGraph<Node> graph = buildGraph();
        return graph.shortestDistance(destination.toNode(), spot.toNode());
    }
    
    public Destination getDestinationByName(String name) {
        return allDestinations.get(name);
    }
    
    public MyGraph<Node> getGraph() {
        return buildGraph();
    }

}

