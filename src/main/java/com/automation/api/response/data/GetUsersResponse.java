package com.automation.api.response.data;

import java.util.List;

import com.automation.model.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <p>
 * <code>GetUsersResponse</code> class is placeholder for 
 * capturing GET USERS response
 * </p>
 * <p> this class holds Users List
 * </p>
 * @author Govi
 *
 */
public class GetUsersResponse {

	@SerializedName("data")
	@Expose
	private GetUsersData data;

	public GetUsersData getData() {
		return data;
	}

	public void setData(GetUsersData data) {
		this.data = data;
	}
	
	/**
	 * Inner class of <code>GetUsersResponse</code>	 * 
	 * it hold list of users
	 * @author Govi
	 *
	 */
	public class GetUsersData {

		@SerializedName("users")
		@Expose
		private List<User> users = null;

		public List<User> getUsers() {
			return users;
		}

		public void setUsers(List<User> users) {
			this.users = users;
		}

	}

}


