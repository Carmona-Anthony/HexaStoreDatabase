import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import org.openrdf.model.Statement;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.Rio;
import org.openrdf.rio.helpers.RDFHandlerBase;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import DataManaging.DataHandler;
import Request.RequestController;
import Utils.CSVUtils;

public final class RDFRawParser {
	
	static DataHandler dataHandler = new DataHandler();
	
	@Parameter(names = "-debug", description = "Debug mode", arity = 1)
	private boolean debug = true;
	
	@Parameter(names = "--help", help = true)
	private boolean help = false;
	
	@Parameter(names = "-r", description = "Unary Request to execute", arity=1)
	private String requete;
	
	@Parameter(names = "-i", description = "Input File for Data", arity=1)
	private String dataIn = "500K.owl";
	
	@Parameter(names = "-rf", description = "File that contains requests", arity=1)
	private String fileNameRequest = "Q_3_nationality_gender_type.queryset";
	
	@Parameter(names = "-o", description = "CSV result output file", arity=1)
	private static String fileOut = "results.csv";
	
	private Writer resultWriter;

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
		 
		 /*
		  * Start current Benchmark
		  */
		 Long start = System.currentTimeMillis();
		 
		 
	     try {
			main.run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     
	     System.out.println("Benchmark: " + (System.currentTimeMillis() - start));
	}		
	
	public void run() throws IOException {
		//Init writer for fileOut
		resultWriter = new FileWriter(fileOut);
		
		//Init the dataHandler
		
		Long start = System.currentTimeMillis();
		
		createDatabase();
		System.out.println("Import time : " + (System.currentTimeMillis() - start));

		RequestController requestController = new RequestController(dataHandler);
		
		//If parameter for requestFile isn't empty solve file and not unary request
		if(!fileNameRequest.equals("")) {
			
			start = System.currentTimeMillis();
			
			HashMap<Integer, HashSet<String>> results = requestController.solve(new FileReader(fileNameRequest));
			System.err.println(results.keySet().size());
			//Iterate on map to write in csv file 
			//Separator for excel
			CSVUtils.writeLine(resultWriter, "sep=,");
		    
		    for(Entry<Integer, HashSet<String>> entry : results.entrySet()) {
			    int request = entry.getKey();
			    HashSet<String> result = entry.getValue();
			    
			    String requestCounter = "R" + String.valueOf(request);
			    CSVUtils.writeLine(resultWriter, requestCounter);
			    CSVUtils.writeLine(resultWriter, result);
			    
			}
		}
		else {
			if(!requete.equals("")) {
				start = System.currentTimeMillis();
				
				HashSet<String> results = requestController.solve(requete);
				
				//Write result for unary in csv file
				for(String result : results) {
					CSVUtils.writeLine(resultWriter, result);
					System.out.println(result);
				}
			}
		}
		
		resultWriter.flush();
		resultWriter.close();
		
		System.out.println("Query + Display time : " + (System.currentTimeMillis() - start));
	}
	
	/**
	 * Create the Database (Index + Dictionnary)
	 * @throws FileNotFoundException
	 */
	public void createDatabase() throws FileNotFoundException {
		
		Reader reader = new FileReader(dataIn);

		org.openrdf.rio.RDFParser rdfParser = Rio
				.createParser(RDFFormat.RDFXML);
		rdfParser.setRDFHandler(new RDFListener());
		
		try {
			rdfParser.parse(reader, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}