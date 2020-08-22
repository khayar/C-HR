package com.chr.ui;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import com.chr.business.MasterDataBusiness;
import com.chr.data.DbManager;
import com.chr.entity.AttandenceRegisterEntity;
import com.chr.entity.MasterDataEntity;
import com.chr.entity.SalaryProcessEntity;

@ManagedBean(name = "salaryProcessController")
@ViewScoped
public class SalaryProcessController implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(DbManager.class);

	private List<MasterDataEntity> masterEntityList = null;
	private SalaryProcessEntity selectedMasterEntity;
	MasterDataBusiness masterDataBussiness = new MasterDataBusiness();
	private Date salaryMonth;

	public SalaryProcessController() {
		super();
	}

	@PostConstruct
	public void init() {
		// In @PostConstruct (will be invoked immediately after construction and
		// dependency/property injection).
		selectedMasterEntity = new SalaryProcessEntity();

	}

	public List<MasterDataEntity> getMasterDataList() {
		if (masterEntityList == null) {
			masterEntityList = masterDataBussiness.getMasterDataList();
			for (int i = 0; i < masterEntityList.size(); i++) {
				masterEntityList.get(i).salaryProcessEntity = new SalaryProcessEntity();
			}
		}
		return masterEntityList;
	}

	public List<MasterDataEntity> getMasterList() {
		if (masterEntityList == null) {
			masterEntityList = masterDataBussiness.getMasterList();
		}
		return masterEntityList;
	}

	public void processSalary(List<MasterDataEntity> masterList) {
		logger.info("======================= Salary Processing Start =======================");

		for (MasterDataEntity masterEntity : masterList) {
			getCalculateVariableOTRate(masterEntity);
		}

		FacesMessage msg = new FacesMessage("Salary has been processed and sends for approval");
		FacesContext.getCurrentInstance().addMessage(null, msg);

	}

	public void getCalculateVariableOTRate(MasterDataEntity masterEntity) {
		logger.info("================ Step 1 : Calculate Variable OT Rate for Weekdays or Weekend================");

		if (masterEntity.getOtType().equals("Variable")) {

			getCountOfVariableWeekDays(masterEntity);
			getCountOfVariableWeekend(masterEntity);
			getTotalSumUpOTHoursWeekend(masterEntity);
			getTotalSumUpOTHoursWeekDays(masterEntity);

		} else if (masterEntity.getOtType().equals("Fixed")) {

		}
	}

	public void getCountOfVariableWeekDays(MasterDataEntity masterEntity) {
		// weekdays formula : Weekdays (Total salary/Total no hours in
		// month)*1.25

		logger.info("================ Step 2 : Calculate Variable OT Rate for Weekdays [start]================");

		LocalDate date = salaryMonth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		Integer standardHours = Integer.valueOf(JsfUtil.getResourceInstance("STANDARD_HOURS"));
		Double weekDaysFormulaRate = Double.valueOf(JsfUtil.getResourceInstance("WEEKDAYS_FORMULA_RATE"));

		Integer lastDateOfMonth = date.lengthOfMonth();
		String totalSalary = masterEntity.getTotalFixedSalary();

		Integer res = standardHours * lastDateOfMonth;
		Integer res2 = Integer.valueOf(totalSalary) / res;
		Double res3 = res2 * weekDaysFormulaRate;
		masterEntity.salaryProcessEntity.setVariableOtRateWeekdays(String.valueOf(res3));

		logger.info("================ Step 2 : Calculate Variable OT Rate for Weekdays [end]================");

	}

	public void getCountOfVariableWeekend(MasterDataEntity masterEntity) {

		logger.info("================ Step 3 : Calculate Variable OT Rate for Weekend [start]================");

		LocalDate date = salaryMonth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		Integer standardHours = Integer.valueOf(JsfUtil.getResourceInstance("STANDARD_HOURS"));
		Double weekendFormulaRate = Double.valueOf(JsfUtil.getResourceInstance("WEEKEND_FORMULA_RATE"));

		Integer lastDateOfMonth = date.lengthOfMonth();
		String totalSalary = masterEntity.getTotalFixedSalary();

		Integer res = standardHours * lastDateOfMonth;
		Integer res2 = Integer.valueOf(totalSalary) / res;
		Double res3 = res2 * weekendFormulaRate;
		masterEntity.salaryProcessEntity.setVariableOtRateWeekend(String.valueOf(res3));

		logger.info("================ Step 3 : Calculate Variable OT Rate for Weekend [end]================");

	}

	public void getTotalSumUpOTHoursWeekend(MasterDataEntity masterEntity) {

		logger.info("================ Step 4 : Calculate Total sum up of OT hours by Weekend [start]================");

		Boolean isWeekend = true;
		LocalDate date = salaryMonth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate beginningOfMonth = date.withDayOfMonth(1);
		LocalDate endOfMonth = date.plusMonths(1).withDayOfMonth(1).minusDays(1);
		Double otHours = null;
		List<AttandenceRegisterEntity> attandenceRegisterList = masterDataBussiness.getAttandenceRegister(isWeekend,
				masterEntity.getEmployeeCode(), beginningOfMonth, endOfMonth);
		for (AttandenceRegisterEntity attandenceEntity : attandenceRegisterList) {
			otHours = Double.valueOf(attandenceEntity.getTotalOThours());
			otHours += otHours;
		}
		System.out.println("Sum of OT hours = " + otHours);

		logger.info("================ Step 4 : Calculate Total sum up of OT hours by Weekend [end]================");
	}

	public void getTotalSumUpOTHoursWeekDays(MasterDataEntity masterEntity) {

		logger.info("================ Step 4 : Calculate Total sum up of OT hours by Weekdays [start]================");

		Boolean isWeekend = false;
		LocalDate date = salaryMonth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate beginningOfMonth = date.withDayOfMonth(1);
		LocalDate endOfMonth = date.plusMonths(1).withDayOfMonth(1).minusDays(1);
		List<AttandenceRegisterEntity> attandenceRegisterList = masterDataBussiness.getAttandenceRegister(isWeekend,
				masterEntity.getEmployeeCode(), beginningOfMonth, endOfMonth);
		Double otHours = null;
		Double result = 0.0;
		for (AttandenceRegisterEntity attandenceEntity : attandenceRegisterList) {
			otHours = Double.valueOf(attandenceEntity.getTotalOThours());
			result += otHours;
		}
		System.out.println("Sum of OT hours = " + result);

		logger.info("================ Step 4 : Calculate Total sum up of OT hours by Weekend [end]================");
	}

	public Date getSalaryMonth() {
		return salaryMonth;
	}

	public void setSalaryMonth(Date salaryMonth) {
		this.salaryMonth = salaryMonth;
	}

}
