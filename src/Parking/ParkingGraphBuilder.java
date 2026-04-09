package Parking;

import Graph.MyGraph;
import Graph.Node;
import List.ListInterface;
import List.MyArrayList;

public class ParkingGraphBuilder {
	private ParkingData parkingData;

    public ParkingGraphBuilder(ParkingData parkingData) {
        this.parkingData = parkingData;
    }
    
    
    // connect 5 nodes on main road
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
    	RoadNode r1 = parkingData.getAllRoadNodes().get(key1);
        RoadNode r2 = parkingData.getAllRoadNodes().get(key2);

        double dist = r1.getCoordinate().distanceTo(r2.getCoordinate());
        graph.addEdge(r1.toNode(), r2.toNode(), dist);
    }

    // connect parking spot to the nearest road node
    private void connectSpotsToMainRoad(MyGraph<Node> graph) {
        for (Spot spot : parkingData.getAllSpots().values()) {
            RoadNode nearestRoad = findNearestRoadNode(spot.getArea(), spot.getCoordinate());
            double dist = spot.getCoordinate().distanceTo(nearestRoad.getCoordinate());
            graph.addEdge(spot.toNode(), nearestRoad.toNode(), dist);
        }
    }

    // connect destination to the nearest road node
    private void connectDestinationsToMainRoad(MyGraph<Node> graph) {
        for (Destination d : parkingData.getAllDestinations().values()) {
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
            RoadNode road = parkingData.getAllRoadNodes().get(key);
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

    // connect parking spot and destination if they are on the same side
    private void connectAreaSameSide(MyGraph<Node> graph, AreaType area) {
        ListInterface<Node> topNodes = new MyArrayList<>();
        ListInterface<Node> bottomNodes = new MyArrayList<>();

        int roadY = 250;
        // occupied means it is blocked
        for (Spot spot : parkingData.getAllSpots().values()) {
            if (spot.getArea() == area && !spot.isOccupied()) {
                if (spot.getCoordinate().getY() < roadY) {
                    topNodes.add(spot.toNode());
                } else {
                    bottomNodes.add(spot.toNode());
                }
            }
        }

        for (Destination d : parkingData.getAllDestinations().values()) {
            if (d.getArea() == area) {
                if (d.getCoordinate().getY() < roadY) {
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
    
    // connect 5 areas
    private void connectAreaChains(MyGraph<Node> graph) {
        connectRoadNodes(graph, "BACKBAY_R5", "FENWAY_R1");
        connectRoadNodes(graph, "FENWAY_R5", "DOWNTOWN_R1");
        connectRoadNodes(graph, "DOWNTOWN_R5", "SEAPORT_R1");
        connectRoadNodes(graph, "SEAPORT_R5", "NEWTON_R1");
    }

    public MyGraph<Node> buildGraph() {
        MyGraph<Node> graph = new MyGraph<>();

        for (Spot spot : parkingData.getAllSpots().values()) {
            graph.addNode(spot.toNode());
        }

        for (Destination d : parkingData.getAllDestinations().values()) {
            graph.addNode(d.toNode());
        }

        for (RoadNode road : parkingData.getAllRoadNodes().values()) {
            graph.addNode(road.toNode());
        }

        connectMainRoads(graph);
        connectAreaChains(graph); 
        connectSpotsToMainRoad(graph);
        connectDestinationsToMainRoad(graph);
        connectSameSideNeighbors(graph);

        return graph;
    }

    public Spot findNearestAvailableSpot(String destinationName) {
        Destination destination = parkingData.getAllDestinations().get(destinationName);

        if (destination == null) {
            return null;
        }

        MyGraph<Node> graph = buildGraph();

        Spot bestSpot = null;
        double bestDistance = Double.MAX_VALUE;

        for (Spot spot : parkingData.getAllSpots().values()) {
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
        Destination destination = parkingData.getAllDestinations().get(destinationName);

        if (destination == null || spot == null) {
            return Double.MAX_VALUE;
        }

        MyGraph<Node> graph = buildGraph();
        return graph.shortestDistance(destination.toNode(), spot.toNode());
    }

    public MyGraph<Node> getGraph() {
        return buildGraph();
    }
}
