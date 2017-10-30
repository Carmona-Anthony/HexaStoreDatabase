
public class PredicateObject {
	
	int[] predicateObject;
	
	PredicateObject(int predicate, int object){
		predicateObject = new int[2];
		predicateObject[0] = predicate;
		predicateObject[1] = object;
	}
	
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

}
