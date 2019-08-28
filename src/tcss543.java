import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import graphCode.GraphInput;
import graphCode.SimpleGraph;
import networkFlowAlgorithms.FordFulkerson.FordFulkerson;
import networkFlowAlgorithms.PreFlowPush.PreFlowPush;
import networkFlowAlgorithms.ScalingMaxFlow.ScalingFordFulkerson;

/**
 * Main class that invokes 3 network flow algorithms 
 * to compute max flow for the input graphs
 * 
 * @author Raaghavi Sivaguru, Suganya Jeyaraman, Sindhuja Chandran
 */

public class tcss543 {
	
	public static void main(String[] args) throws Exception {
		String directory = args[0];
		File fname = new File(directory);
		if (fname.isDirectory()) {
			directory = directory + "/";
			String[] files = getFiles(directory);
			if (files != null) {
				runFordFulkerson(directory, files);
				Thread.sleep(1000);
				runScalingMaxFlow(directory, files);
				Thread.sleep(1000);
				runPreFlowPush(directory, files);
			} 
		} else if (fname.isFile()) {
			runFordFulkerson(fname);
			runScalingMaxFlow(fname);
			runPreFlowPush(fname);
		}
	}
	
	private static void runFordFulkerson(File fname) {
		SimpleGraph G = new SimpleGraph();
  	  	GraphInput.LoadSimpleGraph(G, fname.getAbsolutePath());
		FordFulkerson ff = new FordFulkerson(G);
		double maxflow = 0;
		double runtime = 0;
		
		System.out.println("Started FORD FULKERSON ALGORITHM");
		for(int j=0; j<10; j++) {
			long start = System.currentTimeMillis();
			maxflow = ff.getMaxFlow();
			long end = System.currentTimeMillis();
			runtime = runtime + (end - start);
		}
		System.out.println("Maxflow: "+maxflow+" Average runtime: "+runtime/10+" ms");
	}
	
	private static void runFordFulkerson(String directory, String[] files) {
		int n_files = files.length;
		double[] runtimes = new double[n_files];
		double[] maxflows = new double[n_files];
		System.out.println("Started FORD FULKERSON ALGORITHM");
		for (int i=0; i<n_files; i++){
			File fname = new File(directory+files[i]);
			SimpleGraph G = new SimpleGraph();
	  	  	GraphInput.LoadSimpleGraph(G, fname.getAbsolutePath());
			FordFulkerson ff = new FordFulkerson(G);
			double maxflow = 0;
			double runtime = 0;
			for(int j=0; j<10; j++) {
				long start = System.currentTimeMillis();
				maxflow = ff.getMaxFlow();
				long end = System.currentTimeMillis();
				runtime = runtime + (end - start);
			}
			maxflows[i] = maxflow;
			runtimes[i] = runtime/10;
			System.out.println("Maxflow: "+maxflow+" Average runtime: "+runtimes[i]+" ms");
		}
		saveResults("ford-fulkerson", files, runtimes, maxflows);
	}
	
	private static void runScalingMaxFlow(File fname) throws Exception {		
		double maxflow = 0;
		double runtime = 0;
		
		System.out.println("Started SCALING FORD FULKERSON ALGORITHM");
		for(int j=0; j<10; j++) {
			long start = System.currentTimeMillis();
			maxflow = ScalingFordFulkerson.scalingFordFulkersonDriver(fname.getAbsolutePath());
			long end = System.currentTimeMillis();
			runtime = runtime + (end - start);
		}
		System.out.println("Maxflow: "+maxflow+" Average runtime: "+runtime/10+" ms");
	}
	
	private static void runScalingMaxFlow(String directory, String[] files) throws Exception {
		long num_iterations = 10;
        int n_files = files.length;
        double[] runtimes = new double[n_files];
		double[] maxflows = new double[n_files];
		
        System.out.println("Started SCALING FORD FULKERSON ALGORITHM");
        for (int i = 0; i < n_files; i++) {
        	long total_time = 0;
        	double maxflow = 0;
	        for (int j = 0; j < num_iterations; j++) {
	        	File fname = new File(directory+files[i]);
	            long startTime = System.currentTimeMillis();
	            //System.out.printf("processing file " + filePath);
	            maxflow = ScalingFordFulkerson.scalingFordFulkersonDriver(fname.getAbsolutePath());
	            long endTime = System.currentTimeMillis();
	            long estimatedTime = endTime - startTime;
	            total_time = total_time + estimatedTime;
	        }
	        maxflows[i] = maxflow;
	        runtimes[i] = total_time / num_iterations;
	        System.out.println("Maxflow: "+maxflow+" Average runtime: "+runtimes[i]+" ms");
        }
        saveResults("scaling-maxflow", files, runtimes, maxflows);
	}
	
	private static void runPreFlowPush(File fname) {
		SimpleGraph G = new SimpleGraph();
  	  	GraphInput.LoadSimpleGraph(G, fname.getAbsolutePath());
		FordFulkerson ff = new FordFulkerson(G);
		double maxflow = 0;
		double runtime = 0;
		
		System.out.println("Started PRE-FLOW PUSH ALGORITHM");
		for(int j=0; j<10; j++) {
			long start = System.currentTimeMillis();
			maxflow = ff.getMaxFlow();
			long end = System.currentTimeMillis();
			runtime = runtime + (end - start);
		}
		System.out.println("Maxflow: "+maxflow+" Average runtime: "+runtime/10+" ms");
	}
	
	private static void runPreFlowPush(String directory, String[] files) {
		int n_files = files.length;
		double[] runtimes = new double[n_files];
		double[] maxflows = new double[n_files];
		
		System.out.println("Started PRE-FLOW PUSH ALGORITHM");
		for (int i=0; i<n_files; i++){
			File fname = new File(directory+files[i]);
	        double maxflow = 0;
	        double runtime = 0;
	        
	        for(int j=0; j<10; j++) {
	        	// Loading graph from input file for the Preflow Push Algorithm
	        	SimpleGraph graphPreflowPush = new SimpleGraph();
	        	GraphInput.LoadSimpleGraph(graphPreflowPush, fname.getAbsolutePath());
	        	PreFlowPush preFlowPush = new PreFlowPush();
		        long start = System.currentTimeMillis();
		        maxflow = preFlowPush.maxFlowPFP(graphPreflowPush);
		        long end = System.currentTimeMillis();
		        runtime = runtime + (end - start);
	        }       
	        runtimes[i] = runtime/10;
	        maxflows[i] = maxflow;
	        System.out.println("Maxflow: "+maxflow+" Average runtime: "+runtimes[i]+" ms");
		}
		saveResults("preflow-push", files, runtimes, maxflows);
	}
	
	private static String[] getFiles(String dirName) {
		File directory = new File(dirName);
		if (directory.isDirectory()) {
			String[] files = directory.list();
			return files;
		}
			return null;
	}
	
	private static void saveResults(String algo, String[] files, double[] runtimes, double[] maxflows) {
		PrintWriter pw = null;
		try {
			String output = new String("results-"+algo+".csv");
			pw = new PrintWriter(new File(output));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		StringBuilder builder = new StringBuilder();
		//builder.append(algo+",,\n");
		builder.append("file,maxflow,runtime\n");
		for (int i=0; i<files.length; i++) {
			builder.append(files[i]+","+maxflows[i]+","+runtimes[i]+"\n");
		}
		if (pw != null) {
			pw.write(builder.toString());
			pw.close();
			System.out.println("SUCCESS: Saved results for "+algo+"!!!");
		}
		else {
			System.out.println("ERROR: Results not saved for "+algo);
		}
	}

}
