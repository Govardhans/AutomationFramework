package com.automation.api.response.data;

import com.automation.api.request.data.QueryVariables;
import com.automation.model.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <p>
 * <code>ModifyUserResponse</code> class is placeholder for <br>
 * User manipulation query response.<br>
 * This class supports modifyUser and createUser response
 * </p>
 * 
 * @author Govi
 *
 */
public class UserResponse {

	@SerializedName("data")
	@Expose
	private CreatedUserData data;

	public CreatedUserData getData() {
		return data;
	}

	public void setData(CreatedUserData data) {
		this.data = data;
	}

	/**
	 * Inner class of <code>ModifyUserResponse</code>
	 * 
	 * @author Govi
	 *
	 */
	public class CreatedUserData implements QueryVariables {

		@SerializedName(value = "createUser", alternate = { "updateUser", "deleteUser" })
		@Expose
		private ResponseUser updateUser;

		public ResponseUser getResponseUser() {
			return updateUser;
		}

		public void setResponseUser(ResponseUser updateUser) {
			this.updateUser = updateUser;
		}

		/**
		 * Inner class of <code>ModifyUserResponse</code>
		 * 
		 * @author Govi
		 *
		 */
		public class ResponseUser {

			@SerializedName("ok")
			@Expose
			private Boolean ok;

			@SerializedName("error")
			@Expose
			private String error;

			@SerializedName("user")
			@Expose
			private User user;

			public Boolean getOk() {
				return ok;
			}

			public void setOk(Boolean ok) {
				this.ok = ok;
			}

			public String getError() {
				return error;
			}

			public void setError(String error) {
				this.error = error;
			}

			public User getUser() {
				return user;
			}

			public void setUser(User user) {
				this.user = user;
			}

			@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
//				result = prime * result + getEnclosingInstance().hashCode();
				result = prime * result + ((error == null) ? 0 : error.hashCode());
				result = prime * result + ((ok == null) ? 0 : ok.hashCode());
				result = prime * result + ((user == null) ? 0 : user.hashCode());
				return result;
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj)
					return true;
				if (obj == null)
					return false;
				if (getClass() != obj.getClass())
					return false;
				ResponseUser other = (ResponseUser) obj;
//				if (!getEnclosingInstance().equals(other.getEnclosingInstance())) {
//					return false;
//				}
				if (error == null) {
					if (other.error != null) {						
						return false;
					}
				} else if (!error.equals(other.error)) {
					return false;
				}
				if (ok == null) {
					if (other.ok != null)
						return false;
				} else if (!ok.equals(other.ok)) {
					return false;
				}
//				if (user == null) {
//					if (other.user != null)
//						return false;
//				} else if (!user.equals(other.user))
//					return false;
				return true;
			}

			private CreatedUserData getEnclosingInstance() {
				return CreatedUserData.this;
			}

		}

	}

}
