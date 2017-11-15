package Request;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import Models.CustomStatement;
import Utils.TimerHandler;

/**
 * Class that handle the parsing of a SPARQL Request
 * @author
 *
 */
public class RequestParser {
	
	TimerHandler timerHandler;
	static int counter = 1;
	
	RequestParser(){

	}
	
	RequestParser(TimerHandler timerHandler){
		this.timerHandler = timerHandler;
	}
	
	/**
	 * Parse the selected request
	 * @param request the request to parse
	 * @return A list of clauses
	 */
	public ArrayList<CustomStatement> parse(String request) {
		// Complexity : O(P) + O(P) * O(C)
		// O(C) : Number of clauses
		// O(P) : Number of prefixes
		String[] split = request.split("SELECT");
		
		String prefixes = null;
		String requete = null;
		HashMap<String, String> prefix = new HashMap<>();
		
		if (!request.isEmpty() || !request.equals("")) {
			prefixes = split[0];
			requete = split[1];
			prefix = getPrefixes(prefixes); //O(P)
			return parseSelect(requete,prefix); //O(C) * O(P)
		}
		
		return null;
	}
	
	/**
	 * Get the prefixes from the request
	 * @param prefixes
	 * @return A Map <Prefix, Value>
	 */
	private HashMap<String, String> getPrefixes(String prefixes){
		
		HashMap<String, String> result = new HashMap<>(); 
		
		String [] prefixe = prefixes.split("PREFIX");
		for(int i=1; i<prefixe.length; i++) {
			String[] aliasToValue = prefixe[i].trim().split(" ",2);
			String value = aliasToValue[1].substring(aliasToValue[1].indexOf("<") + 1, aliasToValue[1].indexOf(">"));
			result.put(aliasToValue[0].trim(),value.trim());
		}
		return result;
	}
	
	/**
	 * Parse the select and replace prefixes with there value
	 * @param requete the request to parse
	 * @param prefix the list of prefixes
	 * @return A List of clauses
	 */
	private ArrayList<CustomStatement> parseSelect(String requete,HashMap<String, String> prefix) {
		
		ArrayList<CustomStatement> requests = new ArrayList<>();
		//Get the substring between { } to get each clauses
		String result = requete.substring(requete.indexOf("{") + 1, requete.indexOf("}"));
		
		//Remove every blank space around the result
		result = result.trim();
		
		String regex = " \\.";
		
		//Split by . to get the list of statements/clauses
		String[] statements = result.split(regex);
		
		for(String statement : statements) { //O(C)
			//for each clauses replace by the prefix if needed
			for (Entry<String, String> entry : prefix.entrySet()) { //O(P)
				statement = statement.replaceFirst(entry.getKey(), entry.getValue()).trim();
			}
			//Get each part of the clause (Subject , Predicate , Object)
			String[] splitStatement = statement.split(" ");
			// If uri contains < > takes substring
			splitStatement[0] = splitStatement[0].replace("<", "").replace(">", "").trim();
			splitStatement[1] = splitStatement[1].replace("<", "").replace(">", "").trim();
			splitStatement[2] = splitStatement[2].replace("<", "").replace(">", "").trim();
		
			requests.add(new CustomStatement(splitStatement[0], splitStatement[1], splitStatement[2]));
		}
		
		timerHandler.tour("Parsing R" + counter);
		counter++;
		
		return requests;
	}
	/**
	 * Parse a file from fileReader to get all requests
	 * @param fileReader
	 * @return
	 */
	public ArrayList<String> parseFile(FileReader fileReader){
		/*
		 * Optimize reading for request storage
		 */
		ArrayList<String> requests = new ArrayList<String>();
		try{
		    BufferedReader in = new BufferedReader(fileReader);
		    String currentRequest = "";
		    String line;
		    
		    while((line = in.readLine()) != null){
		    	
		    	if(line.isEmpty() && !currentRequest.equals("")) {
		    		requests.add(currentRequest);
		    		currentRequest = "";
		    	}
		    	else {
		    		if(line.trim().endsWith("}")) {
		    			currentRequest += line;
				    	requests.add(currentRequest);   
				    	currentRequest = "";
				    }
		    		else currentRequest += line;
		    	}
			}
			if (!currentRequest.isEmpty() || !currentRequest.equals("")) {
				requests.add(currentRequest);
			}
		    
		}catch(Exception e){
		    e.printStackTrace();
		}	
		return requests;
	}
}