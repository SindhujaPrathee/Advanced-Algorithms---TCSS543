package networkFlowAlgorithms.PreFlowPush;

/*
 * @author Suganya Jeyaraman.
 */


public class PFPVertex {
	
	public int height;
	public double excessFlow;
	
	public PFPVertex(int height, double excessFlow)
	{
		this.height = height;
		this.excessFlow = excessFlow;
	}
	
}