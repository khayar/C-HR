package com.chr.ui;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.chr.business.MasterDataBusiness;
import com.chr.entity.AttandenceRegisterEntity;
import com.chr.entity.MasterDataEntity;
import com.chr.entity.SalaryProcessEntity;

@ManagedBean(name = "masterDataController")
@ViewScoped
public class MasterDataController implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<MasterDataEntity> masterEntityList = null;
	private MasterDataEntity selectedMasterEntity;
	MasterDataBusiness masterDataBussiness = new MasterDataBusiness();

	public MasterDataController() {
		super();
	}

	@PostConstruct
	public void init() {
		// In @PostConstruct (will be invoked immediately after construction and
		// dependency/property injection).
		selectedMasterEntity = new MasterDataEntity();
	}

	public List<MasterDataEntity> getList() {
		masterEntityList = masterDataBussiness.getMasterDataList();
		return masterEntityList;
	}

	public void saveMasterDataDetail(MasterDataEntity masterDataEntity) {
		masterDataBussiness.saveMasterDataDetail(masterDataEntity);
	}

	public void editMasterDataDetail(MasterDataEntity masterDataEntity) {
		masterDataBussiness.editMasterDataDetail(masterDataEntity);
	}

	public MasterDataEntity getMasterDataById() {
		String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("masterDataId");
		selectedMasterEntity = masterDataBussiness.getMasterDataById(id);
		return selectedMasterEntity;
	}

	public MasterDataEntity getSelectedMasterEntity() {
		return selectedMasterEntity;
	}

	public void setSelectedMasterEntity(MasterDataEntity selectedMasterEntity) {
		this.selectedMasterEntity = selectedMasterEntity;
	}

}
