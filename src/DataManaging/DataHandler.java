package DataManaging;
import java.util.HashMap;
import java.util.HashSet;


import org.openrdf.model.Statement;

import Models.PredicateObject;

/**
 * Class that contains the dictionnaries and indexes for dataManagement of rdf
 * @author Proprietaire
 *
 */
public class DataHandler {
	
	/*
	 * HashMap <id, Value>
	 */
	HashMap<Integer,String> ids;
	
	/*
	 * HashMap <Value, id>
	 */
	HashMap<String,Integer> values;
	
	/*
	 * HashMap that contains for a couple <Predicate,Object> the list of subjects associated
	 */
	HashMap<PredicateObject, HashSet<Integer>> pos;
	
	/*
	 * HashMap that contains for a subject the list of couple <Predicate,Object> associated
	 */
	HashMap<Integer, HashSet<PredicateObject>> spo;
	
	/*
	 * Simple counter for value's id
	 */
	int counter; 
	
	public DataHandler(){
		
		ids = new HashMap<>();
		values = new HashMap<>();
		pos = new HashMap<>();
		spo = new HashMap<>();
		
		counter = 1;
	}
	
	/**
	 * Populate dictionnaries and indexes from a Statement < S P O >
	 * @param st
	 */
	public void add(Statement st) {
		
		if(!values.containsKey(st.getSubject().stringValue())) {
			ids.put(counter, st.getSubject().stringValue());
			values.put(st.getSubject().stringValue(),counter);
			counter++;
		}
		
		if(!values.containsKey(st.getPredicate().stringValue())) {
			ids.put(counter, st.getPredicate().stringValue());
			values.put(st.getPredicate().stringValue(),counter);
			counter++;
		}
		
		if(!values.containsKey(st.getObject().stringValue())) {
			ids.put(counter, st.getObject().stringValue());
			values.put(st.getObject().stringValue(),counter);
			counter++;
		}
		
		PredicateObject predicateObject = new PredicateObject(values.get(st.getPredicate().stringValue()), values.get(st.getObject().stringValue()));
		pos.computeIfAbsent(predicateObject, k -> new HashSet<>()).add(values.get(st.getSubject().stringValue()));
		spo.computeIfAbsent(values.get(st.getSubject().stringValue()), k-> new HashSet<>()).add(predicateObject);
		
	}
	
	public boolean exist(int subject, PredicateObject predicateObject) {
		return spo.get(subject).contains(predicateObject);
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
		if(pos.get(predicateObject) == null) return -1;
		return pos.get(predicateObject).size();
	}
}
