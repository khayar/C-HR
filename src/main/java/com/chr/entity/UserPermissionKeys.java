package com.chr.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserPermissionKeys implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "PERMISSION_ID")
	private String permissionId;

	@Column(name = "USER_ID")
	private String userID;

	public UserPermissionKeys() {
		super();
	}

	public String getPermissionId() {
		return permissionId;
	}

	public String getUserID() {
		return userID;
	}

	public void setPermissionId(String permissionId) {
		this.permissionId = permissionId;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((permissionId == null) ? 0 : permissionId.hashCode());
		result = prime * result + ((userID == null) ? 0 : userID.hashCode());
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
		UserPermissionKeys other = (UserPermissionKeys) obj;
		if (permissionId == null) {
			if (other.permissionId != null)
				return false;
		} else if (!permissionId.equals(other.permissionId))
			return false;
		if (userID == null) {
			if (other.userID != null)
				return false;
		} else if (!userID.equals(other.userID))
			return false;
		return true;
	}
	
	
}
