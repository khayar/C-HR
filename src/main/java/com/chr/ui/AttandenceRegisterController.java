package com.chr.ui;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
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
	private static DecimalFormat df = new DecimalFormat("#.##");
	
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

		Long totalHoursWorkedInHours = 0L;
		Long totalHoursWorkedInMinutes = 0L;
		String totalHoursFormatted = "";
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

			totalHoursWorkedInHours = aLong;

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
			totalHoursWorkedInHours = totalHoursWorkedInHours == 0 ? Long.valueOf(attandenceEntity.getTotalhours()) : totalHoursWorkedInHours;
			totalHoursWorkedInHours = totalHoursWorkedInHours + aLong; // add timein and
															// timein another

		}

		else {
			long duration = timeOut.getTime() - timeIn.getTime();
			totalHoursWorkedInHours   = TimeUnit.MILLISECONDS.toHours (duration);
			totalHoursWorkedInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
			
			totalHoursFormatted = String.format("%02d:%02d", totalHoursWorkedInHours, TimeUnit.MILLISECONDS.toMinutes(duration)%60);
			
			if (timeOutAnother != null && timeInAnother != null) {
				long durationAnother 	= timeOutAnother.getTime() - timeInAnother.getTime();
				
				long diffInHoursAnother   = TimeUnit.MILLISECONDS.toHours(durationAnother);
				long diffInMinutesAnother = TimeUnit.MILLISECONDS.toMinutes(durationAnother);
								
				long sumUpHours =  duration + durationAnother;
				long hours = TimeUnit.MILLISECONDS.toHours(sumUpHours);
				long minutes = TimeUnit.MILLISECONDS.toMinutes(sumUpHours);

				totalHoursWorkedInHours = hours;
				totalHoursWorkedInMinutes = minutes;
				
				totalHoursFormatted = String.format("%02d:%02d", hours, minutes%60);
				
				
			}
		}
		attandenceEntity.setTotalhours(String.valueOf(totalHoursWorkedInHours));
		attandenceEntity.setTotalMinutes(String.valueOf(totalHoursWorkedInMinutes));
		attandenceEntity.setTotalhoursDisplay(totalHoursFormatted);

		System.out.print("The difference in time is = " + totalHoursFormatted);

		getTotalOTHoursCount(attandenceEntity);
	}

	public void getTotalOTHoursCount(AttandenceRegisterEntity attandenceEntity) {
		
		BigDecimal standardHours = new BigDecimal(JsfUtil.getResourceInstance("STANDARD_HOURS"));
		BigDecimal totalhours =  new BigDecimal(attandenceEntity.getTotalhours());
		
		Integer totalMinutes = Integer.valueOf(attandenceEntity.getTotalMinutes());
		BigDecimal minutesFraction = new BigDecimal(totalMinutes);
		
		totalhours = totalhours.add(minutesFraction);
		BigDecimal res = totalhours.subtract(standardHours);
		res = res.divide(new BigDecimal(60));
		
//		if ((totalhours < standardHours) || (standardHours.equals(totalhours)) && totalMinutes == 0 ) {
//			attandenceEntity.setTotalOThours("0");
//			attandenceEntity.setProductionIncentiveHours("0");
//
//		}
		
			Date dateOfAttandence = attandenceEntity.getAttandenceDate();
			LocalDate localDate = dateOfAttandence.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			int dayOfMonth = localDate.getDayOfMonth();

			BigDecimal otHours ;

			if (dayOfMonth <= 10)
				otHours = new BigDecimal(JsfUtil.getResourceInstance("LESS_THAN_TEN_MONTH_VALUE"));
			else if (dayOfMonth <= 20)
				otHours = new BigDecimal(JsfUtil.getResourceInstance("GREATER_THAN_TEN_MONTH_VALUE"));
			else
				otHours = new BigDecimal(JsfUtil.getResourceInstance("GREATER_THAN_TWENTY_MONTH_VALUE"));
			
			if(res.compareTo(otHours) == 0){
				
			}
			
			//attandenceEntity.setTotalOThours(otHours);
			getProductionIncentiveCount(attandenceEntity);
		

		isWeekendTrue(attandenceEntity);
	}

	public void getProductionIncentiveCount(AttandenceRegisterEntity attandenceEntity) {


		Integer standardHours = Integer.valueOf(JsfUtil.getResourceInstance("STANDARD_HOURS"));
		Integer totalhours = Integer.valueOf(attandenceEntity.getTotalhours());
		Integer totalMinutes = Integer.valueOf(attandenceEntity.getTotalMinutes());
		Integer totalMinutesDivideBy60 = totalMinutes%60;
		String val = totalhours+"."+totalMinutesDivideBy60;
		
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(attandenceEntity.getAttandenceDate());
//        cal.add(Calendar.HOUR_OF_DAY,totalhours);
//        cal.add(Calendar.MINUTE,totalMinutes%60);

        BigDecimal totalHoursInMilli = new BigDecimal(val);
        
        
		Object res = null;
		BigDecimal phours = null;
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

		if (res instanceof BigDecimal) {
			phours = new BigDecimal(res.toString()).subtract(totalHoursInMilli) ;
		} else {
			phours = new BigDecimal(res.toString()).subtract(totalHoursInMilli);
		}
//		df.setRoundingMode(RoundingMode.DOWN);
		String phoursInString = df.format(phours).replace("-","");
	
		attandenceEntity.setProductionIncentiveHours(phoursInString.trim());
	}

	public void isWeekendTrue(AttandenceRegisterEntity attandenceEntity) {
		Date dateOfAttandence = attandenceEntity.getAttandenceDate();
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
