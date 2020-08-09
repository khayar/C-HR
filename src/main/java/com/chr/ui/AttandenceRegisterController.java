package com.chr.ui;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

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
	private Boolean isRender=false;
	
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
	
	public AttandenceRegisterEntity getAttandecRegisterById() {
		String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("attandenceRegisterId");
		selectedEntity = masterDataBussiness.getAttandenceRegisterById(id);
		return selectedEntity;
	}
	
	public void getEmployeeName(String empCode) {
		String empName = masterDataBussiness.getEmployeeName(empCode);
		selectedEntity.setEmployeeName(empName);
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
