package networkFlowAlgorithms.PreFlowPush;

/*
 * @author Suganya Jeyaraman.
 */

import graphCode.Edge;
import graphCode.SimpleGraph;
import graphCode.Vertex;

import java.util.Iterator;
import java.util.LinkedList;

public class PreFlowPush {
	    
	    Vertex excessFlowVertex;
	    Double maxflow = (double) 0;
	   
	    LinkedList<Vertex> vertexList;
	    LinkedList<Edge> edgeList;
	    
	public double maxFlowPFP(SimpleGraph g)
	{
		this.InitializeGraph(g);
		this.PreFlow(g);
		
		//When there is a excessFlow vertex in the graph, perform Push or Relabel 
		while(ExcessFlowVertex() != false)
		{
			if(Push(excessFlowVertex, g) != true)
			{
				Relabel(excessFlowVertex, g);
			}	
		}
		
		//Once there is no excessFlow vertex in G, Check the excessFlow in Sink 
		for(Iterator<Vertex> i = vertexList.iterator(); i.hasNext();)
		  {
			  Vertex v1 = i.next();
			  
			  if(v1.getName().equals("t"))
			  {
				PFPVertex sinkVertexData = (PFPVertex) v1.getData();
				maxflow = sinkVertexData.excessFlow;  
			  }
		  }
		
		return maxflow;
	}
	
	public void Relabel(Vertex v, SimpleGraph g)
	{
		
		int minHeight = Integer.MAX_VALUE;
		
		for(Iterator<Edge> i = g.incidentEdges(v); i.hasNext();)
		  {
			Edge e1 = i.next();
			
			if(e1.getFirstEndpoint() == v)
			{
				//If edge capacity and flow are same, no relabeling happens
				PFPEdge edgeData = (PFPEdge) e1.getData();
				if(edgeData.capacity == edgeData.flow)
				{
					continue;
				}
				
				//To get the height of the adjacent vertex
				Vertex adjacentVertex = e1.getSecondEndpoint();
				PFPVertex adjacentVertexData = (PFPVertex) adjacentVertex.getData();
				
				//To get the current height of the given vertex
				PFPVertex VertexData = (PFPVertex) v.getData();
				
				//Incrementing excessFlow vertex height 
				if(adjacentVertexData.height < minHeight)
				{
					minHeight = adjacentVertexData.height;
					
					VertexData.height = minHeight +1;
				}
			}

		  }
	}
	
	
	public Boolean Push(Vertex v, SimpleGraph g)
	{
		
		for(Iterator<Edge> i = g.incidentEdges(v); i.hasNext();)
		  {
			Edge e1 = i.next();
			
			if(e1.getFirstEndpoint() == v)
			{
				//If edge capacity and flow are same, Push is not possible to that adjacent vertex
				PFPEdge edgeData = (PFPEdge) e1.getData();
				if(edgeData.capacity == edgeData.flow)
				{
					continue;
				}
				
				//To get the height of the excessFlow vertex
				PFPVertex excessFlowVertex = (PFPVertex) v.getData();
				
				//To get the height of the adjacent vertex
				Vertex adjacentVertex = e1.getSecondEndpoint();
				PFPVertex adjacentVertexData = (PFPVertex) adjacentVertex.getData();
				
				double remainingFlow = edgeData.capacity - edgeData.flow;
				
				if(adjacentVertexData.height < excessFlowVertex.height)
				{
					//Finding minimum value among excessFlow and the remaining possible flow on an edge 
					double flow = (double) Math.min(excessFlowVertex.excessFlow, remainingFlow);
					
					//Reduce excessFlow value in excessFlowVertex
					excessFlowVertex.excessFlow -= flow;
					
					//Updating excess flow in the adjacent vertex 
					adjacentVertexData.excessFlow += flow;
					
					//Updating the edge with flow value
					edgeData.flow += flow;
					
					//Updating the residual flow in the graph
					UpdateReverseEdgeFlow(g, e1, flow); 
					
					return true;
				}
			
			}
			
		  }
		
		return false;
	}
	
