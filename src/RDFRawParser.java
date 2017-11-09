import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
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
import Utils.TimerHandler;

public final class RDFRawParser {
	
	static DataHandler dataHandler = new DataHandler();
	
	@Parameter(names = "-debug", description = "Debug mode", arity = 1)
	private boolean debug = true;
	
	@Parameter(names = "--help", help = true)
	private boolean help = false;
	
	@Parameter(names = "-r", description = "Unary Request to execute", arity=1)
	private String requete;
	
	@Parameter(names = "-i", description = "Input File for Data", arity=1)
	private String dataIn = "";
	
	@Parameter(names = "-rf", description = "File that contains requests", arity=1)
	private String fileNameRequest = "";
	
	@Parameter(names = "-o", description = "CSV result output file", arity=1)
	private static String fileOut = "results.csv";
	
	@Parameter(names = "-ot", description = "CSV timer result output file", arity = 1)
	private static String timerOut = "timer.csv";
	
	//Writer for csv file that contains all results
	private Writer resultWriter;
	
	//Writer for csv file that contains all timers and benchmarks
	private Writer timerWriter;
	
	//Timer for all execution of the main
	private static TimerHandler timerHandler = new TimerHandler();
	
	//Timer for all the requests
	private static TimerHandler requestTimerHandler = new TimerHandler();
	

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
		 timerHandler = new TimerHandler();
		 timerHandler.start();
		 
		 
	     try {
			main.run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}		
	
	public void run() throws IOException {
		//Init writer for fileOut
		resultWriter = new FileWriter(fileOut);
		
		//Init writer for timerOut
		timerWriter = new FileWriter(timerOut);
		
		//Init the dataHandler
		createDatabase();
	
		//Create new tour on timer after the creation of indexes and dictionnaries
		timerHandler.tour("Import Time");
		
		RequestController requestController = new RequestController(dataHandler, requestTimerHandler);
		
		//If parameter for requestFile isn't empty solve file and not unary request
		if(!fileNameRequest.equals("")) {
			
			HashMap<Integer, HashSet<String>> results = requestController.solve(new FileReader(fileNameRequest));
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
			//if no request file is given as parameter then check if there is a request as parameter
			if(requete != null) {
				if(!requete.equals("")) {
					HashSet<String> results = requestController.solve(requete);
					
					//Write result for unary in csv file
					for(String result : results) {
						CSVUtils.writeLine(resultWriter, result);
						System.out.println(result);
					}
				}
			}
		}
		
		resultWriter.flush();
		resultWriter.close();
		
		timerHandler.tour("Query + Print");
		
		System.out.println("Timer Handler : \n" + timerHandler);
		System.out.println("Request timers : \n" + requestTimerHandler );
		
		writeTimer();
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
	
	/**
	 * Write all timers in the timerWriter
	 * @throws IOException
	 */
	public void writeTimer() throws IOException {
		
		CSVUtils.writeLine(timerWriter, "sep=:");
		CSVUtils.writeLine(timerWriter, "Full Benchmark : ");
		CSVUtils.newLine(timerWriter);
		CSVUtils.writeLine(timerWriter, timerHandler.toString());
		CSVUtils.newLine(timerWriter);
		CSVUtils.writeLine(timerWriter, "Requests : ");
		CSVUtils.writeLine(timerWriter, requestTimerHandler.toString());
		
		timerWriter.flush();
		timerWriter.close();
		
	}
}