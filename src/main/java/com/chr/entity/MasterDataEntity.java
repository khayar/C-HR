package com.chr.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "MASTER_DATA")
public class MasterDataEntity {

	@Id
	@Column(name = "EMPLOYEE_CODE", unique = true, nullable = false)
	private String employeeCode;

	@Column(name = "NAME")
	private String employeeName;

	@Column(name = "EMPLOYEE_DESIGNATION")
	private String employeeDesig;

	@Column(name = "DOB")
	private Date employeeDob;

	@Column(name = "DATE_OF_JOINING")
	private Date dateOfJoining;

	@Column(name = "PASSPORT_NO")
	private String passportNo;

	@Column(name = "PASSPORT_EXPIRY")
	private Date passportExpiry;

	@Column(name = "VISA_NO")
	private String visaNo;

	@Column(name = "VISA_EXPIRY")
	private Date visaExpiry;

	@Column(name = "LABOUR_CARD_NO")
	private String labourCardNo;

	@Column(name = "LABOUR_CARD_EXPIRY")
	private Date labourCardExpiry;

	@Column(name = "EMIRATE_ID")
	private String emiratesId;

	@Column(name = "EMIRATES_EXPIRY")
	private Date emiratesExpiry;

	@Column(name = "SUPERVISOR_NAME")
	private String supervisorName;

	@Column(name = "LOAN")
	private String loan;

	@Column(name = "OUTSTANDING_LOAN")
	private String outstandingLoan;

	@Column(name = "OT_TYPE")
	private String otType;

	@Column(name = "OT_RATE")
	private String otRate;

	@Column(name = "BASIC_SALARY")
	private String basicSalary;

	@Column(name = "ALLOWANCES")
	private String allowances;

	@Column(name = "TOTAL_FIXED_SALARY")
	private String totalFixedSalary;

	@Column(name = "SALARY_AS_PER_LABOR_CONTRACT")
	private String salaryAsPerLabourContract;

	@Column(name = "PAYMENT_MODE")
	private String paymentMode;

	@Column(name = "BANK_NAME")
	private String bankName;

	@Column(name = "SALARY_ACCOUNT_NUM")
	private String salaryAccountNum;

	@OneToOne(mappedBy = "masterDataEntitySalaryProcess")
	public SalaryProcessEntity salaryProcessEntity;

	public MasterDataEntity() {
		super();
	}

	public MasterDataEntity(String employeeCode, String employeeName, String employeeDesig, Date employeeDob,
			Date dateOfJoining, String passportNo, Date passportExpiry, String visaNo, Date visaExpiry,
			String labourCardNo, Date labourCardExpiry, String emiratesId, Date emiratesExpiry, String supervisorName,
			String loan, String outstandingLoan, String otType, String otRate, String basicSalary, String allowances,
			String totalFixedSalary, String salaryAsPerLabourContract, String paymentMode, String bankName,
			String salaryAccountNum, SalaryProcessEntity salaryProcessEntity) {
		super();
		this.employeeCode = employeeCode;
		this.employeeName = employeeName;
		this.employeeDesig = employeeDesig;
		this.employeeDob = employeeDob;
		this.dateOfJoining = dateOfJoining;
		this.passportNo = passportNo;
		this.passportExpiry = passportExpiry;
		this.visaNo = visaNo;
		this.visaExpiry = visaExpiry;
		this.labourCardNo = labourCardNo;
		this.labourCardExpiry = labourCardExpiry;
		this.emiratesId = emiratesId;
		this.emiratesExpiry = emiratesExpiry;
		this.supervisorName = supervisorName;
		this.loan = loan;
		this.outstandingLoan = outstandingLoan;
		this.otType = otType;
		this.otRate = otRate;
		this.basicSalary = basicSalary;
		this.allowances = allowances;
		this.totalFixedSalary = totalFixedSalary;
		this.salaryAsPerLabourContract = salaryAsPerLabourContract;
		this.paymentMode = paymentMode;
		this.bankName = bankName;
		this.salaryAccountNum = salaryAccountNum;
		this.salaryProcessEntity = salaryProcessEntity;

	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public Date getEmployeeDob() {
		return employeeDob;
	}

	public void setEmployeeDob(Date employeeDob) {
		this.employeeDob = employeeDob;
	}

	public Date getDateOfJoining() {
		return dateOfJoining;
	}

	public void setDateOfJoining(Date dateOfJoining) {
		this.dateOfJoining = dateOfJoining;
	}

	public String getPassportNo() {
		return passportNo;
	}

	public void setPassportNo(String passportNo) {
		this.passportNo = passportNo;
	}

	public Date getPassportExpiry() {
		return passportExpiry;
	}

	public void setPassportExpiry(Date passportExpiry) {
		this.passportExpiry = passportExpiry;
	}

	public String getVisaNo() {
		return visaNo;
	}

	public void setVisaNo(String visaNo) {
		this.visaNo = visaNo;
	}

	public Date getVisaExpiry() {
		return visaExpiry;
	}

	public void setVisaExpiry(Date visaExpiry) {
		this.visaExpiry = visaExpiry;
	}

	public String getLabourCardNo() {
		return labourCardNo;
	}

	public void setLabourCardNo(String labourCardNo) {
		this.labourCardNo = labourCardNo;
	}

	public Date getLabourCardExpiry() {
		return labourCardExpiry;
	}

	public void setLabourCardExpiry(Date labourCardExpiry) {
		this.labourCardExpiry = labourCardExpiry;
	}

	public String getEmiratesId() {
		return emiratesId;
	}

	public void setEmiratesId(String emiratesId) {
		this.emiratesId = emiratesId;
	}

	public Date getEmiratesExpiry() {
		return emiratesExpiry;
	}

	public void setEmiratesExpiry(Date emiratesExpiry) {
		this.emiratesExpiry = emiratesExpiry;
	}

	public String getSupervisorName() {
		return supervisorName;
	}

	public void setSupervisorName(String supervisorName) {
		this.supervisorName = supervisorName;
	}

	public String getLoan() {
		return loan;
	}

	public void setLoan(String loan) {
		this.loan = loan;
	}

	public String getOutstandingLoan() {
		return outstandingLoan;
	}

	public void setOutstandingLoan(String outstandingLoan) {
		this.outstandingLoan = outstandingLoan;
	}

	public String getOtType() {
		return otType;
	}

	public void setOtType(String otType) {
		this.otType = otType;
	}

	public String getOtRate() {
		return otRate;
	}

	public void setOtRate(String otRate) {
		this.otRate = otRate;
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

	public String getSalaryAsPerLabourContract() {
		return salaryAsPerLabourContract;
	}

	public void setSalaryAsPerLabourContract(String salaryAsPerLabourContract) {
		this.salaryAsPerLabourContract = salaryAsPerLabourContract;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getSalaryAccountNum() {
		return salaryAccountNum;
	}

	public void setSalaryAccountNum(String salaryAccountNum) {
		this.salaryAccountNum = salaryAccountNum;
	}

	public String getEmployeeDesig() {
		return employeeDesig;
	}

	public void setEmployeeDesig(String employeeDesig) {
		this.employeeDesig = employeeDesig;
	}


}
