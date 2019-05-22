package ar.edu.itba.paw.persistence;

import java.util.Optional;

public class Filter {
	
	private String name;
	private final Optional<?> value;
	
	public Filter(final String name, final Optional<?> value) {
		this.name = name;
		this.value = value;
	}
	
	public String queryAsString() {
		return " " + name.toString() + " LIKE '%' || LOWER(?) || '%' ";
	}
	
	public String queryAsInteger() {
		return " " + name.toString() + " = ? ";
	}
	
	public String queryAsGreaterInteger(boolean alsoEquals) {
		return " " + name.toString() + " >" + ((alsoEquals) ? "= ?" : " ?");
	}
	
	public String queryAsDateRange() {
		return " CAST( " + name.toString() + " AS DATE) = CAST( ? AS DATE) ";
	}
	
	public String getName() {
		return name;
	}
	
	public Optional<?> getValue() {
		return value;
	}

}
