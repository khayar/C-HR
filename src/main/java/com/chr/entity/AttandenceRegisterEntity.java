package com.chr.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "ATTANDENCE_REGISTER")
public class AttandenceRegisterEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ATTANDENCE_REGISTER_ID", unique = true, nullable = false)
	private Integer attandenceRegisterId;

	@Column(name = "EMPLOYEE_CODE")
	private String employeeCode;

	@Column(name = "EMPLOYEE_NAME")
	private String employeeName;

	@Column(name = "ATTANDENCE_DATE")
	@Temporal(TemporalType.DATE)
	private Date attandenceDate;

	@Temporal(TemporalType.TIME)
	@Column(name = "ATTANDENCE_TIME_IN")
	private Date attandenceTimeIn;

	@Temporal(TemporalType.TIME)
	@Column(name = "ATTANDENCE_TIME_OUT")
	private Date attandenceTimeOut;

	@Temporal(TemporalType.TIME)
	@Column(name = "ATTANDENCE_TIME_IN_ANOTHER")
	private Date attandenceTimeInAnother;

	@Temporal(TemporalType.TIME)
	@Column(name = "ATTANDENCE_TIME_OUT_ANOTHER")
	private Date attandenceTimeOutAnother;

	@Column(name = "TOTAL_HOURS")
	private String totalhours;

	@Column(name = "TOTAL_HOURS_DISPLAY")
	private String totalhoursDisplay;

	@Column(name = "TOTAL_MINUTES")
	private String totalMinutes;

	@Column(name = "TOTAL_OT_HOURS")
	private String totalOThours;

	@Column(name = "PRODUCTION_INCENTIVIE_HOURS")
	private String productionIncentiveHours;

	@Column(name = "IS_WEEKEND")
	private Boolean isWeekend;

	@Column(name = "IS_NEXT_DAY")
	private Boolean isNextDay;

	@Column(name = "IS_NEXT_DAY_ANOTHER")
	private Boolean isNextDayAnother;

	@Column(name = "IS_ABSENT")
	private Boolean isAbsent;
	
	@Column(name = "SHORT_HOURS")
	private String shortHours;

	public AttandenceRegisterEntity() {
		super();
	}

	public AttandenceRegisterEntity(Integer attandenceRegisterId, String employeeCode, String employeeName,
			Date attandenceDate, Date attandenceTimeIn, Date attandenceTimeOut, Date attandenceTimeInAnother,
			Date attandenceTimeOutAnother, String totalhours, String totalhoursDisplay, String totalMinutes,
			String totalOThours, String productionIncentiveHours, Boolean isWeekend, Boolean isNextDay,
			Boolean isNextDayAnother, Boolean isAbsent, String shortHours) {
		super();
		this.attandenceRegisterId = attandenceRegisterId;
		this.employeeCode = employeeCode;
		this.employeeName = employeeName;
		this.attandenceDate = attandenceDate;
		this.attandenceTimeIn = attandenceTimeIn;
		this.attandenceTimeOut = attandenceTimeOut;
		this.attandenceTimeInAnother = attandenceTimeInAnother;
		this.attandenceTimeOutAnother = attandenceTimeOutAnother;
		this.totalhours = totalhours;
		this.totalhoursDisplay = totalhoursDisplay;
		this.totalMinutes = totalMinutes;
		this.totalOThours = totalOThours;
		this.productionIncentiveHours = productionIncentiveHours;
		this.isWeekend = isWeekend;
		this.isNextDay = isNextDay;
		this.isNextDayAnother = isNextDayAnother;
		this.isAbsent = isAbsent;
		this.shortHours = shortHours;
	}





	public String getTotalMinutes() {
		return totalMinutes;
	}

	public void setTotalMinutes(String totalMinutes) {
		this.totalMinutes = totalMinutes;
	}

	public Integer getAttandenceRegisterId() {
		return attandenceRegisterId;
	}

	public void setAttandenceRegisterId(Integer attandenceRegisterId) {
		this.attandenceRegisterId = attandenceRegisterId;
	}

	public Date getAttandenceDate() {
		return attandenceDate;
	}

	public Date getAttandenceTimeIn() {
		return attandenceTimeIn;
	}

	public Date getAttandenceTimeOut() {
		return attandenceTimeOut;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public Date getAttandenceTimeInAnother() {
		return attandenceTimeInAnother;
	}

	public Date getAttandenceTimeOutAnother() {
		return attandenceTimeOutAnother;
	}

	public void setAttandenceDate(Date attandenceDate) {
		this.attandenceDate = attandenceDate;
	}

	public void setAttandenceTimeIn(Date attandenceTimeIn) {
		this.attandenceTimeIn = attandenceTimeIn;
	}

	public void setAttandenceTimeOut(Date attandenceTimeOut) {
		this.attandenceTimeOut = attandenceTimeOut;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public void setAttandenceTimeInAnother(Date attandenceTimeInAnother) {
		this.attandenceTimeInAnother = attandenceTimeInAnother;
	}

	public void setAttandenceTimeOutAnother(Date attandenceTimeOutAnother) {
		this.attandenceTimeOutAnother = attandenceTimeOutAnother;
	}

	public String getTotalhours() {
		return totalhours;
	}

	public String getTotalOThours() {
		return totalOThours;
	}

	public String getProductionIncentiveHours() {
		return productionIncentiveHours;
	}

	public void setTotalhours(String totalhours) {
		this.totalhours = totalhours;
	}

	public void setTotalOThours(String totalOThours) {
		this.totalOThours = totalOThours;
	}

	public void setProductionIncentiveHours(String productionIncentiveHours) {
		this.productionIncentiveHours = productionIncentiveHours;
	}

	public Boolean getIsWeekend() {
		return isWeekend;
	}

	public void setIsWeekend(Boolean isWeekend) {
		this.isWeekend = isWeekend;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getTotalhoursDisplay() {
		return totalhoursDisplay;
	}

	public void setTotalhoursDisplay(String totalhoursDisplay) {
		this.totalhoursDisplay = totalhoursDisplay;
	}

	public Boolean getIsNextDay() {
		return isNextDay;
	}

	public void setIsNextDay(Boolean isNextDay) {
		this.isNextDay = isNextDay;
	}

	public Boolean getIsNextDayAnother() {
		return isNextDayAnother;
	}

	public void setIsNextDayAnother(Boolean isNextDayAnother) {
		this.isNextDayAnother = isNextDayAnother;
	}
	
	public Boolean getIsAbsent() {
		return isAbsent;
	}

	public void setIsAbsent(Boolean isAbsent) {
		this.isAbsent = isAbsent;
	}
	public String getShortHours() {
		return shortHours;
	}
	public void setShortHours(String shortHours) {
		this.shortHours = shortHours;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attandenceRegisterId == null) ? 0 : attandenceRegisterId.hashCode());
		result = prime * result + ((employeeCode == null) ? 0 : employeeCode.hashCode());
		result = prime * result + ((employeeName == null) ? 0 : employeeName.hashCode());
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
		AttandenceRegisterEntity other = (AttandenceRegisterEntity) obj;
		if (attandenceRegisterId == null) {
			if (other.attandenceRegisterId != null)
				return false;
		} else if (!attandenceRegisterId.equals(other.attandenceRegisterId))
			return false;
		if (employeeCode == null) {
			if (other.employeeCode != null)
				return false;
		} else if (!employeeCode.equals(other.employeeCode))
			return false;
		if (employeeName == null) {
			if (other.employeeName != null)
				return false;
		} else if (!employeeName.equals(other.employeeName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AttandenceRegisterEntity [attandenceRegisterId=" + attandenceRegisterId + ", employeeCode="
				+ employeeCode + ", employeeName=" + employeeName + "]";
	}

}
