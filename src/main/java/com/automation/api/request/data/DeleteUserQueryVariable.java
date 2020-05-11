package com.automation.api.request.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**<p>
 * <code>DeleteUserQueryVariable</code> is used to create delete user payload
 * </p>
 * @author Govi
 *
 */

public class DeleteUserQueryVariable implements QueryVariables {

	@SerializedName("uuid")
	@Expose
	private String uuid;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}