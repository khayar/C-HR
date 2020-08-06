package com.chr.ui;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.chr.business.MasterDataBusiness;
import com.chr.entity.AttandenceRegisterEntity;
import com.chr.entity.MasterDataEntity;

@ManagedBean(name = "attandenceRegisterController")
@ViewScoped
public class AttandenceRegisterController {

	private AttandenceRegisterEntity selectedEntity;
	MasterDataBusiness masterDataBussinee = new MasterDataBusiness();

	public void saveAttandenceRegisterDetail(AttandenceRegisterEntity attandenceRegisterEntity) {
		masterDataBussinee.saveAttandenceRegisterDetail(attandenceRegisterEntity);
	}

	public String getEmployeeName() {
		return "Khayar";
	}

	public AttandenceRegisterEntity getSelectedEntity() {
		return selectedEntity;
	}

	public void setSelectedEntity(AttandenceRegisterEntity selectedEntity) {
		this.selectedEntity = selectedEntity;
	}

}
