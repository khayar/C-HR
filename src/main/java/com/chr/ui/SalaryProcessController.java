package com.chr.ui;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.chr.business.MasterDataBusiness;
import com.chr.entity.AttandenceRegisterEntity;
import com.chr.entity.MasterDataEntity;
import com.chr.entity.SalaryProcessEntity;

@ManagedBean(name = "salaryProcessController")
@ViewScoped
public class SalaryProcessController implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<MasterDataEntity> masterEntityList = null;
	private SalaryProcessEntity selectedMasterEntity;
	MasterDataBusiness masterDataBussiness = new MasterDataBusiness();

	public SalaryProcessController() {
		super();
		selectedMasterEntity = new SalaryProcessEntity();
		MasterDataEntity master = new MasterDataEntity(); 
	}
	
	public List<MasterDataEntity> getMasterDataList() {
		if(masterEntityList == null){
			masterEntityList = masterDataBussiness.getMasterDataList();
			for(int i=0; i< masterEntityList.size();i++){
				//masterEntityList.get(i).salaryprocessEntity = new SalaryProcessEntity();
			}
		}
		return masterEntityList;
	}
	
	public List<MasterDataEntity> getMasterList() {
		if(masterEntityList == null){
			masterEntityList = masterDataBussiness.getMasterList();
		}
		return masterEntityList;
	}
	
	public void processSalary(List<MasterDataEntity> masterList){
		System.out.println("Comes here");
		FacesMessage msg = new FacesMessage("Salary has been processed and sends for approval");
        FacesContext.getCurrentInstance().addMessage(null, msg);
         
	}
	
}
