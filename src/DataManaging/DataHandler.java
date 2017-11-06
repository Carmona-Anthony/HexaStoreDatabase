package DataManaging;
import java.util.HashMap;
import java.util.HashSet;


import org.openrdf.model.Statement;

import Models.PredicateObject;

/**
 * Class that contains the dictionnaries and indexes for dataManagement of rdf
 * @author 
 *
 */
public class DataHandler {
	
	/**
	 * HashMap <id, Value>
	 * Size : Number of distincts values in the model
	 * Access : O(1)
	 * Add : O(1)
	 */
	HashMap<Integer,String> ids;
	
	/**
	 * HashMap <Value, id>
	 * Size : Number of distincts values in the model
	 * Access : O(1)
	 * Add : O(1)
	 */
	HashMap<String,Integer> values;
	
	/**
	 * HashMap that contains for a couple <Predicate,Object> the list of subjects associated
	 * Size : Number of distinct couples <Predicate,Object> * number of subjects associated
	 * Access : O(1)
	 * Add : O(1)
	 */
	HashMap<PredicateObject, HashSet<Integer>> pos;
	
	/**
	 * HashMap that contains for a subject the list of couple <Predicate,Object> associated
	 * Size : Number of distinct couples <Predicate,Object> * number of subjects associated
	 * Access : O(1)
	 * Add : O(1)
	 */
	HashMap<Integer, HashSet<PredicateObject>> spo;
	
	/**
	 * Simple counter for mapping between value and id
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
	
	/**
	 * Check for a given subject if he is associated with a given couple <PredicateObject>
	 * @param subject 
	 * @param predicateObject
	 * @return true if the subject if associated with the given predicateObject else false
	 */
	public boolean exist(int subject, PredicateObject predicateObject) {
		return spo.get(subject).contains(predicateObject);
	}
	/**
	 * Get all subjects associated with a predicateObject
	 * @param predicateObject
	 * @return the list of subjects associated with the given predicateObject
	 */
	public HashSet<Integer> getSubjects(PredicateObject predicateObject){
		return pos.get(predicateObject);
	}
	
	/**
	 * Get the number of subjects associated with a couple predicateObject
	 * @param predicateObject
	 * @return the number of subjects associated with a couple predicateObject
	 */
	public int getSize(PredicateObject predicateObject) {
		if(pos.get(predicateObject) == null) return -1;
		return pos.get(predicateObject).size();
	}
	
	/**
	 * Get the string value for a given id
	 * @param id
	 * @return the value for a given id
	 */
	public String getValue(int id) {
		if(ids.get(id) != null) {
			return ids.get(id);
		}
		return null;
	}
	
	/**
	 * Get the id for a string value
	 * @param value
	 * @return the id associated with a given string value
	 */
	public int getId(String value) {
		if(values.get(value) != null) {
			return values.get(value);
		}
		return -1;
	}
}
