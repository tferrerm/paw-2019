package ar.edu.itba.paw.persistence;

public class Filter {
	
	private String name;
	private final Object value;
	
	public Filter(final String name, final Object value) {
		this.name = name;
		this.value = value;
	}
	
	public String queryAsString() {
		return " " + name.toString() + " LIKE '%' || ? || '%' ";
	}
	
	public String queryAsInteger() {
		return " " + name.toString() + " = ? ";
	}
	
	public String queryAsGreaterInteger(boolean alsoEquals) {
		return " " + name.toString() + " >" + ((alsoEquals) ? "= ?" : " ?");
	}
	
	public String getName() {
		return name;
	}
	
	public Object getValue() {
		return value;
	}

}
