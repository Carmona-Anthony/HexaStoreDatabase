package Models;

/**
 * Custom Class that represent a statement < S P O >
 * @author Proprietaire
 *
 */
public class CustomStatement {

	String subject;
	String predicate;
	String object;
	
	public CustomStatement(String subject, String predicate, String object) {
		this.subject = subject;
		this.predicate = predicate;
		this.object = object;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getPredicate() {
		return predicate;
	}
	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}
	
	public String toString() {
		return this.predicate + " " + this.object;
	}
	
}
