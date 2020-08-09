package com.chr.business;

import java.util.List;

import com.chr.data.DbManager;
import com.chr.entity.AttandenceRegisterEntity;
import com.chr.entity.MasterDataEntity;

public class MasterDataBusiness {

	private DbManager dbManager = new DbManager();

	public List<MasterDataEntity> getMasterDataList() {
		return dbManager.getEntityList("MasterDataEntity");
	}
	
	public void saveMasterDataDetail(MasterDataEntity masterDataEntity){
		dbManager.addEntity(masterDataEntity);
	}
	
	public void editMasterDataDetail(MasterDataEntity masterDataEntity){
		dbManager.updateEntity(masterDataEntity);
	}
	
	public MasterDataEntity getMasterDataById(String id){
		MasterDataEntity masterEntity = dbManager.getMasterDataById(id);
		return masterEntity;
	}
	
	public AttandenceRegisterEntity getAttandenceRegisterById(String id){
		AttandenceRegisterEntity attandencEntity = dbManager.getAttandenceRegisterById(id);
		return attandencEntity;
	}
	
	public List<AttandenceRegisterEntity> getAttandenceRegisterList() {
		return dbManager.getAttandenceRegisterList();
	}

	public void saveAttandenceRegisterDetail(AttandenceRegisterEntity attandenceRegisterEntity){
		dbManager.addEntity(attandenceRegisterEntity);
	}
	
	public String getEmployeeName(String empCode){
		MasterDataEntity masterEntity = dbManager.getMasterDataByEmployeeCode(empCode);
		return masterEntity.getEmployeeName();
	}
	
}
