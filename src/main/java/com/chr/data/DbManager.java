package com.chr.data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
//import org.primefaces.context.RequestContext;

import com.chr.entity.SalaryProcessEntity;

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
			FacesMessage msg = new FacesMessage("Data has been updated successfully.");
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
		try {
			session.beginTransaction();
			session.save(entity);
			session.getTransaction().commit();
			FacesMessage msg = new FacesMessage("New data has been saved");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(entity.toString(), null);

		} catch (ConstraintViolationException ce) {
			logger.info("---------ConstraintViolationException Exception comes in DbManager class create()---------");

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, ce.getSQLException().toString(), ce.getMessage()));
			ce.printStackTrace();
			session.clear();
		} catch (Exception e) {
			logger.info("---------Exception comes in DbManager class create()---------");
			e.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
	}

	public <T> void addSalaryEntity(T entity) {
		Session session = HibernateUtil.buildSessionFactory().openSession();
		try {
			session.beginTransaction();
			session.save(entity);
			session.getTransaction().commit();

		} catch (ConstraintViolationException ce) {
			logger.info(
					"---------ConstraintViolationException Exception comes in DbManager class addSalaryEntity---------");
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, ce.getSQLException().toString(), ce.getMessage()));
			ce.printStackTrace();
			session.clear();
		} catch (Exception e) {
			logger.info("---------Exception comes in DbManager class addSalaryEntity---------");
			e.printStackTrace();
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
			Query query = session.createQuery("SELECT m FROM " + tableName + " m");
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

	public <T> List<T> getMasterDataList() {
		Session session = HibernateUtil.buildSessionFactory().openSession();
		String sql = "select master.name,master.EMPLOYEE_CODE,master.EMPLOYEE_DESIGNATION,attandence.ATTANDENCE_TIME_IN,attandence.ATTANDENCE_TIME_OUT ,salary.BASIC_SALARY "
				+ " from MASTER_DATA master inner join ATTANDENCE_REGISTER attandence on master.EMPLOYEE_CODE=attandence.EMPLOYEE_CODE"
				+ " left join SALARY_PROCESS salary on master.EMPLOYEE_CODE=salary.EMPLOYEE_CODE";

		SQLQuery query = session.createSQLQuery(sql);
		// query.setParameter("roll", a);
		// query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		return query.list();

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

	public <T> List<T> getSystemHolidaysList() {
		Session session = HibernateUtil.buildSessionFactory().openSession();
		List<T> entityList = null;
		try {
			Query query = session.createQuery("SELECT m FROM SystemHolidays m");
			entityList = query.list();
		} catch (Exception e) {
			logger.info("---------Exception comes in DbManager class getSystemHolidaysList()---------");
			e.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return entityList;
	}

	public <T> List<T> getCountOfVariableOTRateForWeekendWeekdays(Boolean isWeekend, String empCode, LocalDate fromDate,
			LocalDate endDate) {
		Session session = HibernateUtil.buildSessionFactory().openSession();
		Transaction transcation = null;
		List<T> entityList = null;
		try {
			Query query = session.createQuery("SELECT m FROM AttandenceRegisterEntity m where m.employeeCode='"
					+ empCode + "' AND m.isWeekend=" + isWeekend + " " + "AND m.attandenceDate BETWEEN '" + fromDate
					+ "' AND '" + endDate + "'");
			entityList = query.list();
		} catch (Exception e) {
			logger.info(
					"---------Exception comes in DbManager class getCountOfVariableOTRateForWeekendWeekdays()---------");
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

	public <T> List<T> getTotalNoOfDaysWork(String empCode, LocalDate fromDate, LocalDate endDate) {
		Session session = HibernateUtil.buildSessionFactory().openSession();
		Transaction transcation = null;
		List<T> entityList = null;
		try {
			Query query = session.createQuery("SELECT m FROM AttandenceRegisterEntity m where m.employeeCode='"
					+ empCode + "'" + "AND m.attandenceDate BETWEEN '" + fromDate + "' AND '" + endDate + "'");
			entityList = query.list();
		} catch (Exception e) {
			logger.info("---------Exception comes in DbManager class getTotalNoOfDaysWork()---------");
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

	public Object getTotalNoOfProductionIncentiveHours(String empCode, LocalDate fromDate, LocalDate endDate) {
		Session session = HibernateUtil.buildSessionFactory().openSession();
		Transaction transcation = null;
		Object totalProductionIncentiveHours = null;
		try {
			Query query = session.createQuery(
					"SELECT SUM(CAST(PRODUCTION_INCENTIVIE_HOURS as float)) FROM AttandenceRegisterEntity m where m.employeeCode='"
							+ empCode + "'" + "AND m.attandenceDate BETWEEN '" + fromDate + "' AND '" + endDate + "'");
			totalProductionIncentiveHours = query.uniqueResult();
		} catch (Exception e) {
			logger.info("---------Exception comes in DbManager class getTotalNoOfProductionIncentiveHours()---------");
			e.printStackTrace();
			if (transcation != null) {
				transcation.rollback();
			}
		} finally {
			session.flush();
			session.close();
		}
		return totalProductionIncentiveHours;
	}

	public <T> T getMasterDataById(String entityId) {
		T entity = null;
		Transaction trns = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			trns = session.beginTransaction();
			String queryString = "from MasterDataEntity where employeeCode = :id";
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

	public <T> T getAttandenceRegisterById(String entityId) {
		T entity = null;
		Transaction trns = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			trns = session.beginTransaction();
			String queryString = "from AttandenceRegisterEntity where attandenceRegisterId = :id";
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

	public <T> List<T> getAttandenceRegisterByDate(LocalDate fromDate, LocalDate endDate) {
		Session session = HibernateUtil.buildSessionFactory().openSession();
		Transaction transcation = null;
		List<T> entityList = null;
		try {
			transcation = session.beginTransaction();
			Query query = session
					.createQuery("SELECT m FROM AttandenceRegisterEntity m where m.attandenceDate BETWEEN '" + fromDate
							+ "' AND '" + endDate + "'");
			entityList = query.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return entityList;
	}

	public <T> T getSystemHolidayById(String entityId) {
		T entity = null;
		Transaction trns = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			trns = session.beginTransaction();
			String queryString = "from SystemHolidays where systemHolidayId = :id";
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
			// query.setInteger ("id", Integer.valueOf(entityId));
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

	public <T> List<T> getTotalHolidaysOfMonth(LocalDate fromDate, LocalDate endDate) {
		Session session = HibernateUtil.buildSessionFactory().openSession();
		Transaction transcation = null;
		List<T> entityList = null;
		try {
			Query query = session.createQuery("SELECT m FROM SystemHolidays m where m.startDate >= '" + fromDate
					+ "' and m.endDate <= '" + endDate + "'");
			entityList = query.list();
		} catch (Exception e) {
			logger.info("---------Exception comes in DbManager class getTotalHolidaysOfMonth()---------");
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

	public <T> void updateAttandenceRegisterEntity(T entity) {
		Session session = HibernateUtil.buildSessionFactory().openSession();
		Transaction transcation = null;
		try {
			session.beginTransaction();
			session.update(entity);
			session.getTransaction().commit();
		} catch (Exception e) {
			logger.info("---------Exception comes in DbManager class  updateAttandenceRegisterEntity()---------");
			e.printStackTrace();
			if (transcation != null) {
				transcation.rollback();
			}
		} finally {
			session.flush();
			session.close();
		}
	}

	public <T> List<T> getUserMenuPermissionList(String userId) {
		Session session = HibernateUtil.buildSessionFactory().openSession();
		Transaction transcation = null;
		List<T> entityList = null;
		try {
			Query query = session.createQuery("SELECT m FROM UserMenuPermission m where m.userPermissionKey.userID = '"
					+ userId + "' order by sortId");
			entityList = query.list();

		} catch (Exception e) {
			logger.info("---------Exception comes in DbManager class getUserMenuPermissionList()---------");
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

	public <T> List<T> getSalaryApprovalList() {
		Session session = HibernateUtil.buildSessionFactory().openSession();
		Transaction transcation = null;
		List<T> entityList = null;
		try {
			Query query = session
					.createQuery("SELECT m FROM SalaryProcessEntity m where m.currentState = 'Approval Required'");
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

	public <T> List<T> getSalaryReturnList() {
		Session session = HibernateUtil.buildSessionFactory().openSession();
		Transaction transcation = null;
		List<T> entityList = null;
		try {
			Query query = session.createQuery("SELECT m FROM SalaryProcessEntity m where m.currentState = 'Returned'");
			entityList = query.list();
		} catch (Exception e) {
			logger.info("---------Exception comes in DbManager class getSalaryReturnList()---------");
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

	public int getSalaryApproved(List<SalaryProcessEntity> salaryEntityList, String flag) {
		Session session = HibernateUtil.buildSessionFactory().openSession();
		Transaction transcation = session.beginTransaction();
		int countUpdate = 0;
		try {
			FacesMessage msg=null;	
			if (flag.equals("1")) {
				for (SalaryProcessEntity salaryEntity : salaryEntityList) {
					countUpdate = session
							.createQuery("Update SalaryProcessEntity set CURRENT_STATE='Approved' where EMPLOYEE_CODE='"
									+ salaryEntity.getEmployeeCode() + "' AND SALARY_PROCESS_DATE='"
									+ salaryEntity.getSalaryProcessDate() + "'")
							.executeUpdate();
					transcation.commit();
				}
				 msg = new FacesMessage("Salary has been approved.");
			}
			else {
				for (SalaryProcessEntity salaryEntity : salaryEntityList) {
					countUpdate = session
							.createQuery("Update SalaryProcessEntity set CURRENT_STATE='Returned' where EMPLOYEE_CODE='"
									+ salaryEntity.getEmployeeCode() + "' AND SALARY_PROCESS_DATE='"
									+ salaryEntity.getSalaryProcessDate() + "'")
							.executeUpdate();
					transcation.commit();
				}
				 msg = new FacesMessage("Salary has been returned to maker");
		
			}
			
			
			FacesContext.getCurrentInstance().addMessage(null, msg);

		} catch (Exception e) {
			logger.info("---------Exception comes in DbManager class getSalaryApproved()---------");
			e.printStackTrace();
			if (transcation != null) {
				transcation.rollback();
			}
		} finally {
			session.flush();
			session.close();
		}
		return countUpdate;
	}
}
