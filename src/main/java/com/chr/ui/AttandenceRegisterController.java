package com.chr.ui;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.tomcat.jni.Local;

import com.chr.business.MasterDataBusiness;
import com.chr.entity.AttandenceRegisterEntity;
import com.chr.entity.MasterDataEntity;

@ManagedBean(name = "attandenceRegisterController")
@ViewScoped
public class AttandenceRegisterController implements Serializable {

	private static final long serialVersionUID = 1L;
	private AttandenceRegisterEntity selectedEntity;
	MasterDataBusiness masterDataBussiness = new MasterDataBusiness();
	private List<AttandenceRegisterEntity> attandenceRegisterList = null;
	private Boolean isRender = false;

	public AttandenceRegisterController() {
		super();
		selectedEntity = new AttandenceRegisterEntity();

	}

	public List<AttandenceRegisterEntity> getAttandenceList() {
		attandenceRegisterList = masterDataBussiness.getAttandenceRegisterList();
		return attandenceRegisterList;
	}

	public void saveAttandenceRegisterDetail(AttandenceRegisterEntity attandenceRegisterEntity) {
		masterDataBussiness.saveAttandenceRegisterDetail(attandenceRegisterEntity);
	}

	public void editAttandenceRegister(AttandenceRegisterEntity attandenceRegisterEntity) {
		masterDataBussiness.editAttandenceRegister(attandenceRegisterEntity);
	}

	public AttandenceRegisterEntity getAttandecRegisterById() {
		String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("attandenceRegisterId");
		selectedEntity = masterDataBussiness.getAttandenceRegisterById(id);
		return selectedEntity;
	}

	public void getEmployeeName(String empCode) {
		String empName = masterDataBussiness.getEmployeeName(empCode);
		selectedEntity.setEmployeeName(empName);
	}

	public void getTotalHoursCount(AttandenceRegisterEntity attandenceEntity) {
		Date timeIn = attandenceEntity.getAttandenceTimeIn();
		Date timeOut = attandenceEntity.getAttandenceTimeOut();

		Date timeInAnother = attandenceEntity.getAttandenceTimeInAnother();
		Date timeOutAnother = attandenceEntity.getAttandenceTimeOutAnother();

		long duration = timeOut.getTime() - timeIn.getTime();
		long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);

		if (timeOutAnother != null && timeInAnother != null) {
			long durationAnother = timeOutAnother.getTime() - timeInAnother.getTime();
			long diffInHoursAnother = TimeUnit.MILLISECONDS.toHours(durationAnother);
			diffInHours = diffInHours + diffInHoursAnother;
		}

		attandenceEntity.setTotalhours(String.valueOf(diffInHours));

		System.out.print("The difference in time is" + diffInHours);

		getTotalOTHoursCount(attandenceEntity);
	}

	public void getTotalOTHoursCount(AttandenceRegisterEntity attandenceEntity) {
		Integer standardHours = Integer.valueOf(JsfUtil.getResourceInstance("STANDARD_HOURS"));
		Integer totalhours = Integer.valueOf(attandenceEntity.getTotalhours());

		if (standardHours.equals(totalhours)) {
			attandenceEntity.setTotalOThours("0");

		} else {
			Date dateOfAttandence = attandenceEntity.getAttandenceDate();
			LocalDate localDate = dateOfAttandence.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			int dayOfMonth = localDate.getDayOfMonth();

			String otHours = "";

			if (dayOfMonth <= 10)
				otHours = JsfUtil.getResourceInstance("LESS_THAN_TEN_MONTH_VALUE");
			else if (dayOfMonth <= 20)
				otHours = JsfUtil.getResourceInstance("GREATER_THAN_TEN_MONTH_VALUE");
			else
				otHours = JsfUtil.getResourceInstance("GREATER_THAN_TWENTY_MONTH_VALUE");

			attandenceEntity.setTotalOThours(otHours);
		}

		getProductionIncentiveCount(attandenceEntity);
	}

	public void getProductionIncentiveCount(AttandenceRegisterEntity attandenceEntity) {
		Integer standardHours = Integer.valueOf(JsfUtil.getResourceInstance("STANDARD_HOURS"));
		Integer totalhours = Integer.valueOf(attandenceEntity.getTotalhours());

		Object res = null;
		Object phours = null;
		Object otHours = null;

		if (attandenceEntity.getTotalOThours().contains("."))
			otHours = Float.valueOf(attandenceEntity.getTotalOThours());
		else
			otHours = Integer.valueOf(attandenceEntity.getTotalOThours());

		if (otHours instanceof Float) {
			res = standardHours + Float.valueOf(otHours.toString());
		} else {
			res = standardHours + Integer.valueOf(otHours.toString());
		}

		if (res instanceof Float) {
			phours = totalhours - Float.valueOf(res.toString());
		} else {
			phours = totalhours - Integer.valueOf(res.toString());
		}

		attandenceEntity.setProductionIncentiveHours(String.valueOf(phours));
		
		isWeekendTrue(attandenceEntity);
	}

	public void isWeekendTrue(AttandenceRegisterEntity attandenceEntity) {
		Date dateOfAttandence = attandenceEntity.getAttandenceDate();
		Locale usLocale = new Locale("ar_AE");

		Calendar c1 = Calendar.getInstance();
		c1.setTime(dateOfAttandence);
		
		if (c1.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY)
			attandenceEntity.setIsWeekend(true);
		else
			attandenceEntity.setIsWeekend(false);

	}

	public void isSecondFormRender() {
		setIsRender(true);
		selectedEntity = new AttandenceRegisterEntity();
	}

	public AttandenceRegisterEntity getSelectedEntity() {
		return selectedEntity;
	}

	public void setSelectedEntity(AttandenceRegisterEntity selectedEntity) {
		this.selectedEntity = selectedEntity;
	}

	public Boolean getIsRender() {
		return isRender;
	}

	public void setIsRender(Boolean isRender) {
		this.isRender = isRender;
	}

}
