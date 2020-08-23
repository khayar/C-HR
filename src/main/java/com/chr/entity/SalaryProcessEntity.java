package com.chr.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SALARY_PROCESS")
public class SalaryProcessEntity {

	@Id
	@Column(name = "EMPLOYEE_CODE", unique = true, nullable = false)
	private String employeeCode;

	@Column(name = "NO_OF_DAYS_WORK")
	private String noOfDaysWork;

	@Column(name = "TOTAL_OT_HOURS")
	private String totalOTHours;

	@Column(name = "PRODUCTION_INCENTIVE_HOURS")
	private String productionIncentiveHours;

	@Column(name = "FIXED_OT_RATE_PER_HOUR")
	private String fixedRatePerHour;

	@Column(name = "VARIABLE_OT_RATE_WEEKDAYS")
	private String variableOtRateWeekdays;

	@Column(name = "VARIABLE_OT_RATE_WEEKEND")
	private String variableOtRateWeekend;

	@Column(name = "BASIC_SALARY")
	private String basicSalary;

	@Column(name = "ALLOWANCES")
	private String allowances;

	@Column(name = "TOTAL_FIXED_SALARY")
	private String totalFixedSalary;

	@Column(name = "LOST_OF_PAY")
	private String lostOfPay;

	@Column(name = "OVERTIME_WEEK_DAYS")
	private String overTimeWeekDays;

	@Column(name = "OVERTIME_WEEK_ENDS")
	private String overTimeWeekEnds;

	@Column(name = "PRODUCTION_INCENTIVES")
	private String productionIncentives;

	@Column(name = "TOTAL_SALARY")
	private String totalSalary;

	@Column(name = "ADVANCE_DEDUCTION")
	private String advanceDeduction;

	@Column(name = "NET_SALARY")
	private String netSalary;

	@Column(name = "SALARY_AS_PER_LABOUR_CONTRACT")
	private String salaryAsPerLabourContract;

	@Column(name = "DIFFERENCE_IN_SALARY")
	private String differenceInSalary;

	@Column(name = "AMOUNT_TO_BE_CREDIT")
	private String amountToBeCredit;

	@Column(name = "NET_PAYABLE_RECEVABLE")
	private String netPayableRevevable;

	@Column(name = "MODE_OF_TRANSFER")
	private String modeOftransfer;

	@Column(name = "SALARY_PROCESS_DATE")
	private Date salaryProcessDate;

	@Column(name = "SALARY_PROCESS_MONTH")
	private String salaryProcessMonth;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@OneToOne
	@JoinColumn(name = "EMPLOYEE_CODE")
	private MasterDataEntity masterDataEntitySalaryProcess;

	public SalaryProcessEntity() {
		super();
	}

	public SalaryProcessEntity(String employeeCode, String noOfDaysWork, String totalOTHours,
			String productionIncentiveHours, String fixedRatePerHour, String variableOtRateWeekdays,
			String variableOtRateWeekend, String basicSalary, String allowances, String totalFixedSalary,
			String lostOfPay, String overTimeWeekDays, String overTimeWeekEnds, String productionIncentives,
			String totalSalary, String advanceDeduction, String netSalary, String salaryAsPerLabourContract,
			String differenceInSalary, String amountToBeCredit, String netPayableRevevable, String modeOftransfer,
			Date salaryProcessDate, String salaryProcessMonth, String createdBy, Date createdOn,
			MasterDataEntity masterDataEntitySalaryProcess) {
		super();
		this.employeeCode = employeeCode;
		this.noOfDaysWork = noOfDaysWork;
		this.totalOTHours = totalOTHours;
		this.productionIncentiveHours = productionIncentiveHours;
		this.fixedRatePerHour = fixedRatePerHour;
		this.variableOtRateWeekdays = variableOtRateWeekdays;
		this.variableOtRateWeekend = variableOtRateWeekend;
		this.basicSalary = basicSalary;
		this.allowances = allowances;
		this.totalFixedSalary = totalFixedSalary;
		this.lostOfPay = lostOfPay;
		this.overTimeWeekDays = overTimeWeekDays;
		this.overTimeWeekEnds = overTimeWeekEnds;
		this.productionIncentives = productionIncentives;
		this.totalSalary = totalSalary;
		this.advanceDeduction = advanceDeduction;
		this.netSalary = netSalary;
		this.salaryAsPerLabourContract = salaryAsPerLabourContract;
		this.differenceInSalary = differenceInSalary;
		this.amountToBeCredit = amountToBeCredit;
		this.netPayableRevevable = netPayableRevevable;
		this.modeOftransfer = modeOftransfer;
		this.salaryProcessDate = salaryProcessDate;
		this.salaryProcessMonth = salaryProcessMonth;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.masterDataEntitySalaryProcess = masterDataEntitySalaryProcess;
	}

