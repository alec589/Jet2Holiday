package Graph;

public interface GraphInterface {
	
	public void addNode(Node node);
	
	public void addEdge(Node from, Node to, double weight);
	
	public double shortestDistance(Node start, Node end);
	
}
