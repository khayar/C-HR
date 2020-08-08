package com.chr.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "ATTANDENCE_REGISTER")
public class AttandenceRegisterEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ATTANDENCE_REGISTER_ID", unique = true, nullable = false)
	private Integer attandenceRegisterId;
	
	@Column(name = "ATTANDENCE_DATE")
	@Temporal(TemporalType.DATE)
	private Date attandenceDate;
	
	@Temporal(TemporalType.TIME)
	@Column(name = "ATTANDENCE_TIME_IN")
	private Date attandenceTimeIn;
	
	@Temporal(TemporalType.TIME)
	@Column(name = "ATTANDENCE_TIME_OUT")
	private Date attandenceTimeOut;

	@Column(name = "EMPLOYEE_NAME")
	private String employeeName;

	@Column(name = "EMPLOYEE_CODE")
	private String employeeCode;

	public AttandenceRegisterEntity() {
		super();
	}

	public AttandenceRegisterEntity(Integer attandenceRegisterId, Date attandenceDate, Date attandenceTimeIn,
			Date attandenceTimeOut, String employeeName, String employeeCode) {
		super();
		this.attandenceRegisterId = attandenceRegisterId;
		this.attandenceDate = attandenceDate;
		this.attandenceTimeIn = attandenceTimeIn;
		this.attandenceTimeOut = attandenceTimeOut;
		this.employeeName = employeeName;
		this.employeeCode = employeeCode;
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attandenceRegisterId == null) ? 0 : attandenceRegisterId.hashCode());
		result = prime * result + ((attandenceTimeIn == null) ? 0 : attandenceTimeIn.hashCode());
		result = prime * result + ((attandenceTimeOut == null) ? 0 : attandenceTimeOut.hashCode());
		result = prime * result + ((employeeCode == null) ? 0 : employeeCode.hashCode());
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
		if (attandenceTimeIn == null) {
			if (other.attandenceTimeIn != null)
				return false;
		} else if (!attandenceTimeIn.equals(other.attandenceTimeIn))
			return false;
		if (attandenceTimeOut == null) {
			if (other.attandenceTimeOut != null)
				return false;
		} else if (!attandenceTimeOut.equals(other.attandenceTimeOut))
			return false;
		if (employeeCode == null) {
			if (other.employeeCode != null)
				return false;
		} else if (!employeeCode.equals(other.employeeCode))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "attandenceRegisterController";
	}
	
}
