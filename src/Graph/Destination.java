package Graph;

public class Destination {
	private String name;     
    private String area;   
    private String nodeId;   // accordingly node id

    public Destination(String name, String area, String nodeId) {
        this.name = name;
        this.area = area;
        this.nodeId = nodeId;
    }

    public String getName() {
        return name;
    }

    public String getArea() {
        return area;
    }

    public String getNodeId() {
        return nodeId;
    }

    @Override
    public String toString() {
        return name;
    }
}
