package com.chr.ui;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import com.chr.business.UserMenuPermissionBusiness;
import com.chr.entity.UserMenuPermission;

@ManagedBean(name = "userMenuPermissionController")
@ViewScoped
public class UserMenuPermissionController implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<UserMenuPermission> userMenuPerList = null;
	UserMenuPermissionBusiness masterDataBussiness = new UserMenuPermissionBusiness();
	private MenuModel model;
	
	public UserMenuPermissionController() {
		super();
	}

	@PostConstruct
	public void init() {
		createMenu();
	}

	public List<UserMenuPermission> getList() {
		Subject currentUser = SecurityUtils.getSubject();
		userMenuPerList = masterDataBussiness.getUserMenuPermissionList(currentUser.getPrincipal().toString());
		return userMenuPerList;
	}

	public void createMenu() {
		model = new DefaultMenuModel();
		List<UserMenuPermission> userPermissionList = getList();
		
		DefaultSubMenu firstSubmenu = new DefaultSubMenu("Main Menu");
		
		
		for (UserMenuPermission userMenuPermission : userPermissionList) {
			DefaultMenuItem item = new DefaultMenuItem(userMenuPermission.getPermissionName());
			item.setUrl(userMenuPermission.getPermissionURL());
			//item.setAjax(false);
			//item.setUpdate("menuMessage");
			//item.setIcon("ui-icon-home");
			firstSubmenu.addElement(item);
		}
		model.getElements().add(firstSubmenu);
		/*
		DefaultMenuItem item = new DefaultMenuItem("External");
		item.setUrl("http://www.primefaces.org");
		item.setIcon("ui-icon-home");
		firstSubmenu.addElement(item);

		model.addElement(firstSubmenu);

		// Second submenu
		DefaultSubMenu secondSubmenu = new DefaultSubMenu("Dynamic Actions");

		item = new DefaultMenuItem("Save");
		item.setIcon("ui-icon-disk");
		item.setCommand("#{menuView.save}");
		secondSubmenu.addElement(item);

		item = new DefaultMenuItem("Delete");
		item.setIcon("ui-icon-close");
		item.setCommand("#{menuView.delete}");
		item.setAjax(false);
		secondSubmenu.addElement(item);

		item = new DefaultMenuItem("Redirect");
		item.setIcon("ui-icon-search");
		item.setCommand("#{menuView.redirect}");
		secondSubmenu.addElement(item);

		model.addElement(secondSubmenu);
		*/
	}

	public MenuModel getModel() {
		return model;
	}

	public void setModel(MenuModel model) {
		this.model = model;
	}

}
