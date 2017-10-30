import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;

public class RequestHandler {
	
	DataHandler dataHandler;
	RequestParser requestParser;
	
	RequestHandler(DataHandler dataHandler){
		this.dataHandler = dataHandler;
		this.requestParser = new RequestParser();
	}
	
	public HashSet<Integer> exec(String request) {
		ArrayList<CustomStatement> statements = parse(request);
		return solve(statements);
	}
	
	public ArrayList<CustomStatement> parse(String request) {
		return requestParser.parse(request);
	}
	
	public HashSet<Integer> solve(ArrayList<CustomStatement> statements) {
		
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
		return results;
	}
}
