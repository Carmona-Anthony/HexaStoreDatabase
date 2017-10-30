import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class RequestParser {
	
	ArrayList<CustomStatement> requests;

	RequestParser(){
		requests = new ArrayList<>();
	}
	
	public ArrayList<CustomStatement> parse(String request) {
		
		String[] split = request.split("SELECT");
		String prefixes = split[0];
		String requete = split[1];
		HashMap<String, String> prefix = getPrefixes(prefixes);
		
		String[] statements = requete.substring(requete.indexOf("{") + 1, requete.indexOf("}")).split(" . ");
		ArrayList<String> results = new ArrayList<>();
		for(String statement : statements) {
			for (Entry<String, String> entry : prefix.entrySet()) {
				statement = statement.replaceFirst(entry.getKey(), entry.getValue()).trim();
			}
			String[] splitStatement = statement.split(" ");
			requests.add(new CustomStatement(splitStatement[0],splitStatement[1],splitStatement[2]));
		}
		return requests;
	}
	
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
	
	public ArrayList<CustomStatement> getStatements(){
		return requests;
	}

}
