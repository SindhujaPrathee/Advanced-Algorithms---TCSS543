package networkFlowAlgorithms.FordFulkerson;

/**
 * A class that represents the residual edge for the graph
 * 
 * @author Raaghavi Sivaguru
 */

public class ResidualEdge {
	private double capacity;
	private ResidualNode v;
	private ResidualNode u;
	
	public ResidualEdge(ResidualNode v, ResidualNode u, double capacity) {
		this.v = v;
		this.u = u;
		this.capacity = capacity;
	}
	
	public double getCapacity() {
		return capacity;
	}
	
	public void setCapacity(double value) {
		this.capacity = value;
	}
	
	public ResidualNode getFirstPoint() {
		return v;
	}
	
	public ResidualNode getSecondPoint() {
		return u;
	}

}
