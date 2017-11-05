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
 * @author Proprietaire
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
		
		int minSize = Integer.MAX_VALUE;
		PredicateObject minPredicateObject = null;
		ArrayList<HashSet<Integer>> subjectList = new ArrayList<>();
		for(CustomStatement customStatement : statements) {
			PredicateObject predicateObject = new PredicateObject(dataHandler.getId(customStatement.getPredicate()),dataHandler.getId(customStatement.getObject()));
			if(minSize > dataHandler.getSize(predicateObject)) {
				minSize = dataHandler.getSize(predicateObject);
				minPredicateObject = predicateObject;
			}
			
			subjectList.add(dataHandler.getSubjects(predicateObject));
		}
		
		HashSet<Integer> results = new HashSet<>();
		HashSet<Integer> subjects = dataHandler.getSubjects(minPredicateObject);
		if(subjects != null) {
			Queue<Integer> queue = new PriorityQueue<>(subjects);
			while(queue.size() > 0) {
				boolean exist = true;
				int currentSubject = queue.poll();
				for(HashSet<Integer> subjectSet : subjectList) {
					if(!subjectSet.contains(currentSubject)) {
						exist = false;
						break;
					}
				}
				if(exist) {
					results.add(currentSubject);
				}
			}
		}		
		return results;
	}
}
