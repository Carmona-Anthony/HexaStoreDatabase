package Models;

/**
 * Custom Class that represents a couple < Predicate , Object>
 * @author Proprietaire
 *
 */
public class PredicateObject {
	
	final static int PREDICATE = 0;
	final static int OBJECT = 1;
	
	/**
	 * An array to store the couple predicate object. Array[0] = predicate and Array[1] = object
	 */
	int[] predicateObject;
	
	public PredicateObject(int predicate, int object){
		predicateObject = new int[2];
		predicateObject[PREDICATE] = predicate;
		predicateObject[OBJECT] = object;
	}
	
	/**
	 * Create custom hashcode for managing hashMap
	 */
	 @Override
	 public int hashCode() {
		 return (predicateObject[PREDICATE]+":"+predicateObject[OBJECT]).hashCode();
	 }
	 
	@Override
	public boolean equals(Object obj) {
		PredicateObject predicateObject = (PredicateObject) obj;
		if(predicateObject.getPredicate() == this.getPredicate() && predicateObject.getObject() == this.getObject()) {
			return true;
		}
		return false;
	}
	
	public int getPredicate() {
		return predicateObject[PREDICATE];
	}
	
	public int getObject() {
		return predicateObject[OBJECT];
	}

	public String toString() {
		return "Predicat : " + predicateObject[PREDICATE] + " Object : " + predicateObject[OBJECT];
	}
}
