package Request;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import DataManaging.DataHandler;
import Models.CustomStatement;

/**
 * Controller class for requestSolving
 * @author Proprietaire
 *
 */
public class RequestController {
	
	/**
	 * Simple requestParser
	 */
	RequestParser requestParser;
	/**
	 * DataHandler that contains data, dictionnaries and indexes
	 */
	DataHandler dataHandler;
	/**
	 * Solver
	 */
	RequestSolver requestSolver;

	public RequestController(DataHandler dataHandler){
		this.requestParser = new RequestParser();
		this.requestSolver = new RequestSolver();
		this.dataHandler = dataHandler;
	}
	
	/**
	 * Solve all the request for a given file
	 * @param fileReader the file that contains all requests
	 * @return the list of results <Request , List of results>
	 */
	public HashMap<String, HashSet<String>> solve(FileReader fileReader) {
		/**
		 * Optimize reading for request storage
		 */
		ArrayList<String> requests = requestParser.parseFile(fileReader);
		HashMap<String, HashSet<String>> results = new HashMap<>();
		for (String request : requests) {
			 results.put(request, solve(request));
		}
		
		return results;
	}
	
	/**
	 * Solving method
	 * @param request the request we need to solve
	 * @return the list of results
	 */
	public HashSet<String> solve(String request) {
		
		System.out.println("Start Request : " + request);
		System.out.println("---------- Execution de la requete ----------");
		long startTime = System.nanoTime();
		
		ArrayList<CustomStatement> statements = requestParser.parse(request);
		HashSet<Integer> idResults = requestSolver.solve(dataHandler, statements);
		
		HashSet<String> finalResults = new HashSet<>();
		
		for(int subject : idResults) {
			finalResults.add(dataHandler.getValue(subject));
		}
		
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		long seconds = (duration / 1000) % 60;
		// formatedSeconds = (0.xy seconds)
		String formatedSeconds = String.format("(0.%d seconds)", seconds);
		System.out.println("Execution = " + formatedSeconds);
		// i.e actual formatedSeconds = (0.52 seconds)
		
		return finalResults;
		
	}
}
