package com.chr.ui;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import com.chr.entity.SystemHolidays;

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

	public void getTotalHoursCount(AttandenceRegisterEntity attandenceEntity) throws ParseException {
		Date timeIn = attandenceEntity.getAttandenceTimeIn();
		Date timeOut = attandenceEntity.getAttandenceTimeOut();

		Date timeInAnother = attandenceEntity.getAttandenceTimeInAnother();
		Date timeOutAnother = attandenceEntity.getAttandenceTimeOutAnother();

		LocalTime timeInLocalDate = timeIn.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
		LocalTime timeOutLocalDate = timeOut.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
		
		timeOutLocalDate.getHour();
		timeOutLocalDate.getMinute();
		
		Long totalHoursWorkedInHours = 0L;
		Long totalHoursWorkedInMinutes = 0L;
		String totalHoursFormatted = "";
		
		BigDecimal timeInAdded12 =null;
		BigDecimal timeInAnotherAdded12 =null;
		BigDecimal timeOutAdded12 =null;
		BigDecimal result =null;
		BigDecimal resultAnother =null;
		BigDecimal timeOutAnotherAdded12 =null;
		BigDecimal stHours = new BigDecimal(JsfUtil.getResourceInstance("STANDARD_HOURS"));

		if (attandenceEntity.getIsNextDay()) {
			StringBuilder stringBuilderTimeOut = new StringBuilder();
			stringBuilderTimeOut.append(timeOut.getHours());
			stringBuilderTimeOut.append(".");
			stringBuilderTimeOut.append(timeOut.getMinutes());
			timeOutAdded12 = new BigDecimal(stringBuilderTimeOut.toString());
			
			StringBuilder stringBuilderTimeIn = new StringBuilder();
			stringBuilderTimeIn.append(timeIn.getHours());
			stringBuilderTimeIn.append(".");
			stringBuilderTimeIn.append(timeIn.getMinutes());
			timeInAdded12 = new BigDecimal(stringBuilderTimeIn.toString());
		
			timeOutAdded12 = timeOutAdded12.add(new BigDecimal(24)); //add 24 hours
			
			
			String s[] = timeOutAdded12.toPlainString().split("\\.");
			String hour = s[0];
			String minute = s[1];
			
			Date tempDateOut = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("1970-01-01 00:00:00"); 
			Calendar calendar = GregorianCalendar.getInstance(); 
			calendar.setTime(tempDateOut);    
			calendar.set(Calendar.HOUR,Integer.valueOf(hour));
			calendar.set(Calendar.MINUTE,Integer.valueOf(minute));
			calendar.getTime();
			
			long duration = calendar.getTime().getTime() - timeIn.getTime();
			totalHoursWorkedInHours = TimeUnit.MILLISECONDS.toHours(duration);
			totalHoursWorkedInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
			totalHoursFormatted = String.format("%02d:%02d", totalHoursWorkedInHours,
					TimeUnit.MILLISECONDS.toMinutes(duration) % 60);
			
		//  result = timeOutAdded12.subtract(timeInAdded12);
		//  BigDecimal totalHours = result;
		//	totalHoursWorkedInHours = totalHours.longValue();
		//	totalHoursFormatted = String.valueOf(totalHours);

		} else {
			long duration = timeOut.getTime() - timeIn.getTime();
			totalHoursWorkedInHours = TimeUnit.MILLISECONDS.toHours(duration);
			totalHoursWorkedInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
			totalHoursFormatted = String.format("%02d:%02d", totalHoursWorkedInHours,
					TimeUnit.MILLISECONDS.toMinutes(duration) % 60);

			if (timeOutAnother != null && timeInAnother != null) {

				if (attandenceEntity.getIsNextDayAnother()) {

					StringBuilder stringBuilderTimeOut = new StringBuilder();
					stringBuilderTimeOut.append(timeOutAnother.getHours());
					stringBuilderTimeOut.append(".");
					stringBuilderTimeOut.append(timeOutAnother.getMinutes());
					timeOutAnotherAdded12 = new BigDecimal(stringBuilderTimeOut.toString());

					StringBuilder stringBuilderTimeIn = new StringBuilder();
					stringBuilderTimeIn.append(timeInAnother.getHours());
					stringBuilderTimeIn.append(".");
					stringBuilderTimeIn.append(timeInAnother.getMinutes());
					timeInAnotherAdded12 = new BigDecimal(stringBuilderTimeIn.toString());

					timeOutAnotherAdded12 = timeOutAnotherAdded12.add(new BigDecimal(24)); // add 24 hours
					
					//resultAnother = timeOutAnotherAdded12.subtract(timeInAnotherAdded12);
					
					String s[] = timeOutAnotherAdded12.toPlainString().split("\\.");
					String hour = s[0];
					String minute = s[1];
					
					Date tempDateOut = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("1970-01-01 00:00:00"); 
					Calendar calendar = GregorianCalendar.getInstance(); 
					calendar.setTime(tempDateOut);    
					calendar.set(Calendar.HOUR,Integer.valueOf(hour));
					calendar.set(Calendar.MINUTE,Integer.valueOf(minute));
					calendar.getTime();
					
					long durationAnother = calendar.getTime().getTime() - timeOutAnother.getTime();
					totalHoursWorkedInHours = TimeUnit.MILLISECONDS.toHours(durationAnother);
					totalHoursWorkedInMinutes = TimeUnit.MILLISECONDS.toMinutes(durationAnother);
					totalHoursFormatted = String.format("%02d:%02d", totalHoursWorkedInHours,
							TimeUnit.MILLISECONDS.toMinutes(durationAnother) % 60);
					
					
					long addTimes = durationAnother + duration;
					totalHoursWorkedInHours = TimeUnit.MILLISECONDS.toHours(addTimes);
					totalHoursWorkedInMinutes = TimeUnit.MILLISECONDS.toMinutes(addTimes);
					totalHoursFormatted = String.format("%02d:%02d", totalHoursWorkedInHours,
							TimeUnit.MILLISECONDS.toMinutes(addTimes) % 60);
					
//					String time1Diff = totalHoursFormatted.replace(":", ".");
//					BigDecimal time1DiffBD = new BigDecimal(time1Diff);
//					BigDecimal totalHours = resultAnother.add(time1DiffBD);
//					totalHoursFormatted = totalHours.toString();
	
				} else {
					long durationAnother = timeOutAnother.getTime() - timeInAnother.getTime();
					long sumUpHours = duration + durationAnother;
					long hours = TimeUnit.MILLISECONDS.toHours(sumUpHours);
					long minutes = TimeUnit.MILLISECONDS.toMinutes(sumUpHours);

					totalHoursWorkedInHours = hours;
					totalHoursWorkedInMinutes = minutes;

					totalHoursFormatted = String.format("%02d:%02d", hours, minutes % 60);
				}

			}
		}

		attandenceEntity.setTotalhours(String.valueOf(totalHoursWorkedInHours));
		attandenceEntity.setTotalMinutes(String.valueOf(totalHoursWorkedInMinutes));
		attandenceEntity.setTotalhoursDisplay(totalHoursFormatted);

		System.out.print("The difference in time is = " + totalHoursFormatted);

		getTotalOTHoursCount(attandenceEntity);
	}

	public void getTotalOTHoursCount(AttandenceRegisterEntity attandenceEntity) throws ParseException {
		BigDecimal standardHours = new BigDecimal(JsfUtil.getResourceInstance("STANDARD_HOURS"));
		if(getHolidaysAndWeekend(attandenceEntity))
				standardHours = new BigDecimal("0");
		
		String totalHoursString = attandenceEntity.getTotalhoursDisplay().replace(":", ".");
		BigDecimal totalhours = new BigDecimal(totalHoursString);
		Integer totalMinutes = Integer.valueOf(attandenceEntity.getTotalMinutes()) % 60;

		Integer totalMinutesDivideBy60 = totalMinutes % 60;
		//BigDecimal totalHoursInMilli = new BigDecimal(Double.valueOf(totalhours + "." + totalMinutesDivideBy60));
		BigDecimal totalHoursInMilli = totalhours;
		
		BigDecimal result = totalHoursInMilli.subtract(standardHours);

		if ((totalhours.compareTo(standardHours) == -1) || (standardHours.equals(totalhours)) && totalMinutes == 0) {
			attandenceEntity.setTotalOThours("0");
			attandenceEntity.setProductionIncentiveHours("0");
		}
		else {
			Date dateOfAttandence = attandenceEntity.getAttandenceDate();
			Calendar cal = Calendar.getInstance(); // locale-specific
			cal.setTime(dateOfAttandence);
			dateOfAttandence = cal.getTime();
			LocalDate localDate = dateOfAttandence.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			int dayOfMonth = localDate.getDayOfMonth();

			String range = "";
			Integer otHours = 0;
			BigDecimal productionIncentiveHours = new BigDecimal(0.0);
			
			if (dayOfMonth <= 10) {
				range = JsfUtil.getResourceInstance("LESS_THAN_TEN_MONTH_VALUE");

			} else if (dayOfMonth <= 20) {
				range = JsfUtil.getResourceInstance("GREATER_THAN_TEN_MONTH_VALUE");
			} else {
				range = JsfUtil.getResourceInstance("GREATER_THAN_TWENTY_MONTH_VALUE");
			}

			if (range.equals("2") || range.equals("1")) {
				if (new BigDecimal(range).compareTo(result) == 0) {
					otHours = 0;
					attandenceEntity.setTotalOThours(String.valueOf(result));
					productionIncentiveHours = productionIncentiveHours.stripTrailingZeros();
					attandenceEntity.setProductionIncentiveHours(String.valueOf(productionIncentiveHours));
				} else if (result.compareTo(new BigDecimal(range)) == -1) {
					productionIncentiveHours = productionIncentiveHours.stripTrailingZeros();
					attandenceEntity.setTotalOThours(String.valueOf(result));
					attandenceEntity.setProductionIncentiveHours(String.valueOf(productionIncentiveHours));
				} else {
					productionIncentiveHours = result.subtract(new BigDecimal(range));
					productionIncentiveHours = productionIncentiveHours.stripTrailingZeros();
					attandenceEntity.setTotalOThours(String.valueOf(range));
					attandenceEntity.setProductionIncentiveHours(productionIncentiveHours.toPlainString());

				}
			}
			if (range.equals("1.5")) {
				if (new BigDecimal(range).compareTo(result) == 0) {
					otHours = 0;
					attandenceEntity.setTotalOThours(String.valueOf(otHours));
					attandenceEntity.setProductionIncentiveHours(String.valueOf(productionIncentiveHours));
				} else if (result.compareTo(new BigDecimal(range)) == -1) {
					productionIncentiveHours = productionIncentiveHours.stripTrailingZeros();
					attandenceEntity.setTotalOThours(String.valueOf(result));
					attandenceEntity.setProductionIncentiveHours(String.valueOf(productionIncentiveHours));
				} else {
					String s[] = new BigDecimal("1.30").toPlainString().split("\\.");
					String shour = s[0];
					String sminute = s[1];
					
					String s1[] = result.toPlainString().split("\\.");
					String reshour = s1[0];
					String resminute = s1[1];
					
					Date stdHourTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("1970-01-01 00:00:00"); 
					Date resultingTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("1970-01-01 00:00:00");
					
					Calendar calendarStnd = GregorianCalendar.getInstance(); 
					calendarStnd.setTime(stdHourTime);    
					calendarStnd.set(Calendar.HOUR,Integer.valueOf(shour));
					calendarStnd.set(Calendar.MINUTE,Integer.valueOf(sminute));
					calendarStnd.getTime();
					
					Calendar calendarResultingTime = GregorianCalendar.getInstance(); 
					calendarResultingTime.setTime(resultingTime);    
					calendarResultingTime.set(Calendar.HOUR,Integer.valueOf(reshour));
					calendarResultingTime.set(Calendar.MINUTE,Integer.valueOf(resminute));
					calendarResultingTime.getTime();
			
					long durationAnother = calendarResultingTime.getTime().getTime() - calendarStnd.getTime().getTime();
					long hour = TimeUnit.MILLISECONDS.toHours(durationAnother);
					long minute = TimeUnit.MILLISECONDS.toMinutes(durationAnother);
					String formatStr = String.format("%02d:%02d", hour,TimeUnit.MILLISECONDS.toMinutes(durationAnother) % 60);

					//BigDecimal productionIncentiveHoursFraction = result.subtract(new BigDecimal("1.30"));
					//productionIncentiveHoursFraction = productionIncentiveHoursFraction.stripTrailingZeros();
					attandenceEntity.setTotalOThours("1.30");
					attandenceEntity.setProductionIncentiveHours(formatStr);

				}
			}

		}

		isWeekendTrue(attandenceEntity);
	}

	public void getProductionIncentiveCount(AttandenceRegisterEntity attandenceEntity) {

		Integer standardHours = Integer.valueOf(JsfUtil.getResourceInstance("STANDARD_HOURS"));
		Integer totalhours = Integer.valueOf(attandenceEntity.getTotalhours());
		Integer totalMinutes = Integer.valueOf(attandenceEntity.getTotalMinutes());
		Integer totalMinutesDivideBy60 = totalMinutes % 60;

		Calendar cal = Calendar.getInstance();
		cal.setTime(attandenceEntity.getAttandenceDate());
		cal.add(Calendar.HOUR_OF_DAY, totalhours);
		cal.add(Calendar.MINUTE, totalMinutes % 60);
		Double totalHoursInMilli = Double.valueOf(totalhours + "." + totalMinutesDivideBy60);

		Object res = null;
		Double phours = null;
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
			phours = Double.valueOf(res.toString()) - totalHoursInMilli;
		} else {
			phours = Integer.valueOf(res.toString()) - totalHoursInMilli;
		}
		df.setRoundingMode(RoundingMode.DOWN);
		String phoursInString = df.format(phours).replace("-", "");

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
	
	public void isAbsentCheck(AttandenceRegisterEntity attandenceEntity)  {
		attandenceEntity.setTotalhours("0");
		attandenceEntity.setTotalOThours("0");
		attandenceEntity.setTotalhoursDisplay("0");
		attandenceEntity.setTotalMinutes("0");
		attandenceEntity.setProductionIncentiveHours("0");
		attandenceEntity.setIsWeekend(false);
	}

	public Boolean getHolidaysAndWeekend(AttandenceRegisterEntity attandenceEntity){
		Boolean isHolidayWeekend= false;
		Date dateOfAttandence = attandenceEntity.getAttandenceDate();
		LocalDate date = dateOfAttandence.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate beginningOfMonth = date.withDayOfMonth(1);
		LocalDate endOfMonth = beginningOfMonth;

		List<SystemHolidays> systemHolidaysEntityList = masterDataBussiness.getTotalHolidaysOfMonth(date,date);
		if(!systemHolidaysEntityList.isEmpty()){
			isHolidayWeekend = true;
		}
		
		return isHolidayWeekend;
	}
	
	public void isSecondFormRender() {
		setIsRender(true);
		selectedEntity = new AttandenceRegisterEntity();
	}

	public String redirect() {
		return "attandenceRegisterList.xhtml";
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

	public void isNextDaySelection() {
		System.out.print("HELLO");
	}
	
}
