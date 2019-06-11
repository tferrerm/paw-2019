package ar.edu.itba.paw.persistence;

import java.util.Optional;

public class Filter {
	
	private String name;
	private final Optional<?> value;
	
	public Filter(final String name, final Optional<?> value) {
		this.name = name;
		this.value = value;
	}
	
	public String queryAsString(int paramNum) {
		return " LOWER(" + name.toString() + ") LIKE '%' || LOWER(:" + getParamName() + paramNum + ") || '%' ";
	}
	
	public String queryAsInteger() {
		return " " + name.toString() + " = ? ";
	}
	
	/*public String queryAsGreaterInteger(boolean alsoEquals) {
		return " " + name.toString() + " >" + ((alsoEquals) ? "= ?" : " ?");
	}*/
	
	public String queryAsDateRange(int paramNum, boolean onlyAtDate) {
		return " CAST( " + name.toString() + " AS DATE) " + ((onlyAtDate)? "" : ">") + 
				"= CAST(:" + getParamName() + paramNum + " AS DATE) ";
	}
	
	public String getName() {
		return name;
	}
	
	public Optional<?> getValue() {
		return value;
	}
	
	public static String getParamName() {
		return "param_";
	}

}