	public MasterDataEntity getMasterDataEntitySalaryProcess() {
		return masterDataEntitySalaryProcess;
	}

	public void setMasterDataEntitySalaryProcess(MasterDataEntity masterDataEntitySalaryProcess) {
		this.masterDataEntitySalaryProcess = masterDataEntitySalaryProcess;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getNoOfDaysWork() {
		return noOfDaysWork;
	}

	public void setNoOfDaysWork(String noOfDaysWork) {
		this.noOfDaysWork = noOfDaysWork;
	}

	public String getTotalOTHours() {
		return totalOTHours;
	}

	public void setTotalOTHours(String totalOTHours) {
		this.totalOTHours = totalOTHours;
	}

	public String getProductionIncentiveHours() {
		return productionIncentiveHours;
	}

	public void setProductionIncentiveHours(String productionIncentiveHours) {
		this.productionIncentiveHours = productionIncentiveHours;
	}

	public String getFixedRatePerHour() {
		return fixedRatePerHour;
	}

	public void setFixedRatePerHour(String fixedRatePerHour) {
		this.fixedRatePerHour = fixedRatePerHour;
	}

	public String getBasicSalary() {
		return basicSalary;
	}

	public void setBasicSalary(String basicSalary) {
		this.basicSalary = basicSalary;
	}

	public String getAllowances() {
		return allowances;
	}

	public void setAllowances(String allowances) {
		this.allowances = allowances;
	}

	public String getTotalFixedSalary() {
		return totalFixedSalary;
	}

	public void setTotalFixedSalary(String totalFixedSalary) {
		this.totalFixedSalary = totalFixedSalary;
	}

	public String getLostOfPay() {
		return lostOfPay;
	}

	public void setLostOfPay(String lostOfPay) {
		this.lostOfPay = lostOfPay;
	}

	public String getOverTimeWeekDays() {
		return overTimeWeekDays;
	}

	public void setOverTimeWeekDays(String overTimeWeekDays) {
		this.overTimeWeekDays = overTimeWeekDays;
	}

	public String getOverTimeWeekEnds() {
		return overTimeWeekEnds;
	}

	public void setOverTimeWeekEnds(String overTimeWeekEnds) {
		this.overTimeWeekEnds = overTimeWeekEnds;
	}

	public String getProductionIncentives() {
		return productionIncentives;
	}

	public void setProductionIncentives(String productionIncentives) {
		this.productionIncentives = productionIncentives;
	}

	public String getTotalSalary() {
		return totalSalary;
	}

	public void setTotalSalary(String totalSalary) {
		this.totalSalary = totalSalary;
	}

	public String getAdvanceDeduction() {
		return advanceDeduction;
	}

	public void setAdvanceDeduction(String advanceDeduction) {
		this.advanceDeduction = advanceDeduction;
	}

	public String getNetSalary() {
		return netSalary;
	}

	public void setNetSalary(String netSalary) {
		this.netSalary = netSalary;
	}

	public String getSalaryAsPerLabourContract() {
		return salaryAsPerLabourContract;
	}

	public void setSalaryAsPerLabourContract(String salaryAsPerLabourContract) {
		this.salaryAsPerLabourContract = salaryAsPerLabourContract;
	}

	public String getDifferenceInSalary() {
		return differenceInSalary;
	}

	public void setDifferenceInSalary(String differenceInSalary) {
		this.differenceInSalary = differenceInSalary;
	}

	public String getAmountToBeCredit() {
		return amountToBeCredit;
	}

	public void setAmountToBeCredit(String amountToBeCredit) {
		this.amountToBeCredit = amountToBeCredit;
	}

	public String getNetPayableRevevable() {
		return netPayableRevevable;
	}

	public void setNetPayableRevevable(String netPayableRevevable) {
		this.netPayableRevevable = netPayableRevevable;
	}

	public String getModeOftransfer() {
		return modeOftransfer;
	}

	public void setModeOftransfer(String modeOftransfer) {
		this.modeOftransfer = modeOftransfer;
	}

	public Date getSalaryProcessDate() {
		return salaryProcessDate;
	}

	public void setSalaryProcessDate(Date salaryProcessDate) {
		this.salaryProcessDate = salaryProcessDate;
	}

	public String getSalaryProcessMonth() {
		return salaryProcessMonth;
	}

	public void setSalaryProcessMonth(String salaryProcessMonth) {
		this.salaryProcessMonth = salaryProcessMonth;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getVariableOtRateWeekdays() {
		return variableOtRateWeekdays;
	}

	public String getVariableOtRateWeekend() {
		return variableOtRateWeekend;
	}

	public void setVariableOtRateWeekdays(String variableOtRateWeekdays) {
		this.variableOtRateWeekdays = variableOtRateWeekdays;
	}

	public void setVariableOtRateWeekend(String variableOtRateWeekend) {
		this.variableOtRateWeekend = variableOtRateWeekend;
	}

}
