import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.HashSet;

import org.openrdf.model.Statement;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.Rio;
import org.openrdf.rio.helpers.RDFHandlerBase;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public final class RDFRawParser {
	
	static DataHandler dataHandler = new DataHandler();
	
	@Parameter(names = "-debug", description = "Debug mode", arity = 1)
	private boolean debug = true;
	
	@Parameter(names = "--help", help = true)
	private boolean help = false;


	private static class RDFListener extends RDFHandlerBase {
		
		@Override
		public void handleStatement(Statement st) {
			dataHandler.add(st);
		}
	};

	public static void main(String args[]) throws FileNotFoundException {
		
		/*
		 * Parse Options
		 */
		
		 RDFRawParser main = new RDFRawParser();
		 JCommander jcommander = JCommander.newBuilder().build();
		 jcommander.setProgramName("HexaStore Playground");
		 jcommander.addObject(main);
         jcommander.parse(args);
		 
		 if(main.help) {
			 jcommander.usage();
		 }
		 
	     main.run();
	}		
	
	public void run() throws FileNotFoundException {
		
		System.out.println("Debug : " + debug);

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
		
		RequestHandler requestHandler = new RequestHandler(dataHandler);
		
		HashSet<Integer> results = requestHandler.exec(
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "+
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
				"PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#>"+

				"SELECT ?x " +
				"WHERE {?x rdf:type ub:Subj18Student .  ?x rdf:type ub:GraduateStudent . ?x rdf:type ub:ResearchAssistant }");
		
		for(int subject : results) {
			System.out.println(dataHandler.getValue(subject));
		}
	}
}