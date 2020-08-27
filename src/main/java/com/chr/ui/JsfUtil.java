package com.chr.ui;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "util")
@RequestScoped

public class JsfUtil {

	public String getUserFullName() {
		Subject currentUser = SecurityUtils.getSubject();
		return "Welcome " + currentUser.getPrincipal() == null ? "" : currentUser.getPrincipal().toString();
	}
	
	public static Object getUserName() {
		Subject currentUser = SecurityUtils.getSubject();
		return currentUser.getPrincipal().toString();
	}
	
	public void getLogout() throws IOException {
		Subject currentUser = SecurityUtils.getSubject();
		currentUser.logout(); // removes all identifying information and
								// invalidates their session too.
		FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");

	}

	public static String getResourceInstance(String propertyName) {
		ResourceBundle resourceFile = ResourceBundle.getBundle("resource");
		return resourceFile.getString(propertyName);

	}
}
