package App.core.service.spring;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Session;
import org.hibernate.TransactionException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.impl.SessionImpl;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import App.core.applicationContext.ApplicationContext;
import App.core.beans.BaseBean;
import App.core.beans.Season;
import App.core.dao.IBaseDao;
import App.core.exception.DataBaseException;
import App.core.exception.EmptyResultSetException;
import App.core.exception.InvalidReferenceException;
import App.core.exception.PrimaryKeyViolatedException;
import App.core.exception.UniquePropertyViolatedException;
import App.core.service.IBaseRetrievalService;
import App.core.service.IBaseService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;



//import com.sps.passport.bean.ShipVisitSummeryNationality;

@SuppressWarnings("unchecked")
public class BaseService  implements IBaseService, IBaseRetrievalService {
	private IBaseDao baseDao;
	Logger logger = Logger.getLogger(this.getClass().getName());	

	private final ResourceBundle serviceBundle =null; // = ResourceBundle.getBundle("com.sps.core.resources.ApplicationResources_services_ar");
	private ResourceBundle settingsBundle = ResourceBundle.getBundle("ApplicationSettings_ar");

	public ResourceBundle getSettingsBundle() {
		return settingsBundle;
	}
	
	public BaseService() {
	//	ServicesDirectory.registerService(serviceBundle.getString("BaseService"), this);
	}

	public PlatformTransactionManager getMyTransactionManager() {
		try {
			return ApplicationContext.transactionManager;//(HibernateTransactionManager) CacheEntriesDirectory.getEntry("transactionManager").getCacheEntry();
		} catch (Exception e) {
			return null;
		}
	}

