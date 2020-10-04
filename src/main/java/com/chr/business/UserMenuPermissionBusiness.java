package com.chr.business;

import java.util.List;

import com.chr.data.DbManager;
import com.chr.entity.MasterDataEntity;
import com.chr.entity.UserMenuPermission;

public class UserMenuPermissionBusiness {

	private DbManager dbManager = new DbManager();

	public List<UserMenuPermission> getUserMenuPermissionList(String userId) {
		return dbManager.getUserMenuPermissionList(userId);
	}


}
