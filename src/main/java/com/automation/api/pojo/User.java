package com.automation.api.pojo;

/**
 * 
 * @author Govi
 *
 */
public class User {

	private String uuid;
	private String email;
	private String firstName;
	private String lastName;
	private boolean newsletter;
	private String createdAt;
	private String lastModifiedAt;

	public User() {
		super();
	}

	public User(String firstName, String lastName, String email, boolean newsletter) {
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.newsletter = newsletter;
	}

	/**
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the newsletter
	 */
	public boolean isNewsletter() {
		return newsletter;
	}

	/**
	 * @param newsletter the newsletter to set
	 */
	public void setNewsletter(boolean newsletter) {
		this.newsletter = newsletter;
	}

	/**
	 * @return the createdAt
	 */
	public String getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @return the lastModifiedAt
	 */
	public String getLastModifiedAt() {
		return lastModifiedAt;
	}

	/**
	 * @param lastModifiedAt the lastModifiedAt to set
	 */
	public void setLastModifiedAt(String lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	@Override
	public String toString() {
		return "User [uuid=" + uuid + ", email=" + email + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", newsletter=" + newsletter + ", createdAt=" + createdAt + ", lastModifiedAt=" + lastModifiedAt
				+ "]";
	}

}
