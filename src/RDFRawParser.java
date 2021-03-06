import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;

import org.openrdf.model.Statement;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.Rio;
import org.openrdf.rio.helpers.RDFHandlerBase;

public final class RDFRawParser {
	
	static DataHandler dataHandler = new DataHandler();
	
	private static class RDFListener extends RDFHandlerBase {
		
		@Override
		public void handleStatement(Statement st) {
			dataHandler.add(st);
		}
	};

	public static void main(String args[]) throws FileNotFoundException {

		Reader reader = new FileReader(
				"University0_0.owl.xml");

		org.openrdf.rio.RDFParser rdfParser = Rio
				.createParser(RDFFormat.RDFXML);
		rdfParser.setRDFHandler(new RDFListener());
		try {
			rdfParser.parse(reader, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		RequestHandler requestHandler = new RequestHandler();
		ArrayList<CustomStatement> statements = requestHandler.parse(
					"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
					"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "+
					"PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
					"PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#>"+

					"SELECT ?x " +
					"WHERE {?x rdf:type ub:Subj18Student .  ?x rdf:type ub:GraduateStudent . ?x rdf:type ub:ResearchAssistant }");
		
		
		
		//Get min
		int minSize = Integer.MAX_VALUE;
		PredicateObject minPredicateObject = null;
		ArrayList<HashSet<Integer>> subjectList = new ArrayList<>();
		for(CustomStatement customStatement : statements) {
			System.out.println("Predicate : " + customStatement.getPredicate());
			System.out.println("Value : " + customStatement.getPredicate() + " id " + dataHandler.getId(customStatement.getPredicate()));
			PredicateObject predicateObject = new PredicateObject(dataHandler.getId(customStatement.getPredicate()),dataHandler.getId(customStatement.getObject()));
			if(minSize > dataHandler.getSize(predicateObject)) {
				minSize = dataHandler.getSize(predicateObject);
				minPredicateObject = predicateObject;
			}
			
			subjectList.add(dataHandler.getSubjects(predicateObject));
		}
		
		System.out.println(minPredicateObject);
		
		HashSet<Integer> resultats = new HashSet<>();
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
				resultats.add(currentSubject);
			}
		}
		System.out.println(resultats);
		for (int result : resultats) {
			System.out.println(dataHandler.getValue(result));
		}
	}
}