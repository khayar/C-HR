package com.chr.ui;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

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
	HashMap<String, String> empOTHours = new HashMap<>();

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
			if (masterEntity.getOtType().equals("Variable")) {

				getCalculateVariableOTRate(masterEntity);
				getTotalSumUpOTHoursWeekend(masterEntity);
				getTotalSumUpOTHoursWeekDays(masterEntity);
				getSumUpTotalOTHoursAndVariableOTRatesOfWeekend(masterEntity);
				getTotalNoOfDaysWork(masterEntity);
				getTotalNoOfProductionIncentiveHours(masterEntity);
				persistSalary(masterEntity);

			} else {

				getTotalSumUpOTHoursWeekend(masterEntity);
				getTotalSumUpOTHoursWeekDays(masterEntity);
				getSumUpTotalOTHoursAndVariableOTRatesOfWeekend(masterEntity);
				getTotalNoOfDaysWork(masterEntity);
				getTotalNoOfProductionIncentiveHours(masterEntity);
				persistSalary(masterEntity);

			}

		}
		logger.info("======================= Salary Processing End =======================");
		FacesMessage msg = new FacesMessage("Salary has been processed and sends for approval");
		FacesContext.getCurrentInstance().addMessage(null, msg);

	}

	public void getCalculateVariableOTRate(MasterDataEntity masterEntity) {
		logger.info("================ Step 1 : Calculate Variable OT Rate for Weekdays or Weekend================");

		getCountOfVariableWeekDays(masterEntity);
		getCountOfVariableWeekend(masterEntity);

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
		Double otHoursWeekend = null;
		Double result = 0.0;
		List<AttandenceRegisterEntity> attandenceRegisterList = masterDataBussiness.getAttandenceRegister(isWeekend,
				masterEntity.getEmployeeCode(), beginningOfMonth, endOfMonth);

		for (AttandenceRegisterEntity attandenceEntity : attandenceRegisterList) {
			otHoursWeekend = Double.valueOf(attandenceEntity.getTotalOThours());
			result += otHoursWeekend;
		}
		masterEntity.salaryProcessEntity.setOverTimeWeekEnds(String.valueOf(result));

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
		Double otHoursWeekdays = null;
		Double result = 0.0;

		for (AttandenceRegisterEntity attandenceEntity : attandenceRegisterList) {
			otHoursWeekdays = Double.valueOf(attandenceEntity.getTotalOThours());
			result += otHoursWeekdays;
		}
		masterEntity.salaryProcessEntity.setOverTimeWeekDays(String.valueOf(result));
		logger.info("================ Step 4 : Calculate Total sum up of OT hours by Weekend [end]================");
	}

	public void getSumUpTotalOTHoursAndVariableOTRatesOfWeekend(MasterDataEntity masterEntity) {

		logger.info(
				"================ Step 5 : Calculate Total sum up of OT hours and Variable OT hours by Weekend [start]================");

		String varOTratesWeekdays = masterEntity.salaryProcessEntity.getVariableOtRateWeekdays();
		String otRates = empOTHours.get(masterEntity.getEmployeeCode());
		String res = varOTratesWeekdays + otRates;

		masterEntity.salaryProcessEntity.setTotalOTHours(res);
		logger.info(
				"================ Step 5 : Calculate Total sum up of OT hours and Variable OT hours by Weekend [start]================");
	}

	public void getTotalNoOfDaysWork(MasterDataEntity masterEntity) {

		logger.info("================ Calculate Total No of days Work [start]================");

		LocalDate date = salaryMonth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate beginningOfMonth = date.withDayOfMonth(1);
		LocalDate endOfMonth = date.plusMonths(1).withDayOfMonth(1).minusDays(1);

		Integer noOfDaysWork = masterDataBussiness
				.getTotalNoOfDaysWork(masterEntity.getEmployeeCode(), beginningOfMonth, endOfMonth).size();
		masterEntity.salaryProcessEntity.setNoOfDaysWork(String.valueOf(noOfDaysWork));

		logger.info("================ Calculate Total No of days Work [end]================");
	}

	public void getTotalNoOfProductionIncentiveHours(MasterDataEntity masterEntity) {

		logger.info("================ Calculate Total No of Production incentive hours [start]================");

		LocalDate date = salaryMonth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate beginningOfMonth = date.withDayOfMonth(1);
		LocalDate endOfMonth = date.plusMonths(1).withDayOfMonth(1).minusDays(1);

		Integer productionHours = masterDataBussiness
				.getTotalNoOfProductionIncentiveHours(masterEntity.getEmployeeCode(), beginningOfMonth, endOfMonth);
		masterEntity.salaryProcessEntity.setProductionIncentiveHours(String.valueOf(productionHours));

		logger.info("================ Calculate Total No of Production incentive hours [end]================");
	}

	public void persistSalary(MasterDataEntity masterEntity) {
		SalaryProcessEntity salaryEntity = new SalaryProcessEntity();
		LocalDate Local = salaryMonth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		Subject currentUser = SecurityUtils.getSubject();
		currentUser.getPrincipal();

		salaryEntity.setEmployeeCode(masterEntity.getEmployeeCode());
		salaryEntity.setNoOfDaysWork(masterEntity.salaryProcessEntity.getNoOfDaysWork());
		salaryEntity.setTotalOTHours(masterEntity.salaryProcessEntity.getTotalOTHours());
		salaryEntity.setProductionIncentiveHours(masterEntity.salaryProcessEntity.getProductionIncentiveHours());
		salaryEntity.setBasicSalary(masterEntity.getBasicSalary());
		salaryEntity.setOverTimeWeekDays(masterEntity.salaryProcessEntity.getOverTimeWeekDays());
		salaryEntity.setOverTimeWeekEnds(masterEntity.salaryProcessEntity.getOverTimeWeekEnds());
		salaryEntity.setTotalFixedSalary(masterEntity.getTotalFixedSalary());
		salaryEntity.setSalaryAsPerLabourContract(masterEntity.getSalaryAsPerLabourContract());
		salaryEntity.setSalaryProcessDate(new Date());
		salaryEntity.setSalaryProcessMonth(Local.getMonth().name());
		salaryEntity.setVariableOtRateWeekdays(masterEntity.salaryProcessEntity.getVariableOtRateWeekdays());
		salaryEntity.setVariableOtRateWeekend(masterEntity.salaryProcessEntity.getVariableOtRateWeekend());
		salaryEntity.setCreatedBy(currentUser.getPrincipal().toString());
		salaryEntity.setCreatedOn(new Date());

		masterDataBussiness.saveSalaryEntity(salaryEntity);
	}

	public Date getSalaryMonth() {
		return salaryMonth;
	}

	public void setSalaryMonth(Date salaryMonth) {
		this.salaryMonth = salaryMonth;
	}

}
