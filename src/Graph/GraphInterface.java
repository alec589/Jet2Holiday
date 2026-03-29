package Graph;

public interface GraphInterface {
	
	// Node here is districts/areas in Boston
	public void addNode(String name);

	// Edge is to connect two nodes
	public void addEdge(String from, String to, double weight);

	// To define the shortest distance between two nodes
	public double shortestDistance(String start, String end);
	
}
