package ar.edu.itba.paw.persistence;

public class Filter {
	
	private final String name;
	private final String value;
	
	public Filter(final String name, final String value) {
		this.name = name;
		this.value = value;
	}
	
	public String queryAsString() {
		return " " + name.toString() + " LIKE '%' || ? || '%' ";
	}
	
	public String queryAsInteger() {
		return " " + name.toString() + " = ? ";
	}
	
	public String getName() {
		return name;
	}
	
	public String getValue() {
		return value;
	}

}
