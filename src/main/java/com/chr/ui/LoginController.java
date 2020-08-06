package com.chr.ui;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;;

@ManagedBean(name = "login")
@RequestScoped
public class LoginController {

	private String username;
	private String password;
	private boolean remember = true;

	public void submit() throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();
		
		try {
			Subject currentUser = SecurityUtils.getSubject();
			
			if (!currentUser.isAuthenticated()) {
				UsernamePasswordToken token = new UsernamePasswordToken(username, password);
				token.setRememberMe(remember);
				currentUser.login(token);
				context.getExternalContext().redirect("index.xhtml");
			}
				
			
		} catch (UnknownAccountException uae) {
			FacesMessage message = new FacesMessage("User name not found in the system");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, message);
			
		} catch (IncorrectCredentialsException ice) {
			FacesMessage message = new FacesMessage("Password didn't match");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, message);
		} catch (LockedAccountException lae) {
			FacesMessage message = new FacesMessage("Account for the user is locked. Can't login.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, message);
		} catch (AuthenticationException ae) {
			FacesMessage message = new FacesMessage("Unexpeccted error come");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, message);
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isRemember() {
		return remember;
	}

	public void setRemember(boolean remember) {
		this.remember = remember;
	}

}
