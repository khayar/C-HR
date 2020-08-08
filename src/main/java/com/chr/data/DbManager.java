package com.chr.data;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
//import org.primefaces.context.RequestContext;

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
			FacesMessage msg = new FacesMessage("Master data has been updated");
	        FacesContext.getCurrentInstance().addMessage(null, msg);
	        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(entity.toString(), null);
	        
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
			FacesMessage msg = new FacesMessage("New data has been saved");
	        FacesContext.getCurrentInstance().addMessage(null, msg);
	        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(entity.toString(), null);
	        
	    			
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
			Query query = session.createQuery("SELECT m FROM AttandenceRegisterEntity m");
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
			String queryString = "from MasterDataEntity where masterDataId = :id";
			Query query = session.createQuery(queryString);
			query.setString("id", entityId);
			entity = (T) query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return entity;
	}
	
	
	public <T> T getMasterDataByEmployeeCode(String entityId) {
		T entity = null;
		Transaction trns = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			trns = session.beginTransaction();
			String queryString = "from MasterDataEntity where employeeCode = :id";
			Query query = session.createQuery(queryString);
			//query.setInteger ("id", Integer.valueOf(entityId));
			query.setString("id", entityId);
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