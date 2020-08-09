package com.chr.ui;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.chr.business.MasterDataBusiness;
import com.chr.entity.AttandenceRegisterEntity;
import com.chr.entity.MasterDataEntity;

@ManagedBean(name = "mainController")
@SessionScoped
public class MainController implements Serializable {

	private static final long serialVersionUID = 1L;

	public MainController() {
		super();
	}
	
	
}
