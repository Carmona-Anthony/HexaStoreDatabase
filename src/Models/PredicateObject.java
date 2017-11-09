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
	int predicate;
	int object;
	
	public PredicateObject(int predicate, int object){
		this.predicate = predicate;
		this.object = object;
	}
	
	/**
	 * Create custom hashcode for managing hashMap
	 */
	 @Override
	 public int hashCode() {
		 return (predicate+":"+object).hashCode();
	 }
	 
	@Override
	public boolean equals(Object obj) {
		PredicateObject predicateObject = (PredicateObject) obj;
		return (predicateObject.getPredicate() == this.getPredicate() && predicateObject.getObject() == this.getObject());
	}
	
	public int getPredicate() {
		return predicate;
	}
	
	public int getObject() {
		return object;
	}

	public String toString() {
		return "Predicat : " + predicate + " Object : " + object;
	}
}
