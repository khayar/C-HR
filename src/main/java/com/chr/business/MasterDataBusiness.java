package com.chr.business;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.chr.data.DbManager;
import com.chr.entity.AttandenceRegisterEntity;
import com.chr.entity.MasterDataEntity;
import com.chr.entity.SalaryProcessEntity;
import com.chr.entity.SystemHolidays;

public class MasterDataBusiness {

	private DbManager dbManager = new DbManager();

	public List<MasterDataEntity> getMasterDataList() {
		return dbManager.getEntityList("MasterDataEntity");
	}

	public List<MasterDataEntity> getMasterList() {
		return dbManager.getMasterDataList();
	}

	public void saveMasterDataDetail(MasterDataEntity masterDataEntity) {
		dbManager.addEntity(masterDataEntity);
	}

	public void editMasterDataDetail(MasterDataEntity masterDataEntity) {
		dbManager.updateEntity(masterDataEntity);
	}

	public MasterDataEntity getMasterDataById(String id) {
		MasterDataEntity masterEntity = dbManager.getMasterDataById(id);
		return masterEntity;
	}

	public AttandenceRegisterEntity getAttandenceRegisterById(String id) {
		AttandenceRegisterEntity attandencEntity = dbManager.getAttandenceRegisterById(id);
		return attandencEntity;
	}

	public List<AttandenceRegisterEntity> getAttandenceRegisterByDate(LocalDate fromDate, LocalDate toDate) {
		List<AttandenceRegisterEntity> attandencEntityList = dbManager.getAttandenceRegisterByDate(fromDate, toDate);
		return attandencEntityList;
	}

	public SystemHolidays getSystemHolidayById(String id) {
		SystemHolidays systemholidayEntity = dbManager.getSystemHolidayById(id);
		return systemholidayEntity;
	}

	public List<AttandenceRegisterEntity> getAttandenceRegisterList() {
		return dbManager.getAttandenceRegisterList();
	}

	public List<SystemHolidays> getSystemHolidaysList() {
		return dbManager.getSystemHolidaysList();
	}

	public void saveAttandenceRegisterDetail(AttandenceRegisterEntity attandenceRegisterEntity) {
		dbManager.addEntity(attandenceRegisterEntity);
	}

	public void saveSystemHolidays(SystemHolidays systemHolidaysEntity) {
		dbManager.addEntity(systemHolidaysEntity);
	}

	public void editAttandenceRegister(AttandenceRegisterEntity attandenceRegisterEntity) {
		dbManager.updateEntity(attandenceRegisterEntity);
	}

	public void editSystemHolidays(SystemHolidays systemHokidayEntity) {
		dbManager.updateEntity(systemHokidayEntity);
	}

	public String getEmployeeName(String empCode) {
		MasterDataEntity masterEntity = dbManager.getMasterDataByEmployeeCode(empCode);
		return masterEntity.getEmployeeName();
	}

	public List<AttandenceRegisterEntity> getAttandenceRegister(Boolean isWeekend, String empCode, LocalDate fromDate,
			LocalDate endDate) {
		return dbManager.getCountOfVariableOTRateForWeekendWeekdays(isWeekend, empCode, fromDate, endDate);
	}

	public List<AttandenceRegisterEntity> getTotalNoOfDaysWork(String empCode, LocalDate fromDate, LocalDate endDate) {
		return dbManager.getTotalNoOfDaysWork(empCode, fromDate, endDate);
	}

	public List<SystemHolidays> getTotalHolidaysOfMonth(LocalDate fromDate, LocalDate endDate) {
		return dbManager.getTotalHolidaysOfMonth(fromDate, endDate);
	}

	public Object getTotalNoOfProductionIncentiveHours(String empCode, LocalDate fromDate, LocalDate endDate) {
		return dbManager.getTotalNoOfProductionIncentiveHours(empCode, fromDate, endDate);
	}

	public void getApprovedSalary(List<SalaryProcessEntity> salaryProcessList, String flag) {
		dbManager.getSalaryApproved(salaryProcessList, flag);
	}

	public void updateAttandenceRegisterEntity(AttandenceRegisterEntity attandenceRegisterEntity) {
		dbManager.updateAttandenceRegisterEntity(attandenceRegisterEntity);
	}

	public List<SalaryProcessEntity> getSalaryApprovalList() {
		return dbManager.getSalaryApprovalList();
	}

	public List<SalaryProcessEntity> getSalaryReturnList() {
		return dbManager.getSalaryReturnList();
	}

	public void addSalaryEntity(SalaryProcessEntity salaryEntity) {
		dbManager.addSalaryEntity(salaryEntity);
	}
}
