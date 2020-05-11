package com.automation.api.request.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <p>
 * <code>QueryPayload</code> class created QueryPayload that includes 
 * query and variables
 * </p>
 * @author Govi
 *
 */
public class QueryPayload {

	@SerializedName("query")
	@Expose
	private String query;
	
	
	@SerializedName("variables")	
	@Expose
	private QueryVariables variables;

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public QueryVariables getVariables() {
		return variables;
	}

	public void setVariables(QueryVariables variables) {
		this.variables = variables;
	}

}
