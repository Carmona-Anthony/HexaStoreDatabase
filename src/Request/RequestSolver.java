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
		
		HashSet<Integer> results = new HashSet<>();
		
		int minSize = Integer.MAX_VALUE;
		PredicateObject minPredicateObject = null;
		HashSet<PredicateObject> clauses = new HashSet<>();
		
		for(CustomStatement customStatement : statements) {
			PredicateObject predicateObject = new PredicateObject(dataHandler.getId(customStatement.getPredicate()),dataHandler.getId(customStatement.getObject()));
			clauses.add(predicateObject);
			
			if(dataHandler.getSize(predicateObject) == -1) {
				return results;
			}else {
				if(minSize > dataHandler.getSize(predicateObject)) {
					minSize = dataHandler.getSize(predicateObject);
					minPredicateObject = predicateObject;
				}
			}
		}
		//System.out.println("Clause minimale " + dataHandler.getValue(minPredicateObject.getPredicate()) + " " + dataHandler.getValue(minPredicateObject.getObject()));
		HashSet<Integer> subjectsMin = new HashSet<>();
		subjectsMin = dataHandler.getSubjects(minPredicateObject);
		
		/*for(Integer subject : subjectsMin) {
			System.out.println("Liste sujet " + dataHandler.getValue(subject));
		}*/
		
		for (Integer subject : subjectsMin) {
			boolean exist = true;
			for (PredicateObject clause : clauses) {
				//System.out.println("Clause " + dataHandler.getValue(clause.getPredicate()) + " " + dataHandler.getValue(clause.getObject()));
				if (!dataHandler.exist(subject, clause)) {
					exist = false;
					break;
				}
			}
			if (exist)
				results.add(subject);
		}
		
		/*HashSet<Integer> results = new HashSet<>();
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
		}		*/
		return results;
	}
}
