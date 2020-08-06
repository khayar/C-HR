package com.chr.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ATTANDENCE_REGISTER")
public class AttandenceRegisterEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ATTANDENCE_REGISTER_ID", unique = true, nullable = false)
	private String attandenceRegisterId;

	@Column(name = "ATTANDENCE_DATE")
	private Date attandenceDate;

	@Column(name = "ATTANDENCE_TIME_IN")
	private Date attandenceTimeIn;

	@Column(name = "ATTANDENCE_TIME_OUT")
	private Date attandenceTimeOut;

	@Column(name = "EMPLOYEE_NAME")
	private String employeeName;

	@Column(name = "EMPLOYEE_CODE")
	private String employeeCode;

	public AttandenceRegisterEntity() {
		super();
	}

	public AttandenceRegisterEntity(String attandenceRegisterId, Date attandenceDate, Date attandenceTimeIn,
			Date attandenceTimeOut, String employeeName, String employeeCode) {
		super();
		this.attandenceRegisterId = attandenceRegisterId;
		this.attandenceDate = attandenceDate;
		this.attandenceTimeIn = attandenceTimeIn;
		this.attandenceTimeOut = attandenceTimeOut;
		this.employeeName = employeeName;
		this.employeeCode = employeeCode;
	}

	public String getAttandenceRegisterId() {
		return attandenceRegisterId;
	}

	public void setAttandenceRegisterId(String attandenceRegisterId) {
		this.attandenceRegisterId = attandenceRegisterId;
	}

	public Date getAttandenceDate() {
		return attandenceDate;
	}

	public void setAttandenceDate(Date attandenceDate) {
		this.attandenceDate = attandenceDate;
	}

	public Date getAttandenceTimeIn() {
		return attandenceTimeIn;
	}

	public void setAttandenceTimeIn(Date attandenceTimeIn) {
		this.attandenceTimeIn = attandenceTimeIn;
	}

	public Date getAttandenceTimeOut() {
		return attandenceTimeOut;
	}

	public void setAttandenceTimeOut(Date attandenceTimeOut) {
		this.attandenceTimeOut = attandenceTimeOut;
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
}
