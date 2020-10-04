package com.chr.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "USER_MENU_PERMISSION")
public class UserMenuPermission implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private UserPermissionKeys userPermissionKey;

	@Column(name = "PERMISSION_NAME")
	private String permissionName;

	@Column(name = "PERMISSION_URL")
	private String permissionURL;
	
	@Column(name = "SORT_ID")
	private String sortId;

	
	public UserMenuPermission() {
		super();
	}

	public UserPermissionKeys getUserPermissionKey() {
		return userPermissionKey;
	}

	public String getPermissionName() {
		return permissionName;
	}

	public String getPermissionURL() {
		return permissionURL;
	}

	public void setUserPermissionKey(UserPermissionKeys userPermissionKey) {
		this.userPermissionKey = userPermissionKey;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	public void setPermissionURL(String permissionURL) {
		this.permissionURL = permissionURL;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getSortId() {
		return sortId;
	}

	public void setSortId(String sortId) {
		this.sortId = sortId;
	}
	
	
}