	private void closeTransaction(TransactionStatus status) {
		try {
			if (!status.isCompleted())
				this.getMyTransactionManager().rollback(status);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Object findBean(Class<?> beanClass, Map propertyMap) throws DataBaseException, InvalidReferenceException {
		return this.getBaseDao().findBean(beanClass, propertyMap);
	}

	public List findAllBeans(Class<?> beanClass) throws DataBaseException, EmptyResultSetException {
		return this.getBaseDao().findAllBeans(beanClass, null, null, 0, 0);
	}

	public List<Object> findAllBeans(Class<?> beanClass, Map params, Order order) throws DataBaseException,
			EmptyResultSetException {
		List<Object> nOrder = new ArrayList<Object>();
		if (order != null)
			nOrder.add(order);

		List<Object> nExpression = new ArrayList<Object>();
		String key;

		for (Iterator<Object> itr = params.keySet().iterator(); itr.hasNext();) {
			key = (String) itr.next();

			if (params.get(key) == null)
				nExpression.add(Restrictions.isNull(key));
			else if(params.get(key) instanceof String && ((String)params.get(key)).equals("NOT_NULL") == true)
				nExpression.add(Restrictions.isNotNull(key));
			else
				nExpression.add(Restrictions.eq(key, params.get(key)));
		}
		return this.getBaseDao().findAllBeans(beanClass, nExpression, nOrder, 0, 0);
	}

	public List<Object> findAllBeans(Class<?> beanClass, Order order) throws DataBaseException, EmptyResultSetException {
		ArrayList<Object> nOrder = new ArrayList<Object>();
		if (order != null)
			nOrder.add(order);
		return this.getBaseDao().findAllBeans(beanClass, null, nOrder, 0, 0);
	}

	public List findAllBeans(Class<?> beanClass, Order order, int fromRecord, int maxResult) throws DataBaseException,
			EmptyResultSetException {
		ArrayList<Object> nOrder = new ArrayList<Object>();
		nOrder.add(order);
		return this.getBaseDao().findAllBeans(beanClass, null, nOrder,
				fromRecord, maxResult);
	}

	public List findAllBeansWithParams(Class<?> beanClass, Map params,Order order, int fromRecord, int maxResult)
			throws DataBaseException, EmptyResultSetException {

		List<Object> nOrder = new ArrayList<Object>();
		if (order != null)
			nOrder.add(order);

		List<Object> nExpression = new ArrayList<Object>();
		String key;

		for (Iterator<Object> itr = params.keySet().iterator(); itr.hasNext();) {
			key = (String) itr.next();

			if (params.get(key) == null)
				nExpression.add(Restrictions.isNull(key));
			else
				nExpression.add(Restrictions.eq(key, params.get(key)));
		}
		return this.getBaseDao().findAllBeans(beanClass, nExpression, nOrder,
				fromRecord, maxResult);

	}

	
	public Object getBean(Class<?> beanClass, Object identifier) throws DataBaseException, InvalidReferenceException {
		return this.getBaseDao().findBean(beanClass, identifier);
	}

	public Object getBean(Class beanClass, Map identifier) throws DataBaseException, InvalidReferenceException {
		ArrayList<Object> nExpression = new ArrayList<Object>();
		String key;

		for (Iterator itr = identifier.keySet().iterator(); itr.hasNext();) {
			key = (String) itr.next();
			if (identifier.get(key) == null)
				nExpression.add(Restrictions.isNull(key));
			else
				nExpression.add(Restrictions.eq(key, identifier.get(key)));
		}
		Object bean = null;
		try {
			bean = this.getBaseDao().findAllBeans(beanClass, nExpression, null, 0, 0).get(0);
		} catch (EmptyResultSetException e) {
			String temp = beanClass.toString();
			int x = temp.lastIndexOf(".");

			String errorClass = temp.substring(x + 1, temp.length());

			throw new InvalidReferenceException("error.invalidRef," + errorClass);
		}
		return bean;
	}

	public void validateIdentifier(Class beanClass, Object instance) throws DataBaseException,
			PrimaryKeyViolatedException {
		this.getBaseDao().validateIdentifier(beanClass, instance);
	}

	public void validateUniqueness(Class beanClass, Object instance) throws DataBaseException,
			UniquePropertyViolatedException {
		this.getBaseDao().validateUniqueness(beanClass, instance);
	}

	public void validateUniqueness(Class beanClass, Map propertyMap) throws DataBaseException,
			UniquePropertyViolatedException {
		this.getBaseDao().validateUniqueness(beanClass, propertyMap);
	}

	public void deleteBean(Object bean) throws DataBaseException {
		
		TransactionStatus status = null;
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_NESTED);
		def.setTimeout(Integer.parseInt(this.getSettingsBundle().getString("transactionTimeOut.base.lowTimeOut")));
		
		/*//Added by Mabdelsattar
		setChangeInformation(bean);
		editBean(bean);
*/
		try {
			status = this.getMyTransactionManager().getTransaction(def);

		
			setChangeInformation(bean);
			editBean(bean);
			
			this.getBaseDao().deleteBean(bean);

			this.getMyTransactionManager().commit(status);

		} catch (Exception e) {
			throw new DataBaseException("error.dataBase.delete," + bean.getClass().getSimpleName() + ","
					+ e.getMessage());
		} finally {
			closeTransaction(status);
		}
	}

	public void deleteBeans(List beans) throws DataBaseException {
		TransactionStatus status = null;

		try {
			DefaultTransactionDefinition def = new DefaultTransactionDefinition();
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_NESTED);
			def.setTimeout(Integer.parseInt(this.getSettingsBundle().getString("transactionTimeOut.base.lowTimeOut")));
			status = this.getMyTransactionManager().getTransaction(def);

			if(beans!=null)
			{
			for (int i = 0; i < beans.size(); i++)
				setChangeInformation(beans.get(i));
		
			
			
			
			addEditBeans(beans);
			}
		

			this.getBaseDao().deleteBeans(beans);

			this.getMyTransactionManager().commit(status);

		} catch (Exception e) {
			throw new DataBaseException("error.dataBase.delete," + beans.get(0).getClass().getSimpleName() + ","
					+ e.getMessage());
		} finally {
			closeTransaction(status);
		}
	}

