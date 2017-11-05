package Models;

/**
 * Custom Class that represents a couple < Predicate , Object>
 * @author Proprietaire
 *
 */
public class PredicateObject {
	
	/**
	 * An array to store the couple predicate object. Array[0] = predicate and Array[1] = object
	 */
	int[] predicateObject;
	
	public PredicateObject(int predicate, int object){
		predicateObject = new int[2];
		predicateObject[0] = predicate;
		predicateObject[1] = object;
	}
	
	/**
	 * Create custom hashcode for managing hashMap
	 */
	 @Override
	 public int hashCode() {
		 return (predicateObject[0]+":"+predicateObject[1]).hashCode();
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
		return predicateObject[0];
	}
	
	public int getObject() {
		return predicateObject[1];
	}

	public String toString() {
		return "Predicat : " + predicateObject[0] + " Object : " + predicateObject[1];
	}
}
