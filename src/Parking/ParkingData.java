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

                int[][] positions = {
                    {50, 80}, {200, 120}, {350, 85}, {500, 75}, {650, 55}, {800, 70},
                    {50, 320}, {200, 320}, {350, 335}, {500, 360}, {650, 320}, {800, 355}
                };

                x = positions[i][0];
                y = positions[i][1];

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
            new Destination("Prudential Center", new Coordinate(200, 50), AreaType.BACKBAY));

        allDestinations.put("Copley Square",
            new Destination("Copley Square", new Coordinate(600, 170), AreaType.BACKBAY));

        allDestinations.put("Newbury Street",
            new Destination("Newbury Street", new Coordinate(500, 300), AreaType.BACKBAY));

        // FENWAY
        allDestinations.put("Fenway Park",
            new Destination("Fenway Park", new Coordinate(200, 50), AreaType.FENWAY));
        allDestinations.put("Museum of Fine Arts",
            new Destination("Museum of Fine Arts", new Coordinate(600, 170), AreaType.FENWAY));
        allDestinations.put("Northeastern University",
            new Destination("Northeastern University", new Coordinate(500, 300), AreaType.FENWAY));

        // DOWNTOWN
        allDestinations.put("Boston Common",
            new Destination("Boston Common", new Coordinate(200, 50), AreaType.DOWNTOWN));
        allDestinations.put("Faneuil Hall",
            new Destination("Faneuil Hall", new Coordinate(600, 170), AreaType.DOWNTOWN));
        allDestinations.put("State Street Office",
            new Destination("State Street Office", new Coordinate(500, 300), AreaType.DOWNTOWN));

        // SEAPORT
        allDestinations.put("Seaport World Trade Center",
            new Destination("Seaport World Trade Center", new Coordinate(200, 50), AreaType.SEAPORT));
        allDestinations.put("ICA Museum",
            new Destination("ICA Museum", new Coordinate(600, 170), AreaType.SEAPORT));
        allDestinations.put("Harborwalk",
            new Destination("Harborwalk", new Coordinate(500, 300), AreaType.SEAPORT));

        // NEWTON
        allDestinations.put("Newton Centre",
            new Destination("Newton Centre", new Coordinate(200, 50), AreaType.NEWTON));
        allDestinations.put("Boston College",
            new Destination("Boston College", new Coordinate(600, 170), AreaType.NEWTON));
        allDestinations.put("Newton Library",
            new Destination("Newton Library", new Coordinate(500, 300), AreaType.NEWTON));
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
            new RoadNode(areaKey + " Main Road R1", new Coordinate(100, 280)));
        allRoadNodes.put(areaKey + "_R2",
            new RoadNode(areaKey + " Main Road R2", new Coordinate(250, 280)));
        allRoadNodes.put(areaKey + "_R3",
            new RoadNode(areaKey + " Main Road R3", new Coordinate(400, 280)));
        allRoadNodes.put(areaKey + "_R4",
            new RoadNode(areaKey + " Main Road R4", new Coordinate(550, 280)));
        allRoadNodes.put(areaKey + "_R5",
            new RoadNode(areaKey + " Main Road R5", new Coordinate(700, 280)));
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

    private void connectMainRoads(MyGraph<Node> graph) {
        connectRoadChain(graph, "BACKBAY");
        connectRoadChain(graph, "FENWAY");
        connectRoadChain(graph, "DOWNTOWN");
        connectRoadChain(graph, "SEAPORT");
        connectRoadChain(graph, "NEWTON");
    }

    private void connectRoadChain(MyGraph<Node> graph, String areaKey) {
        connectRoadNodes(graph, areaKey + "_R1", areaKey + "_R2");
        connectRoadNodes(graph, areaKey + "_R2", areaKey + "_R3");
        connectRoadNodes(graph, areaKey + "_R3", areaKey + "_R4");
        connectRoadNodes(graph, areaKey + "_R4", areaKey + "_R5");
    }

    private void connectRoadNodes(MyGraph<Node> graph, String key1, String key2) {
        RoadNode r1 = allRoadNodes.get(key1);
        RoadNode r2 = allRoadNodes.get(key2);

        double dist = r1.getCoordinate().distanceTo(r2.getCoordinate());
        graph.addEdge(r1.toNode(), r2.toNode(), dist);
    }

    private void connectSpotsToMainRoad(MyGraph<Node> graph) {
        for (Spot spot : allSpots.values()) {
            RoadNode nearestRoad = findNearestRoadNode(spot.getArea(), spot.getCoordinate());
            double dist = spot.getCoordinate().distanceTo(nearestRoad.getCoordinate());
            graph.addEdge(spot.toNode(), nearestRoad.toNode(), dist);
        }
    }

    private void connectDestinationsToMainRoad(MyGraph<Node> graph) {
        for (Destination d : allDestinations.values()) {
            RoadNode nearestRoad = findNearestRoadNode(d.getArea(), d.getCoordinate());
            double dist = d.getCoordinate().distanceTo(nearestRoad.getCoordinate());
            graph.addEdge(d.toNode(), nearestRoad.toNode(), dist);
        }
    }

    private RoadNode findNearestRoadNode(AreaType area, Coordinate coordinate) {
        RoadNode best = null;
        double bestDist = Double.MAX_VALUE;

        for (int i = 1; i <= 5; i++) {
            String key = area.name() + "_R" + i;
            RoadNode road = allRoadNodes.get(key);
            double dist = coordinate.distanceTo(road.getCoordinate());

            if (dist < bestDist) {
                bestDist = dist;
                best = road;
            }
        }

        return best;
    }

    private void connectSameSideNeighbors(MyGraph<Node> graph) {
        connectAreaSameSide(graph, AreaType.BACKBAY);
        connectAreaSameSide(graph, AreaType.FENWAY);
        connectAreaSameSide(graph, AreaType.DOWNTOWN);
        connectAreaSameSide(graph, AreaType.SEAPORT);
        connectAreaSameSide(graph, AreaType.NEWTON);
    }

    private void connectAreaSameSide(MyGraph<Node> graph, AreaType area) {
        ListInterface<Node> topNodes = new MyArrayList<>();
        ListInterface<Node> bottomNodes = new MyArrayList<>();

        for (Spot spot : allSpots.values()) {
            if (spot.getArea() == area) {
                if (spot.getCoordinate().getY() < 280) {
                    topNodes.add(spot.toNode());
                } else {
                    bottomNodes.add(spot.toNode());
                }
            }
        }

        for (Destination d : allDestinations.values()) {
            if (d.getArea() == area) {
                if (d.getCoordinate().getY() < 280) {
                    topNodes.add(d.toNode());
                } else {
                    bottomNodes.add(d.toNode());
                }
            }
        }

        sortNodesByX(topNodes);
        sortNodesByX(bottomNodes);

        connectNeighborList(graph, topNodes);
        connectNeighborList(graph, bottomNodes);
    }

    private void sortNodesByX(ListInterface<Node> nodes) {
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = 0; j < nodes.size() - 1 - i; j++) {
                Node a = nodes.get(j);
                Node b = nodes.get(j + 1);

                Coordinate ca = a.getCoordinate();
                Coordinate cb = b.getCoordinate();

                if (ca.getX() > cb.getX()) {
                    Node temp = a;
                    nodes.set(j, b);
                    nodes.set(j + 1, temp);
                }
            }
        }
    }

    private void connectNeighborList(MyGraph<Node> graph, ListInterface<Node> nodes) {
        for (int i = 0; i < nodes.size() - 1; i++) {
            Node a = nodes.get(i);
            Node b = nodes.get(i + 1);

            Coordinate ca = a.getCoordinate();
            Coordinate cb = b.getCoordinate();

            double dist = ca.distanceTo(cb);
            graph.addEdge(a, b, dist);
        }
    }

    public MyGraph<Node> buildGraph() {
        MyGraph<Node> graph = new MyGraph<>();

        for (Spot spot : allSpots.values()) {
            graph.addNode(spot.toNode());
        }

        for (Destination d : allDestinations.values()) {
            graph.addNode(d.toNode());
        }

        for (RoadNode road : allRoadNodes.values()) {
            graph.addNode(road.toNode());
        }

        connectMainRoads(graph);
        connectSpotsToMainRoad(graph);
        connectDestinationsToMainRoad(graph);
        connectSameSideNeighbors(graph);

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