package networkFlowAlgorithms.FordFulkerson;

import java.util.*;
/**
 * A class that represents the residual node for the graph
 * 
 * @author Raaghavi Sivaguru
 */

public class ResidualNode {
	public List<ResidualEdge> nextEdgeList;
	private String name;
	private boolean visited;
	
	public ResidualNode(Object name) {
		this.name = String.valueOf(name);
		this.visited = false;
		this.nextEdgeList = new LinkedList<>();
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isVisited() {
		return visited;
	}
	
	public void setVisited() {
		this.visited = true;
	}
	
	public void setUnvisited() {
		this.visited = false;
	}
	
	public boolean hasUnvisitedNeighbor() {
		for (int i = 0; i < nextEdgeList.size(); i++) {
			ResidualEdge e = nextEdgeList.get(i);
			ResidualNode v = e.getSecondPoint();
			if (!v.isVisited()) {
				return true;
			}
		}
		return false;
	}
	
	public ResidualNode getNeighbor() {
		for (int i = 0; i < nextEdgeList.size(); i++) {
			ResidualEdge e = nextEdgeList.get(i);
			ResidualNode v = e.getSecondPoint();
			if (!v.isVisited()) {
				return v;
			}
		}
		return null;
	}

}
