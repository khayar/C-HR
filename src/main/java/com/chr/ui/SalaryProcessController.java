package com.chr.ui;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
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
import com.chr.entity.SystemHolidays;

@ManagedBean(name = "salaryProcessController")
@ViewScoped
public class SalaryProcessController implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(DbManager.class);

	private List<MasterDataEntity> masterEntityList = null;
	private List<SalaryProcessEntity> salaryProcessList = null;
	private SalaryProcessEntity selectedMasterEntity;
	MasterDataBusiness masterDataBussiness = new MasterDataBusiness();
	public static Date salaryMonth;
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

	public List<SalaryProcessEntity> getSalaryProcessList() {
		if (salaryProcessList == null)
			salaryProcessList = masterDataBussiness.getSalaryApprovalList();

		return salaryProcessList;
	}
	
	public List<SalaryProcessEntity> getSalaryReturnList() {
		if (salaryProcessList == null)
			salaryProcessList = masterDataBussiness.getSalaryReturnList();

		return salaryProcessList;
	}
	
	public void getSalaryApproved(List<SalaryProcessEntity> salaryList) {
		masterDataBussiness.getApprovedSalary(salaryList,"1");
	}

	public void getSalaryReturn(List<SalaryProcessEntity> salaryList) {
		masterDataBussiness.getApprovedSalary(salaryList,"0");
	}
	public void processSalary(List<MasterDataEntity> masterList) {
		logger.info("======================= Salary Processing Start =======================");

		processHolidaysOfMonth();

		for (MasterDataEntity masterEntity : masterList) {
			//Ot type variable then calculate OT hours and production incentives otherwise not
			if (masterEntity.getOtType().equals("Variable")) {
				
				getCalculateVariableOTRate(masterEntity);
				getTotalSumUpOTHoursWeekend(masterEntity);
				getTotalSumUpOTHoursWeekDays(masterEntity);
				getSumUpTotalOTHoursAndVariableOTRatesOfWeekendOrWeekdays(masterEntity);
				getTotalNoOfDaysWork(masterEntity);
				getTotalNoOfProductionIncentiveHours(masterEntity);
				persistSalary(masterEntity);

			} else {

				getTotalSumUpOTHoursWeekend(masterEntity);
				getTotalSumUpOTHoursWeekDays(masterEntity);
				getTotalNoOfDaysWork(masterEntity);
				//getTotalNoOfProductionIncentiveHours(masterEntity);
				persistSalaryForNonVariableOtType(masterEntity);

			}

		}
		logger.info("======================= Salary Processing End =======================");
		FacesMessage msg = new FacesMessage("Salary has been processed and sends for approval");
		FacesContext.getCurrentInstance().addMessage(null, msg);

	}

	public void processHolidaysOfMonth() {
		logger.info("================ Step 1 : Process holidays of the salary month [start]================");

		List<AttandenceRegisterEntity> attandenceList = new LinkedList<>();

		LocalDate date = salaryMonth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate beginningOfMonth = date.withDayOfMonth(1);
		LocalDate endOfMonth = date.plusMonths(1).withDayOfMonth(1).minusDays(1);

		List<SystemHolidays> systemHolidaysEntityList = masterDataBussiness.getTotalHolidaysOfMonth(beginningOfMonth,
				endOfMonth);

		if (systemHolidaysEntityList != null && !systemHolidaysEntityList.isEmpty()) {

			for (SystemHolidays systemHoliday : systemHolidaysEntityList) {

				Calendar c = Calendar.getInstance();
				c.setTime(systemHoliday.getStartDate());

				Calendar c1 = Calendar.getInstance();
				c1.setTime(systemHoliday.getEndDate());

				Instant date1 = c.getTime().toInstant();
				Instant date2 = c1.getTime().toInstant();

				LocalDate fromDate = date1.atZone(ZoneId.systemDefault()).toLocalDate();
				LocalDate toDate = date2.atZone(ZoneId.systemDefault()).toLocalDate();

				List<AttandenceRegisterEntity> attandenceRegisterList = masterDataBussiness
						.getAttandenceRegisterByDate(fromDate, toDate);

				for (AttandenceRegisterEntity attandenceEntity : attandenceRegisterList) {
					attandenceEntity.setIsWeekend(true);
					attandenceList.add(attandenceEntity);
				}

			}
			// Update Attandence Register
			for (AttandenceRegisterEntity attandenceEntity : attandenceList) {
				masterDataBussiness.updateAttandenceRegisterEntity(attandenceEntity);
			}
		}
		logger.info("================ Step 1 : Process holidays of the salary month [END]================");

	}

	public void getCalculateVariableOTRate(MasterDataEntity masterEntity) {
		logger.info("================ Step 2 : Calculate Variable OT Rate for Weekdays or Weekend================");

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
		Double otHoursWeekend = 0.0;
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
		Double otHoursWeekdays = 0.0;
		Double result = 0.0;

		for (AttandenceRegisterEntity attandenceEntity : attandenceRegisterList) {
			otHoursWeekdays = Double.valueOf(attandenceEntity.getTotalOThours());
			result += otHoursWeekdays;
		}
		masterEntity.salaryProcessEntity.setOverTimeWeekDays(String.valueOf(result));
		logger.info("================ Step 4 : Calculate Total sum up of OT hours by Weekend [end]================");
	}

	public void getSumUpTotalOTHoursAndVariableOTRatesOfWeekendOrWeekdays(MasterDataEntity masterEntity) {

		logger.info(
				"======= Step 5 : Calculate Total sum up of OT hours and Variable OT hours by Weekend [start]=======");

		String varOTratesWeekend = masterEntity.salaryProcessEntity.getVariableOtRateWeekend();
		String otRatesWeekend = masterEntity.salaryProcessEntity.getOverTimeWeekEnds();

		Double res_1 = Double.valueOf(varOTratesWeekend) * Double.valueOf(otRatesWeekend);

		String varOTratesWeekdays = masterEntity.salaryProcessEntity.getVariableOtRateWeekdays();
		String otRatesWeekdays = masterEntity.salaryProcessEntity.getOverTimeWeekDays();

		Double res_2 = Double.valueOf(varOTratesWeekdays) * Double.valueOf(otRatesWeekdays);

		Double res_3 = res_1 + res_2;

		masterEntity.salaryProcessEntity.setTotalOTHours(String.valueOf(res_3));
		logger.info(
				"================ Step 5 : Calculate Total sum up of OT hours and Variable OT hours by Weekend [start]================");
	}

	public void getSumUpWeekdaysAndWeekend(MasterDataEntity masterEntity) {

		logger.info("======= sum up weekdays and weekend of OT [start]=======");

		String varOTratesWeekdays = masterEntity.salaryProcessEntity.getVariableOtRateWeekdays();
		String otRates = masterEntity.salaryProcessEntity.getOverTimeWeekDays();

		Double res = Double.valueOf(varOTratesWeekdays) * Double.valueOf(otRates);

		masterEntity.salaryProcessEntity.setTotalOTHours(String.valueOf(res));
		logger.info("================ sum up weekdays and weekend of OT [end]================");
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

		List<AttandenceRegisterEntity> attandenceRegisterListIsWeekendTrue = masterDataBussiness
				.getAttandenceRegister(true, masterEntity.getEmployeeCode(), beginningOfMonth, endOfMonth);

		List<AttandenceRegisterEntity> attandenceRegisterListIsWeekendFalse = masterDataBussiness
				.getAttandenceRegister(false, masterEntity.getEmployeeCode(), beginningOfMonth, endOfMonth);

		Double otHoursWeekdays = 0.0;
		Double otHoursWeekdaysResult = 0.0;

		Double otHoursWeekend = 0.0;
		Double otHoursWeekendResult = 0.0;

		for (AttandenceRegisterEntity attandenceEntity : attandenceRegisterListIsWeekendTrue) {
			otHoursWeekend = Double.valueOf(attandenceEntity.getTotalOThours());
			otHoursWeekendResult += otHoursWeekend;
		}

		for (AttandenceRegisterEntity attandenceEntity : attandenceRegisterListIsWeekendFalse) {
			otHoursWeekdays = Double.valueOf(attandenceEntity.getTotalOThours());
			otHoursWeekdaysResult += otHoursWeekdays;
		}

		Double weekdaysResult = otHoursWeekdaysResult
				* Double.valueOf(masterEntity.salaryProcessEntity.getVariableOtRateWeekdays());
		Double weekendResult = otHoursWeekendResult
				* Double.valueOf(masterEntity.salaryProcessEntity.getVariableOtRateWeekend());
		Double sumWeekdaysWeekendResult = weekdaysResult + weekendResult;
		masterEntity.salaryProcessEntity.setProductionIncentives(String.valueOf(sumWeekdaysWeekendResult));

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
		salaryEntity.setProductionIncentives(masterEntity.salaryProcessEntity.getProductionIncentives());
		salaryEntity.setBasicSalary(masterEntity.getBasicSalary());
		salaryEntity.setOverTimeWeekDays(masterEntity.salaryProcessEntity.getOverTimeWeekDays());
		salaryEntity.setOverTimeWeekEnds(masterEntity.salaryProcessEntity.getOverTimeWeekEnds());
		salaryEntity.setTotalFixedSalary(masterEntity.getTotalFixedSalary());
		salaryEntity.setSalaryAsPerLabourContract(masterEntity.getSalaryAsPerLabourContract());
		salaryEntity.setSalaryProcessDate(new Date());
		salaryEntity.setSalaryProcessMonth(salaryMonth);
		salaryEntity.setVariableOtRateWeekdays(masterEntity.salaryProcessEntity.getVariableOtRateWeekdays());
		salaryEntity.setVariableOtRateWeekend(masterEntity.salaryProcessEntity.getVariableOtRateWeekend());
		salaryEntity.setCreatedBy(currentUser.getPrincipal().toString());
		salaryEntity.setCreatedOn(new Date());
		salaryEntity.setCurrentState("Approval Required");

		Double totalSalary = Double.valueOf(masterEntity.getBasicSalary())
				+ Double.valueOf(masterEntity.getAllowances())
				+ Double.valueOf(masterEntity.salaryProcessEntity.getProductionIncentives())
				+ Double.valueOf(masterEntity.salaryProcessEntity.getTotalOTHours());
		Double netSalary = Double.valueOf(masterEntity.getBasicSalary()) + Double.valueOf(masterEntity.getAllowances())
				+ Double.valueOf(masterEntity.salaryProcessEntity.getProductionIncentives())
				+ Double.valueOf(masterEntity.salaryProcessEntity.getTotalOTHours())
				- Double.valueOf(masterEntity.getLoan());

		Double diffInSalary = Double.valueOf(masterEntity.getSalaryAsPerLabourContract()) - netSalary;

		Double amoutCredit = netSalary > Double.valueOf(masterEntity.getSalaryAsPerLabourContract()) ? netSalary
				: Double.valueOf(masterEntity.getSalaryAsPerLabourContract());

		salaryEntity.setTotalSalary(String.valueOf(totalSalary));
		salaryEntity.setNetSalary(String.valueOf(netSalary));
		salaryEntity.setDifferenceInSalary(String.valueOf(diffInSalary));
		salaryEntity.setAmountToBeCredit(String.valueOf(amoutCredit));

		masterDataBussiness.addSalaryEntity(salaryEntity);
	}
	
	
	public void persistSalaryForNonVariableOtType(MasterDataEntity masterEntity) {
		SalaryProcessEntity salaryEntity = new SalaryProcessEntity();
		LocalDate Local = salaryMonth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		BigDecimal diffInSalary= new BigDecimal("0");
		BigDecimal amoutCredit=new BigDecimal("0");
		Subject currentUser = SecurityUtils.getSubject();
		currentUser.getPrincipal();

		salaryEntity.setEmployeeCode(masterEntity.getEmployeeCode());
		salaryEntity.setNoOfDaysWork(masterEntity.salaryProcessEntity.getNoOfDaysWork());
		salaryEntity.setTotalOTHours(masterEntity.salaryProcessEntity.getTotalOTHours());
		salaryEntity.setProductionIncentives("0");
		salaryEntity.setBasicSalary(masterEntity.getBasicSalary());
		salaryEntity.setOverTimeWeekDays(masterEntity.salaryProcessEntity.getOverTimeWeekDays());
		salaryEntity.setOverTimeWeekEnds(masterEntity.salaryProcessEntity.getOverTimeWeekEnds());
		salaryEntity.setTotalFixedSalary(masterEntity.getTotalFixedSalary());
		salaryEntity.setSalaryAsPerLabourContract(masterEntity.getSalaryAsPerLabourContract());
		salaryEntity.setSalaryProcessDate(new Date());
		salaryEntity.setSalaryProcessMonth(salaryMonth);
		salaryEntity.setVariableOtRateWeekdays("0");
		salaryEntity.setVariableOtRateWeekend("0");
		salaryEntity.setCreatedBy(currentUser.getPrincipal().toString());
		salaryEntity.setCreatedOn(new Date());
		salaryEntity.setCurrentState("Approval Required");

		BigDecimal totalSalary = new BigDecimal(masterEntity.getBasicSalary()).add(new BigDecimal(masterEntity.getAllowances()));
		BigDecimal netSalary = new BigDecimal(masterEntity.getBasicSalary()).add(new BigDecimal(masterEntity.getAllowances()));
		
		if(!masterEntity.getLoan().equals(""))
			netSalary = netSalary.subtract(new BigDecimal(masterEntity.getLoan()));
		
		if(!masterEntity.getSalaryAsPerLabourContract().equals("")) {
			 diffInSalary = new BigDecimal(masterEntity.getSalaryAsPerLabourContract()).subtract(netSalary);
			 
			 if(netSalary.compareTo(new BigDecimal(masterEntity.getSalaryAsPerLabourContract())) > 0 ){
				 amoutCredit = netSalary; 
			 }
			 else{
				 amoutCredit = new BigDecimal(masterEntity.getSalaryAsPerLabourContract());
			 }
		}
		else {
			amoutCredit = netSalary;
		}
//		BigDecimal amoutCredit = netSalary > Double.valueOf(masterEntity.getSalaryAsPerLabourContract()) ? netSalary
//				: Double.valueOf(masterEntity.getSalaryAsPerLabourContract());

		salaryEntity.setTotalSalary(String.valueOf(totalSalary));
		salaryEntity.setNetSalary(String.valueOf(netSalary));
		salaryEntity.setDifferenceInSalary(String.valueOf(diffInSalary));
		salaryEntity.setAmountToBeCredit(String.valueOf(amoutCredit));

		masterDataBussiness.addSalaryEntity(salaryEntity);
	}


	public Date getSalaryMonth() {
		return salaryMonth;
	}

	public void setSalaryMonth(Date salaryMonth) {
		this.salaryMonth = salaryMonth;
	}

	public void setSalaryProcessList(List<SalaryProcessEntity> salaryProcessList) {
		this.salaryProcessList = salaryProcessList;
	}

}
