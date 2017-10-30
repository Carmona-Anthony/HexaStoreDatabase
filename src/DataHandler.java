import java.util.HashMap;
import java.util.HashSet;


import org.openrdf.model.Statement;

public class DataHandler {
	
	HashMap<Integer,String> ids;
	HashMap<String,Integer> values;
	
	HashMap<PredicateObject, HashSet<Integer>> pos;
	HashMap<Integer, HashSet<PredicateObject>> spo;
	int compteur; 
	
	DataHandler(){
		
		ids = new HashMap<>();
		values = new HashMap<>();
		pos = new HashMap<>();
		spo = new HashMap<>();
		
		compteur = 1;
	}
	
	
	public void add(Statement st) {
		
		if(!values.containsKey(st.getSubject().stringValue())) {
			ids.put(compteur, st.getSubject().stringValue());
			values.put(st.getSubject().stringValue(),compteur);
			compteur++;
		}
		
		if(!values.containsKey(st.getPredicate().stringValue())) {
			ids.put(compteur, st.getPredicate().stringValue());
			values.put(st.getPredicate().stringValue(),compteur);
			System.out.println("Predicate : " + st.getPredicate().stringValue());
			compteur++;
		}
		
		if(!values.containsKey(st.getObject().stringValue())) {
			ids.put(compteur, st.getObject().stringValue());
			values.put(st.getObject().stringValue(),compteur);
			compteur++;
		}
		
		PredicateObject predicateObject = new PredicateObject(values.get(st.getPredicate().stringValue()), values.get(st.getObject().stringValue()));
		pos.computeIfAbsent(predicateObject, k -> new HashSet<>()).add(values.get(st.getSubject().stringValue()));
		spo.computeIfAbsent(values.get(st.getSubject().stringValue()), k-> new HashSet<>()).add(predicateObject);
		
	}
	
	public String getValue(int id) {
		return ids.get(id);
	}
	public HashSet<Integer> getSubjects(PredicateObject predicateObject){
		return pos.get(predicateObject);
	}
	
	public int getId(String value) {
		return values.get(value);
	}
	public int getSize(PredicateObject predicateObject) {
		return pos.get(predicateObject).size();
	}
}
