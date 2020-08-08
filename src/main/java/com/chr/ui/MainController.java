package com.chr.ui;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.chr.business.MasterDataBusiness;
import com.chr.entity.AttandenceRegisterEntity;
import com.chr.entity.MasterDataEntity;

@ManagedBean(name = "masterDataController")
@SessionScoped
public class MainController implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<MasterDataEntity> masterEntityList = null;
	
	
	private MasterDataEntity selectedMasterEntity;

	MasterDataBusiness masterDataBussinee = new MasterDataBusiness();

	public MainController() {
		super();
		selectedMasterEntity = new MasterDataEntity();
	}

	public List<MasterDataEntity> getList() {
		masterEntityList = masterDataBussinee.getMasterDataList();
		return masterEntityList;
	}

	public void saveMasterDataDetail(MasterDataEntity masterDataEntity) {
		masterDataBussinee.saveMasterDataDetail(masterDataEntity);
	}
	
	public void editMasterDataDetail(MasterDataEntity masterDataEntity) {
		masterDataBussinee.editMasterDataDetail(masterDataEntity);
	}
	
	public MasterDataEntity getMasterDataById() {
		String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("masterDataId");
		selectedMasterEntity = masterDataBussinee.getMasterDataById(id);
		return selectedMasterEntity;
	}

	
	public MasterDataEntity getSelectedMasterEntity() {
		return selectedMasterEntity;
	}

	public void setSelectedMasterEntity(MasterDataEntity selectedMasterEntity) {
		this.selectedMasterEntity = selectedMasterEntity;
	}

}
