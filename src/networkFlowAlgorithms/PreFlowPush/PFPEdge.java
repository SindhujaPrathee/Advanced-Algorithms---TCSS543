package networkFlowAlgorithms.PreFlowPush;

/*
 * @author Suganya Jeyaraman.
 */


public class PFPEdge {

	public double flow;
	public double capacity;
	
	public PFPEdge(double flow, double capacity)
	{
		this.flow = flow;
		this.capacity = capacity;
	}
}