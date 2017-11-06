package Request;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import DataManaging.DataHandler;
import Models.CustomStatement;
import Utils.TimerHandler;

/**
 * Controller class for requestSolving
 * @author
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
	/**
	 * Timer Handler
	 */
	TimerHandler timerHandler;

	public RequestController(DataHandler dataHandler){
		this.requestParser = new RequestParser();
		this.requestSolver = new RequestSolver();
		this.dataHandler = dataHandler;
		this.timerHandler = new TimerHandler();
	}
	
	public RequestController(DataHandler dataHandler, TimerHandler timerHandler){
		this.requestParser = new RequestParser();
		this.requestSolver = new RequestSolver();
		this.dataHandler = dataHandler;
		this.timerHandler = timerHandler;
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
		
		ArrayList<String> requests = requestParser.parseFile(fileReader);
		
		HashMap<Integer, HashSet<String>> results = new HashMap<>();
		int compteur = 0;
		for (String request : requests) {
			compteur++;
			results.put(compteur, solve(request));
			
			timerHandler.tour("R" + compteur);
		}
		return results;
	}
	
	/**
	 * Solving method
	 * @param request the request we need to solve
	 * @return the list of results
	 */
	public HashSet<String> solve(String request) {
		
		ArrayList<CustomStatement> statements = requestParser.parse(request);
		
		HashSet<Integer> idResults = requestSolver.solve(dataHandler, statements);
		
		HashSet<String> finalResults = new HashSet<>();
		
		for(int subject : idResults) {
			finalResults.add(dataHandler.getValue(subject));
		}
		
		return finalResults;
		
	}
}
