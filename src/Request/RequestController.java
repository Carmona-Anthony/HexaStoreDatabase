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
	public HashMap<Integer, HashSet<String>> solve(FileReader fileReader) {
		/**
		 * Optimize reading for request storage
		 */
		
		long start = System.currentTimeMillis();
		
		ArrayList<String> requests = requestParser.parseFile(fileReader);
		
		HashMap<Integer, HashSet<String>> results = new HashMap<>();
		int compteur = 0;
		for (String request : requests) {
			compteur++;
			results.put(compteur, solve(request));
		}
		System.out.println(compteur);
		
		System.out.println("Query pre-processing time : " + (System.currentTimeMillis() - start));
		
		return results;
	}
	
	/**
	 * Solving method
	 * @param request the request we need to solve
	 * @return the list of results
	 */
	public HashSet<String> solve(String request) {
		
		long start = System.currentTimeMillis();
		
		ArrayList<CustomStatement> statements = requestParser.parse(request);
		
		System.out.println("Query pre-processing time : " + (System.currentTimeMillis() - start));
		
		HashSet<Integer> idResults = requestSolver.solve(dataHandler, statements);
		
		HashSet<String> finalResults = new HashSet<>();
		
		for(int subject : idResults) {
			finalResults.add(dataHandler.getValue(subject));
		}
		
		return finalResults;
		
	}
}