	public void UpdateReverseEdgeFlow(SimpleGraph g, Edge e, double flow)
	{
	    Vertex u = e.getFirstEndpoint();
	    Vertex v = e.getSecondEndpoint();
	    
	    //Updating flow in existing backward edge
	    for(Iterator<Edge> i = edgeList.iterator(); i.hasNext();)
		  {
			  Edge e1 = i.next();
			  if(e1.getFirstEndpoint() == v && e1.getSecondEndpoint() == u)
			  {
				  PFPEdge edgeData = (PFPEdge)e1.getData();
				  edgeData.flow -= flow; //should we reduce the flow or increase the flow here ??
				  return;
				  
			  }
		  }
	    
	    //Updating flow for a new backward edge 
	    PFPEdge backwardEdgeData = new PFPEdge(-flow, 0);
	    g.insertEdge(v, u, backwardEdgeData, null);
	    
	    return;

	}
	public Boolean ExcessFlowVertex()
	{
		//Iterate through vertex list to find a vertex excess flow greater than zero
		for(Iterator<Vertex> i = vertexList.iterator(); i.hasNext();)
		  {
			  Vertex v1 = i.next();
			  
			  //Ignore the excessFlow in Source and Sink
			  if(!v1.getName().equals("s") && !v1.getName().equals("t"))
			  {
				  PFPVertex vertexData = (PFPVertex)v1.getData();
				  
				  if(vertexData.excessFlow > 0)
				  {
					  excessFlowVertex = v1;
					  return true;
				  }
			  }
		   }
		
		return false;
	}
	
	public void PreFlow(SimpleGraph g)
	{
		LinkedList reverseEdgeList = new LinkedList();
		
		for(Iterator<Edge> i = edgeList.iterator(); i.hasNext();)
		{
			Edge e1 = i.next();
			if(e1.getFirstEndpoint().getName().equals("s"))
			{
				Vertex sourceVertex = e1.getFirstEndpoint();
				
				// Set Flow is equal to capacity for adjacent vertices from S 
				PFPEdge edgeData = (PFPEdge) e1.getData();
				edgeData.flow = edgeData.capacity;
				e1.setData(edgeData);
				
				// Initialize excess flow for adjacent v 
				Vertex adjacentVertex = e1.getSecondEndpoint();
				PFPVertex vertexData = (PFPVertex) adjacentVertex.getData();
				vertexData.excessFlow += edgeData.flow;
				
				
				
				// Add an edge from v to s in residual graph with 
	            // capacity equal to 0
				PFPEdge backwardEdgeData = new PFPEdge(-edgeData.flow, 0);
				Edge backwardEdge = new Edge(adjacentVertex, sourceVertex, backwardEdgeData, null);
				reverseEdgeList.add(backwardEdge);
				}
	
		}
		
		///Update the edge list with the reverse edges 
		for(Iterator<Edge> j = reverseEdgeList.iterator(); j.hasNext();)
		  {
			  Edge e  = j.next();
			  g.insertEdge(e.getFirstEndpoint(), e.getSecondEndpoint(), e.getData(), null);
		  }

	}
	
	public void InitializeGraph(SimpleGraph g)
	{
		  // Get the vertex list & edge list
		  this.vertexList = g.vertexList;
		  this.edgeList = g.edgeList;
		  
		  //For initial condition Set the height and excessFlow in all the vertex to zero
		  for(Iterator<Vertex> i = vertexList.iterator(); i.hasNext();)
		  {
			  Vertex v1 = i.next();
			  
			  int height = 0;			  
			  if(v1.getName().equals("s"))
			  {
				  // Set source height to total number of vertices
				  height = this.vertexList.size();
			  }
			 
			  // Set height and excess flow in PFPVertex and assign to Vertex data
			  v1.setData(new PFPVertex(height, 0)); 
		  }
		  
		  //For initial condition Set the flow in all the edges to zero
		  for(Iterator<Edge> i = edgeList.iterator(); i.hasNext();)
		  {
			  Edge e1 = i.next();
			  
			  double capacity = (double)e1.getData();
			  
			  // Set flow to 0 in PFPEdge and assign to Edge data
			  e1.setData(new PFPEdge(0, capacity)); 
		  }
		  
		  
	}
}