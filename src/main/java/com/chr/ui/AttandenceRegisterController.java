package com.chr.ui;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.chr.business.MasterDataBusiness;
import com.chr.entity.AttandenceRegisterEntity;

@ManagedBean(name = "attandenceRegisterController")
@ViewScoped
public class AttandenceRegisterController implements Serializable {

	private static final long serialVersionUID = 1L;
	private AttandenceRegisterEntity selectedEntity;
	MasterDataBusiness masterDataBussiness = new MasterDataBusiness();
	private List<AttandenceRegisterEntity> attandenceRegisterList = null;
	private Boolean isRender = false;
	private List<AttandenceRegisterEntity> filteredRanges;
	
	
	public AttandenceRegisterController() {
		super();

	}

	@PostConstruct
	public void init() {
		// In @PostConstruct (will be invoked immediately after construction and
		// dependency/property injection).
		selectedEntity = new AttandenceRegisterEntity();
		setFilteredRanges(getAttandenceList());
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

		Long totalHoursWorked = 0L;
		// check if timeout is less than timein
		if (timeOut.getTime() < timeIn.getTime()) {
			String stringTime = "1970-01-01 24:00:00";
			Date hours24Date = null;
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			try {
				hours24Date = df.parse(stringTime);
			} catch (ParseException e) {
				System.out.println("Exception comes in getTotalHoursCount() " + e);
				e.printStackTrace();
			}

			LocalDateTime hours24DateTime = hours24Date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

			LocalDateTime dateTimeout = timeOut.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			int hours = dateTimeout.getHour();

			LocalDateTime dateTimeIn = timeIn.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

			Duration totalDurationHours = Duration.ofHours(ChronoUnit.HOURS.between(dateTimeIn, hours24DateTime));
			long aLong = totalDurationHours.toHours();
			aLong = aLong + hours;

			totalHoursWorked = aLong;

		} else if (timeOutAnother != null && (timeOutAnother.getTime() < timeInAnother.getTime())) {
			Date hours24Date = null;
			if (timeOutAnother.getTime() < timeInAnother.getTime()) {
				String stringTime = "1970-01-01 24:00:00";
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				try {
					hours24Date = df.parse(stringTime);
				} catch (ParseException e) {
					System.out.println("Exception comes in getTotalHoursCount() timeinAnother" + e);
					e.printStackTrace();
				}
			}
			LocalDateTime hours24DateTime = hours24Date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

			LocalDateTime dateTimeOutAnother = timeOutAnother.toInstant().atZone(ZoneId.systemDefault())
					.toLocalDateTime();
			int hours = dateTimeOutAnother.getHour();

			LocalDateTime dateTimeInAnother = timeInAnother.toInstant().atZone(ZoneId.systemDefault())
					.toLocalDateTime();

			Duration totalDurationHours = Duration
					.ofHours(ChronoUnit.HOURS.between(dateTimeInAnother, hours24DateTime));
			long aLong = totalDurationHours.toHours();
			aLong = aLong + hours;
			totalHoursWorked = totalHoursWorked == 0 ? Long.valueOf(attandenceEntity.getTotalhours()) : totalHoursWorked;
			totalHoursWorked = totalHoursWorked + aLong; // add timein and
															// timein another

		}

		else {
			long duration = timeOut.getTime() - timeIn.getTime();
			totalHoursWorked = TimeUnit.MILLISECONDS.toHours(duration);

			if (timeOutAnother != null && timeInAnother != null) {
				long durationAnother = timeOutAnother.getTime() - timeInAnother.getTime();
				long diffInHoursAnother = TimeUnit.MILLISECONDS.toHours(durationAnother);
				totalHoursWorked = totalHoursWorked + diffInHoursAnother;
			}
		}
		attandenceEntity.setTotalhours(String.valueOf(totalHoursWorked));

		System.out.print("The difference in time is = " + totalHoursWorked);

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
			otHours = Double.valueOf(attandenceEntity.getTotalOThours());
		else
			otHours = Integer.valueOf(attandenceEntity.getTotalOThours());

		if (otHours instanceof Double) {
			res = standardHours + Double.valueOf(otHours.toString());
		} else {
			res = standardHours + Integer.valueOf(otHours.toString());
		}

		if (res instanceof Double) {
			phours = totalhours - Double.valueOf(res.toString());
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

	public List<AttandenceRegisterEntity> getFilteredRanges() {
		return filteredRanges;
	}

	public void setFilteredRanges(List<AttandenceRegisterEntity> filteredRanges) {
		this.filteredRanges = filteredRanges;
	}

}