	public void addBean(Object newBean) throws DataBaseException {
		TransactionStatus status = null;

		try {
			DefaultTransactionDefinition def = new DefaultTransactionDefinition();
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_NESTED);
			def.setTimeout(Integer.parseInt(this.getSettingsBundle().getString("transactionTimeOut.base.lowTimeOut")));
			status = this.getMyTransactionManager().getTransaction(def);

			setChangeInformation(newBean);
			((BaseBean)newBean).setTimestamp(new Timestamp(new Date().getTime()));
			
			this.getBaseDao().insertBean(newBean);
			//	this.addEditBean(newBean);
			this.getMyTransactionManager().commit(status);

		} catch (Exception e) {
			e.printStackTrace();
			throw new DataBaseException("error.dataBase.insert," + newBean.getClass().getSimpleName() + ","
					+ e.getMessage());
		} finally {
			closeTransaction(status);
		}
	}

	public void addEditBean(Object newBean) throws DataBaseException {
		TransactionStatus status = null;

		try {
			DefaultTransactionDefinition def = new DefaultTransactionDefinition();
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_NESTED);
			def.setTimeout(Integer.parseInt(this.getSettingsBundle().getString("transactionTimeOut.base.lowTimeOut")));
			status = this.getMyTransactionManager().getTransaction(def);

			setChangeInformation(newBean);
			
			this.getBaseDao().insertOrUpdateBean(newBean);

			this.getMyTransactionManager().commit(status);

		} catch (Exception e) {
			e.printStackTrace();
			String errMsg = e.getMessage();
			try {
				TransactionException nre = (TransactionException) e.getCause();
				SQLException se = (SQLException) nre.getCause();
				errMsg = se.getMessage();
			} catch (Exception e2) {
			}

			throw new DataBaseException("error.dataBase.insert," + newBean.getClass().getSimpleName() + "," + errMsg);
		} finally {
			closeTransaction(status);
		}
  	}

	public void editBean(Object newBean) throws DataBaseException {
		TransactionStatus status = null;

		try {
			DefaultTransactionDefinition def = new DefaultTransactionDefinition();
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_NESTED);
			def.setTimeout(Integer.parseInt(this.getSettingsBundle().getString("transactionTimeOut.base.lowTimeOut")));
			status = this.getMyTransactionManager().getTransaction(def);

			setChangeInformation(newBean);
			
			this.getBaseDao().updateBean(newBean);

			this.getMyTransactionManager().commit(status);
		} catch (Exception e) {
			throw new DataBaseException("error.dataBase.update," + newBean.getClass().getSimpleName() + ","
					+ e.getMessage());
		} finally {
			closeTransaction(status);
		}
	}



	public List findAllBeansExcept(Class beanClass, List nId, Order order) throws DataBaseException,
			EmptyResultSetException {
		return this.getBaseDao().findAllBeansExcept(beanClass, nId, "id", order);
	}

	public List findAllBeansExcept(Class beanClass, List nId, String beanIdentifier, Order order)
			throws DataBaseException, EmptyResultSetException {
		return this.getBaseDao().findAllBeansExcept(beanClass, nId, beanIdentifier, order);
	}

	public void addEditBeans(List nBean) throws DataBaseException {
		TransactionStatus status = null;

		try {
			DefaultTransactionDefinition def = new DefaultTransactionDefinition();
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_NESTED);
			def.setTimeout(Integer.parseInt(this.getSettingsBundle().getString("transactionTimeOut.base.lowTimeOut")));
			status = this.getMyTransactionManager().getTransaction(def);
			if(nBean!=null)
			{
				for (int i = 0; i < nBean.size(); i++)
					setChangeInformation(nBean.get(i));
			}
			
			this.getBaseDao().insertBeans(nBean);

			this.getMyTransactionManager().commit(status);
		} catch (Exception e) {
			throw new DataBaseException("error.dataBase.insert," + nBean.get(0).getClass().getSimpleName() + ","
					+ e.getMessage());
		} finally {
			closeTransaction(status);
		}
	}

	public JSONArray toJSONArray(List<Object> nBean, Map<String, String> propertyMap) throws Exception {
		JSONArray jsonArray = new JSONArray();

		Object bean;
		JSONObject jsonTemp;
		String jsonFieldName;
		String beanFieldName;

		for (Iterator itr = nBean.iterator(); itr.hasNext();) {
			bean = itr.next();
			jsonTemp = new JSONObject();

			for (Iterator keyItr = propertyMap.keySet().iterator(); keyItr.hasNext();) {
				jsonFieldName = (String) keyItr.next();
				beanFieldName = (String) propertyMap.get(jsonFieldName);

				jsonTemp.put(jsonFieldName, PropertyUtils.getProperty(bean, beanFieldName));
			}

			jsonArray.put(jsonTemp);
		}

		return jsonArray;
	}

	public  Map<ResultSet,Session> executeSQLStatement(String sqlQuery) throws DataBaseException,EmptyResultSetException {
		return this.getBaseDao().executeSQLStatement(sqlQuery);
	}

	public List executeMappedSQLStatement(String sqlQuery) throws DataBaseException {
		return this.getBaseDao().executeMappedSQLStatement(sqlQuery);
	}

	public List findAllBeans(Class beanClass, List nId, String beanIdentifier, Order order) throws DataBaseException,
			EmptyResultSetException {
		return this.getBaseDao().findAllBeans(beanClass, nId, beanIdentifier, order);
	}

	public void transAddEditDeleteObjects(List objects, List delObjects) throws DataBaseException {
		this.getBaseDao().transAddEditDeleteObjects(objects, delObjects);
	}

	public int findBeanCounts(String beanName) throws DataBaseException {
		return this.getBaseDao().findBeanCounts(beanName);
	}

	public List<Object> findAllBeansWithDepthMapping(Class beanClass, Map propertyMap) throws DataBaseException,
			EmptyResultSetException {
		return this.getBaseDao().findAllBeansWithDepthMapping(beanClass, propertyMap);
	}

	public List<Object> findAllBeansWithDepthMapping(Class beanClass, Map propertyMap, List<Object> nOrder)
			throws DataBaseException, EmptyResultSetException {
		return this.getBaseDao().findAllBeansWithDepthMapping(beanClass, propertyMap, nOrder);
	}

	public IBaseDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(IBaseDao baseDao) {
		this.baseDao = baseDao;
	}

	public Long getMax(Class beanClass, String columnName) throws DataBaseException {
		return this.getBaseDao().getMax(beanClass, columnName);
	}


	public void setChangeInformation(Object bean) {
		try {
			if(bean!=null){
				((BaseBean)bean).setChangerId(ApplicationContext.currentUser.getId());
				((BaseBean)bean).setChangeDate(new Date());	
				((BaseBean)bean).setChanged(1); 

				

			}
		}
		catch (Exception e) {}
	}
	
	@Override
	public Season getCurrentSeason() throws DataBaseException, EmptyResultSetException {
		return this.getBaseDao().getCurrentSeason();
	}
	public Object  aggregate(String tablename,String operation,String columnName,Map <String,Object>parameters) throws DataBaseException, EmptyResultSetException {
return this.baseDao.aggregate(tablename, operation, columnName, parameters);

}
	
	@Override
	public void printReport(Map param,InputStream report) throws DataBaseException, JRException {

		 
			  

	  			JasperReport jr = JasperCompileManager.compileReport(report);
	 			
		
 	  	       Connection  connection =null;
 	  	    		   
 	  	    		   try {
 	  	    			   
  	  	    			  SessionImpl sessionImpl = (SessionImpl)getBaseDao().getMySession();

 	  	    		     connection = sessionImpl.connection();
 	  	    		     
 	  	    		  
 	  	    		  
 	  	    			   
 	  	    		   }catch (Exception e) {
						// TODO: handle exception
					}
 
 	  				logger.log(logger.getLevel().INFO,"paramters=> "+ param.toString());

				param.put(JRParameter.REPORT_LOCALE, new Locale("ar", "AE", "Arabic"));
			
				JasperPrint jp = JasperFillManager.fillReport(jr, param, connection);
				
 				JasperViewer.viewReport(jp, false, new Locale("ar", "AE", "Arabic"));
 				
	}

	@Override
	public LocalDate convertToLocalDateViaMilisecond(Date dateToConvert) {
	    return Instant.ofEpochMilli(dateToConvert.getTime())
	      .atZone(ZoneId.systemDefault())
	      .toLocalDate();
	}
	
	
	
	
	
	@Override
	public Date convertToDateViaInstant(LocalDate dateToConvert) {
	    return java.util.Date.from(dateToConvert.atStartOfDay()
	      .atZone(ZoneId.systemDefault())
	      .toInstant());
	}
	
	
	
	
	
	
	
}
