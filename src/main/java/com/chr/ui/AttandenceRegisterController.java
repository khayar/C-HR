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
			result = timeOutAdded12.subtract(timeInAdded12);
			//result = result.stripTrailingZeros();
			
			BigDecimal totalHours = result;
			totalHoursWorkedInHours = totalHours.longValue();
			totalHoursFormatted = String.valueOf(totalHours);

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
					resultAnother = timeOutAnotherAdded12.subtract(timeInAnotherAdded12);
					
					
					String time1Diff = totalHoursFormatted.replace(":", ".");
					BigDecimal time1DiffBD = new BigDecimal(time1Diff);
					
					BigDecimal totalHours = resultAnother.add(time1DiffBD);
					totalHoursFormatted = totalHours.toString();
	
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

	public void getTotalOTHoursCount(AttandenceRegisterEntity attandenceEntity) {
		BigDecimal standardHours = new BigDecimal(JsfUtil.getResourceInstance("STANDARD_HOURS"));
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
					attandenceEntity.setProductionIncentiveHours(String.valueOf(productionIncentiveHours));

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
					BigDecimal productionIncentiveHoursFraction = result.subtract(new BigDecimal("1.30"));
					productionIncentiveHoursFraction = productionIncentiveHoursFraction.stripTrailingZeros();
					attandenceEntity.setTotalOThours("1.30");
					attandenceEntity.setProductionIncentiveHours(String.valueOf(productionIncentiveHoursFraction));

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
