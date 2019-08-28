package networkFlowAlgorithms.FordFulkerson;

/**
 * A class that represents the Ford Fulkerson algorithm to compute max flow
 * 
 * @author Raaghavi Sivaguru
 */

import graphCode.Edge;
import graphCode.SimpleGraph;
import graphCode.Vertex;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class FordFulkerson 
{
	public ResidualGraph graph;
	public double maxFlow;
	public ResidualNode source;
	public SimpleGraph sGraph;
	
	public FordFulkerson(SimpleGraph sGraph) {
		this.sGraph = sGraph;
	}
	public double getMaxFlow() {
		this.graph = new ResidualGraph();
		this.maxFlow = 0; 

		addVerticesToResidualGraph(sGraph);
		this.source = graph.getSource();
		addEdgesToResidualGraph(sGraph);
		
		List<ResidualNode> path;
		
		//find s-t path and update the residual graph if there exists a path
		while ((path = findSTpath()).size() != 0) {
			double minValue = getBottleneckCapacity(path);
			updateResidualGraph(path, minValue);
			maxFlow += minValue;
		}
		
		return maxFlow;
	}
	
	private void addVerticesToResidualGraph(SimpleGraph sGraph) {
		Iterator vertices = sGraph.vertices();
		
		while(vertices.hasNext()) {
			Vertex vertex = (Vertex)vertices.next();
			ResidualNode v = new ResidualNode(vertex.getName());
			graph.insertVertex(v);
		}
	}
	
	private void addEdgesToResidualGraph(SimpleGraph sGraph) {
		Iterator edges = sGraph.edges();
		while(edges.hasNext()) {
			Edge e = (Edge)edges.next();
			Vertex v1 = e.getFirstEndpoint();
			Vertex u1 = e.getSecondEndpoint();
			ResidualNode v2 = null;
			ResidualNode u2 = null;
			Iterator vertices = graph.vertices();
			while (vertices.hasNext()) {
				ResidualNode curV = (ResidualNode) vertices.next();
				if (curV.getName().equalsIgnoreCase((String) v1.getName())) {
					v2 = curV;
				}
				if (curV.getName().equalsIgnoreCase((String) u1.getName())) {
					u2 = curV;
				}
			}
			ResidualEdge edge = new ResidualEdge(v2, u2, (double) e.getData());
			graph.insertEdge(edge, v2);
		}	
	}
	
	private LinkedList<ResidualNode> findSTpath() {
		LinkedList<ResidualNode> path = new LinkedList<>();
		path = graph.findPath(source, path);
		updateNodeStatus();
		source.setUnvisited();
		graph.moveToEnd(source);
		return path;
	}
	
	private void updateNodeStatus() {
		Iterator vertices = graph.vertices();
		while (vertices.hasNext()) {
			ResidualNode v = (ResidualNode) vertices.next();
			v.setUnvisited();
		}
	} 
	private double getBottleneckCapacity(List<ResidualNode> path) {
		double min = Double.MAX_VALUE;
		for (int i = 0; i < path.size() - 1; i++) {
			ResidualNode v = path.get(i);
			ResidualNode u = path.get(i + 1);
			ResidualEdge e = graph.getEdge(v, u);
			min = Math.min(min, e.getCapacity());
		}
		return min;
	}
	
	private void updateResidualGraph(List<ResidualNode> path, double minValue) {
		for (int i = 0; i < path.size() - 1; i++) {
			ResidualNode v = path.get(i);
			ResidualNode u = path.get(i + 1);
			
			ResidualEdge forward = graph.getEdge(v, u);
			if (forward.getCapacity() == minValue) {
				graph.removeEdge(forward);
			} else {
				forward.setCapacity(forward.getCapacity() - minValue);
				graph.moveToEnd(forward);
			}
			
			ResidualEdge backward = graph.getEdge(u, v);
			if (backward == null) {
				backward = new ResidualEdge(u, v, minValue);
				graph.insertEdge(backward, u);
			} else {
				backward.setCapacity(backward.getCapacity() + minValue);
				graph.moveToEnd(backward);
			}
		}
	}
}
