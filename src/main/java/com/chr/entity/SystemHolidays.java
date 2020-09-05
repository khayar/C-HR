package com.chr.entity;

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
@Table(name = "SYSTEM_HOLIDAYS")
public class SystemHolidays {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SYSTEM_HOLIDAYS_ID", unique = true, nullable = false)
	private Integer systemHolidayId;

	@Column(name = "START_DATE")
	@Temporal(TemporalType.DATE)
	private Date startDate;

	@Column(name = "END_DATE")
	@Temporal(TemporalType.DATE)
	private Date endDate;

	@Column(name = "HOLIDAY_NAME")
	private String holdayName;

	public SystemHolidays() {
		super();
	}

	public SystemHolidays(Integer systemHolidayId, Date startDate, Date endDate, String holdayName) {
		super();
		this.systemHolidayId = systemHolidayId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.holdayName = holdayName;
	}

	public String getHoldayName() {
		return holdayName;
	}

	public void setHoldayName(String holdayName) {
		this.holdayName = holdayName;
	}

	public Integer getSystemHolidayId() {
		return systemHolidayId;
	}

	public void setSystemHolidayId(Integer systemHolidayId) {
		this.systemHolidayId = systemHolidayId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
