package com.chr.data;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.context.RequestContext;

public class DbManager {

	private static final Logger logger = Logger.getLogger(DbManager.class);

	public <T> void deleteEntity(T entity) {
		Session session = HibernateUtil.buildSessionFactory().openSession();
		Transaction transcation = null;
		try {
			transcation = session.beginTransaction();
			session.delete(entity);
			session.getTransaction().commit();
		} catch (Exception e) {
			logger.info("---------Exception comes in DbManager class delete method()---------");
			e.printStackTrace();
			if (transcation != null) {
				transcation.rollback();
			}
		} finally {
			session.flush();
			session.close();
		}
	}

	public <T> void updateEntity(T entity) {
		Session session = HibernateUtil.buildSessionFactory().openSession();
		Transaction transcation = null;
		try {
			session.beginTransaction();
			session.update(entity);
			session.getTransaction().commit();
			// FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
			// "Update Information",
			// "Data hase been updated");
			// RequestContext.getCurrentInstance().showMessageInDialog(msg);
		} catch (Exception e) {
			logger.info("---------Exception comes in DbManager class  update()---------");
			e.printStackTrace();
			if (transcation != null) {
				transcation.rollback();
			}
		} finally {
			session.flush();
			session.close();
		}
	}

	public <T> void addEntity(T entity) {
		Session session = HibernateUtil.buildSessionFactory().openSession();
		Transaction transcation = null;
		try {
			session.beginTransaction();
			session.save(entity);
			session.getTransaction().commit();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,"Save Information", "New data has been saved");
			RequestContext.getCurrentInstance().showMessageInDialog(msg);
			
			//FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("masterDataController", "");
			
		} catch (Exception e) {
			logger.info("---------Exception comes in DbManager class create()---------");
			e.printStackTrace();
			if (transcation != null) {
				transcation.rollback();
			}
		} finally {
			session.flush();
			session.close();
		}
	}

	public <T> List<T> getEntityList(String tableName) {
		Session session = HibernateUtil.buildSessionFactory().openSession();
		Transaction transcation = null;
		List<T> entityList = null;
		try {
			Query query = session.createQuery("SELECT m FROM "+ tableName + " m");
			entityList = query.list();
		} catch (Exception e) {
			logger.info("---------Exception comes in DbManager class getList()---------");
			e.printStackTrace();
			if (transcation != null) {
				transcation.rollback();
			}
		} finally {
			session.flush();
			session.close();
		}
		return entityList;
	}
	
	public <T> List<T> getAttandenceRegisterList() {
		Session session = HibernateUtil.buildSessionFactory().openSession();
		Transaction transcation = null;
		List<T> entityList = null;
		try {
			Query query = session.createQuery("SELECT m.masterDataId.employeeCode,m.masterDataId.employeeName,m.attandenceTimeIn,m.attandenceTimeOut FROM AttandenceRegisterEntity m");
			entityList = query.list();
		} catch (Exception e) {
			logger.info("---------Exception comes in DbManager class getAttandenceRegisterList()---------");
			e.printStackTrace();
			if (transcation != null) {
				transcation.rollback();
			}
		} finally {
			session.flush();
			session.close();
		}
		return entityList;
	}

	public <T> T getEntityById(String entityId) {
		T entity = null;
		Transaction trns = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			trns = session.beginTransaction();
			String queryString = "from User where id = :id";
			Query query = session.createQuery(queryString);
			query.setInteger("id", Integer.valueOf(entityId));
			entity = (T) query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return entity;
	}

}