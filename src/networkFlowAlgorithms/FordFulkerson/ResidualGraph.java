package networkFlowAlgorithms.FordFulkerson;

import java.util.*;

/**
 * A class that represents a residual graph.
 * 
 * @author Raaghavi Sivaguru
 */

public class ResidualGraph {
	LinkedList<ResidualEdge> edgeList;
	LinkedList<ResidualNode> vertexList;
	
	public ResidualGraph() {
		this.edgeList = new LinkedList<>();
		this.vertexList = new LinkedList<>();
	}
	
	public Iterator<?> vertices() {
        return vertexList.iterator();
    }
	
	public Iterator<?> edges() {
        return edgeList.iterator();
    }
	
	public Iterator<?> nextEdgeList(ResidualNode v) {
        return v.nextEdgeList.iterator();
    }
	
	public ResidualNode aVertex() {
        if (vertexList.size() > 0)
            return vertexList.getFirst();
        else
            return null;
    }
	
	public void insertVertex(ResidualNode a) {
		this.vertexList.add(a);
	}
	
	public void insertEdge(ResidualEdge edge, ResidualNode start) {
		this.edgeList.add(edge);
		start.nextEdgeList.add(edge);
	}
	
	public void removeEdge(ResidualEdge edge) {
		ResidualNode start = edge.getFirstPoint();
		edgeList.remove(edge);
		start.nextEdgeList.remove(edge);
		moveToEnd(start);
	}
	
	public ResidualEdge getEdge(ResidualNode start, ResidualNode end) {
		Iterator<?> iterator = nextEdgeList(start);
		ResidualEdge result;
		while (iterator.hasNext()) {
			result = (ResidualEdge) iterator.next();
			if (result.getSecondPoint().getName().equals(end.getName())) {
				return result;
			}
		}
		return null;
	}
	
	public ResidualNode getSource() {
		for (int i = 0; i < vertexList.size(); i++) {
			ResidualNode v = vertexList.get(i);
			if (v.getName().equalsIgnoreCase("s")) {
				return vertexList.get(i);
			}
		}
		return null;
	}
	
	public void moveToEnd(ResidualEdge edge) {
		int index = edgeList.indexOf(edge);
		edgeList.remove(index);
		edgeList.add(edge);
	}
	
	public void moveToEnd(ResidualNode vertex) {
		int index = vertexList.indexOf(vertex);
		vertexList.remove(index);
		vertexList.add(vertex);
	}
	
	public LinkedList<ResidualNode> findPath(ResidualNode v, LinkedList<ResidualNode> path) {
		if (!v.isVisited()) {
			v.setVisited();
			path.add(v);
			moveToEnd(v);
		}
		if (v.hasUnvisitedNeighbor()) {
			ResidualNode neigh_v = v.getNeighbor();
			if (!neigh_v.getName().equalsIgnoreCase("t")) {
				findPath(neigh_v, path);
			} else {
				path.add(neigh_v);
			}
		} else {
			path.remove(v);
			if (path.size() != 0) {
				v = path.getLast();
				findPath(v, path);
			}
		}
		return path;
	}
}
