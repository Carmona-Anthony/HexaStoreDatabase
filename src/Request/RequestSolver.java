package Request;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;

import DataManaging.DataHandler;
import Models.CustomStatement;
import Models.PredicateObject;

/**
 * Class that handle the request solving
 * @author
 *
 */
public class RequestSolver {
	
	RequestSolver(){
		
	}
	
	/**
	 * Get results from a dataHander and a list of clauses
	 * @param dataHandler Class that contains the dictionnaries and indexes
	 * @param statements The list of clauses
	 * @return Result list that contains the ids of the subjects
	 */
	public HashSet<Integer> solve(DataHandler dataHandler, ArrayList<CustomStatement> statements) {
		// Complexity : O(C) + O(C) * O(N) 
		// O(C) : Number of clauses
		// O(N) : Number of subjects
		HashSet<Integer> results = new HashSet<>();
		
		int minSize = Integer.MAX_VALUE;
		PredicateObject minPredicateObject = null;
		HashSet<PredicateObject> clauses = new HashSet<>();
		
		//Get the clause that return the minimal number of subjects and the list of subjects associated
		for(CustomStatement customStatement : statements) { //O(C) with c number of clauses
			
			int idPredicate = dataHandler.getId(customStatement.getPredicate());
			int idObject = dataHandler.getId(customStatement.getObject());
			
			//If idPredicate and idObject exists
			if(idPredicate != -1 && idObject != -1) {
				PredicateObject predicateObject = new PredicateObject(idPredicate,idObject);
				
				clauses.add(predicateObject);
				
				if(dataHandler.getSize(predicateObject) == -1) {
					return results;
				}else {
					int currentSize = dataHandler.getSize(predicateObject);
					if(minSize > currentSize) {
						minSize = currentSize;
						minPredicateObject = predicateObject;
					}
				}
			}
		}
		//for each subjects found earlier (Minimal predicate object result) check if the subject is associated with each clause of the request
		if (minPredicateObject != null) {
			HashSet<Integer> subjectsMin = new HashSet<>();
			subjectsMin = dataHandler.getSubjects(minPredicateObject); // O(1)

			for (Integer subject : subjectsMin) { // Worst Case O(N) with n number of subjects
				boolean exist = true;
				for (PredicateObject clause : clauses) { // O(C)
					if (!dataHandler.exist(subject, clause)) { // O(1)
						exist = false;
						break;
					}
				}
				if (exist) results.add(subject);
			}
		}
		return results;
	}
}
