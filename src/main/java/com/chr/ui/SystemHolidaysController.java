package com.chr.ui;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
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

@ManagedBean(name = "systemHolidayController")
@ViewScoped
public class SystemHolidaysController implements Serializable {

	private static final long serialVersionUID = 1L;
	private SystemHolidays selectedEntity;
	MasterDataBusiness masterDataBussiness = new MasterDataBusiness();
	private List<SystemHolidays> systemHolidaysList = null;

	public SystemHolidaysController() {
		super();
	}

	@PostConstruct
	public void init() {
		selectedEntity = new SystemHolidays();

	}

	public List<SystemHolidays> getSystemHolidaysList() {
		systemHolidaysList = masterDataBussiness.getSystemHolidaysList();
		return systemHolidaysList;
	}

	public void saveSystemHolidays(SystemHolidays systemHolidaysEntity) {
		masterDataBussiness.saveSystemHolidays(systemHolidaysEntity);
	}

	public void editSystemHolidays(SystemHolidays systemholidays) {
		masterDataBussiness.editSystemHolidays(systemholidays);
	}

	public SystemHolidays getSelectedEntity() {
		return selectedEntity;
	}

	public void setSelectedEntity(SystemHolidays selectedEntity) {
		this.selectedEntity = selectedEntity;
	}

	public SystemHolidays getSystemHolidayById() {
		String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("systeholidayId");
		selectedEntity = masterDataBussiness.getSystemHolidayById(id);
		return selectedEntity;
	}

}
