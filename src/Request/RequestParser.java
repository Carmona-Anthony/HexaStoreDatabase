package Request;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import Models.CustomStatement;

/**
 * Class that handle the parsing of a SPARQL Request
 * @author Proprietaire
 *
 */
public class RequestParser {
	
	RequestParser(){

	}
	
	/**
	 * Parse the selected request
	 * @param request the request to parse
	 * @return A list of clauses
	 */
	public ArrayList<CustomStatement> parse(String request) {
		
		String[] split = request.split("SELECT");
		String prefixes = split[0];
		String requete = split[1];
		HashMap<String, String> prefix = getPrefixes(prefixes);
		
		return parseSelect(request,prefix);
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
		
		String[] statements = requete.substring(requete.indexOf("{") + 1, requete.indexOf("}")).split(" . ");
		for(String statement : statements) {
			for (Entry<String, String> entry : prefix.entrySet()) {
				statement = statement.replaceFirst(entry.getKey(), entry.getValue()).trim();
			}
			String[] splitStatement = statement.split(" ");
			
			//If uri contains < > takes substring
			splitStatement[0] = splitStatement[0].replace("<", "").replace(">", "").trim();
			splitStatement[1] = splitStatement[1].replace("<", "").replace(">", "").trim();
			splitStatement[2] = splitStatement[2].replace("<", "").replace(">", "").trim();
			
			requests.add(new CustomStatement(splitStatement[0],splitStatement[1],splitStatement[2]));
		}
		return requests;
	}
	
	public ArrayList<String> parseFile(FileReader fileReader){
		
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
		    	else currentRequest += line;
		    }
		    
		    requests.add(currentRequest);
		    
		}catch(Exception e){
		    e.printStackTrace();
		}
		
		return requests;
	}

}