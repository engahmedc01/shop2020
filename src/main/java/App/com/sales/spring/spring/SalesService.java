package App.com.sales.spring.spring;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import App.com.expanses.services.IExpansesServices;
import App.com.sales.action.SalesAction;
import App.com.sales.dao.ISalesDao;
import App.com.sales.spring.ISalesService;
import App.core.Enum.IncomeTypesEnum;
import App.core.Enum.SafeTransactionTypeEnum;
import App.core.Enum.SellerTypeEnum;
import App.core.applicationContext.ApplicationContext;
import App.core.beans.CustomerOrder;
import App.core.beans.Income;
import App.core.beans.IncomeDetail;
import App.core.beans.Installment;
import App.core.beans.Safe;
import App.core.beans.Seller;
import App.core.beans.SellerLoanBag;
import App.core.beans.SellerOrder;
import App.core.beans.SellerOrderWeight;
import App.core.exception.DataBaseException;
import App.core.exception.EmptyResultSetException;
import App.core.exception.InvalidReferenceException;
import App.core.service.IBaseRetrievalService;
import App.core.service.IBaseService;

public class SalesService implements ISalesService {
	
	ISalesDao salesDao;
	IBaseRetrievalService baseRetrievalService;
	IExpansesServices expansesServices;


	IBaseService baseService;
	Logger logger = Logger.getLogger(this.getClass().getName());	
	private ResourceBundle settingsBundle = ResourceBundle.getBundle("ApplicationSettings_ar");
	private Map<String,Object>entityDictionary;
	
	
	
	
	@Override
public void initEntityDictionary() {
	
	try {
		entityDictionary=new HashMap();
		
	}catch (Exception e) {
		// TODO: handle exception
	}
	
	
}

	
	
	public IExpansesServices getExpansesServices() {
		return expansesServices;
	}
	public void setExpansesServices(IExpansesServices expansesServices) {
		this.expansesServices = expansesServices;
	}


	
	
	public ISalesDao getSalesDao() {
		return salesDao;
	}
	public void setSalesDao(ISalesDao salesDao) {
		this.salesDao = salesDao;
	}
	public IBaseRetrievalService getBaseRetrievalService() {
		return baseRetrievalService;
	}
	public void setBaseRetrievalService(IBaseRetrievalService baseRetrievalService) {
		this.baseRetrievalService = baseRetrievalService;
	}
	public IBaseService getBaseService() {
		return baseService;
	}
	public void setBaseService(IBaseService baseService) {
		this.baseService = baseService;
	}

	/*
	 * getAllCustomersOrdersTags
	 * */
@SuppressWarnings("unchecked")
public List getAllCustomersOrdersTags(int seasonId,int fridageId,int productId,int storeId) {
	
	
	List result=new ArrayList();
	Map map=new HashMap();
	map.put("productId", productId);
	map.put("fridageId", fridageId);
	map.put("storeId", storeId);
	map.put("finished",0 );
	
	
	try {
		result=this.getBaseService().findAllBeans(CustomerOrder.class,map,null);
	} catch (DataBaseException | EmptyResultSetException e) {
	//	e.printStackTrace();
	}
return result;
	
	
	
	
	
	
	
}

public Object  aggregate(String tablename,String operation,String columnName,Map <String,Object>parameters) throws DataBaseException, EmptyResultSetException {
	
return	this.getSalesDao().aggregate(tablename, operation, columnName, parameters);
	
	
}





public List<String> getSuggestedSellerName(String searchString) {

return this.getSalesDao().getSuggestedSellerName(searchString);
}

public List getSellersOrders(Date orderDate) throws EmptyResultSetException, DataBaseException {

return this.getSalesDao(). getSellersOrders(orderDate);
}





public void saveSellerOrder(Seller seller, SellerOrder sellerOrder,double paidAmount) throws Exception {
	
	TransactionStatus status = null;

	DefaultTransactionDefinition def = new DefaultTransactionDefinition();
	def.setPropagationBehavior(TransactionDefinition.PROPAGATION_NESTED);
	def.setTimeout(Integer.parseInt(this.getSettingsBundle().getString("transactionTimeOut.base.highTimeOut")));
	status = this.getMyTransactionManager().getTransaction(def);

	
	
	try {
		
		this.initEntityDictionary();
		 seller=saveSeller(seller);

		switch(seller.getTypeId()) {
		
		case SellerTypeEnum.cash:
			sellerOrder.setSellerLoanBagId(0);
			sellerOrder.setSellerId(seller.getId());

			this.getBaseService().addBean(sellerOrder);

			IncomeDetail incomeDetail=new IncomeDetail();
			incomeDetail.setAmount(paidAmount);
			incomeDetail.setFridageId(sellerOrder.getFridageId());
			incomeDetail.setResipeintName(ApplicationContext.currentUser.getUsername());
			incomeDetail.setSellerId(seller.getId());
			incomeDetail.setTypeId(IncomeTypesEnum.CASH);
			incomeDetail.setTypeName(String .valueOf(IncomeTypesEnum.CASH));

			incomeDetail.setSellerOrderId(sellerOrder.getId());

			saveIncomeDetail(incomeDetail,sellerOrder.getOrderDate());
			
			
			break;
		case SellerTypeEnum.permenant:
			int  bagId=saveAndUpdateSellerLoanBag(seller, sellerOrder.getSeasonId(), sellerOrder.getTotalCost(), paidAmount);
			sellerOrder.setSellerLoanBagId(bagId);
			sellerOrder.setSellerId(seller.getId());
			this.getBaseService().addBean(sellerOrder);

			if(paidAmount>0)
				saveSellerInstalment(seller.getId(), sellerOrder.getId(),bagId, sellerOrder.getFridageId(), paidAmount, sellerOrder.getOrderDate(), "");
		break;
		}
		
		
		
		
		List orderdetail=new ArrayList();
		for (Iterator iterator = sellerOrder.getOrderWeights().iterator(); iterator.hasNext();) {
			SellerOrderWeight temp = (SellerOrderWeight) iterator.next();
			temp.setSellerOrderId(sellerOrder.getId());
			orderdetail.add(temp);
			
		}
		// to update seller order detail by new after saving into database and take new 
		//its new id from database
		this.getBaseService().addEditBeans(orderdetail);
		this.getMyTransactionManager().commit(status);
		logger.log(Level.INFO,"tranasction completed succfully");
		
		
	} catch (DataBaseException e) {
		this.getMyTransactionManager().rollback(status);
		logger.log(Level.SEVERE,e.getMessage());
		throw new Exception();

	}finally {
		//this.getMyTransactionManager().rollback(status);
		closeTransaction(status);

	}
	
	
	
}


 @Override
public void editeSellerOrder(Seller newSeller, SellerOrder newOrder,double paidAmount,
		SellerOrder oldOrder, int seasonId) throws Exception  {
	
	//===============================================================================================================================================

	
	if(newSeller.getTypeId()!=oldOrder.getSeller().getTypeId()) {
		
		this.deleteOldSellerOrder(oldOrder);
		this.saveSellerOrder(newSeller, newOrder, paidAmount);
		
	}
//===============================================================================================================================================
	else if(newSeller.getTypeId()==oldOrder.getSeller().getTypeId()&&newSeller.getTypeId()==SellerTypeEnum.cash) {
		
				editCashSellerOrder(newSeller, newOrder, paidAmount, oldOrder);
				}
	
	
	//=========================================================================================================================================
	else if(newSeller.getTypeId()==oldOrder.getSeller().getTypeId()&&newSeller.getTypeId()==SellerTypeEnum.permenant) {
		initEntityDictionary();
				editPermenantSellerOrder(newSeller, newOrder, paidAmount, oldOrder, seasonId);
		}
			
	
}
public void editCashSellerOrder(Seller seller, SellerOrder newOrder,double paidAmount,
		SellerOrder oldOrder) throws Exception {
	
	TransactionStatus status = null;

	DefaultTransactionDefinition def = new DefaultTransactionDefinition();
	def.setPropagationBehavior(TransactionDefinition.PROPAGATION_NESTED);
	def.setTimeout(Integer.parseInt(this.getSettingsBundle().getString("transactionTimeOut.base.highTimeOut")));
	status = this.getMyTransactionManager().getTransaction(def);
	newOrder.setId(oldOrder.getId());
	try {
		Map<String,Object> map=new HashMap<String, Object>();
		this.getExpansesServices().initEntityDictionary();
		IncomeDetail oldPaidAmountDetail=null;
		 Double oldPaidAmount = 0.0;

		try {
		    map=new HashMap<String, Object>();
			map.put("sellerOrderId", oldOrder);
			  oldPaidAmountDetail=	(IncomeDetail) this.getBaseService().getBean(IncomeDetail.class, map);
			  oldPaidAmount = oldPaidAmountDetail.getAmount();

		}catch (InvalidReferenceException e) {
			// TODO: handle exception
		}
 		if (oldPaidAmount!=paidAmount) {
 			 
 				double amount=Math.abs(oldPaidAmountDetail.getAmount()-paidAmount);
 	 			//add or subtract is relative to safe 
 	 			int operationType=(paidAmount<oldPaidAmountDetail.getAmount() )?SafeTransactionTypeEnum.add:SafeTransactionTypeEnum.subtract;
 				
 				this.getExpansesServices().changeIncomeDetailAmount(oldPaidAmountDetail, amount, operationType);
 		 
 		}
		 
	 
 				newOrder.setId(oldOrder.getId());
		
		
		
		
		
		
		
 
		
		
		
		
		
		
		
		
		
		
		
		this.getBaseService().addEditBean(newOrder);

		this.getMyTransactionManager().commit(status);

	    
	} catch (DataBaseException e) {
		this.getMyTransactionManager().rollback(status);
		logger.log(Level.SEVERE,e.getMessage());
		throw new Exception();

	}finally {
		//this.getMyTransactionManager().rollback(status);
		closeTransaction(status);

	}
	
	
	
}


public void editPermenantSellerOrder(Seller seller, SellerOrder newOrder,double paidAmount,
		SellerOrder oldOrder, int seasonId) throws Exception {
	
	TransactionStatus status = null;

	DefaultTransactionDefinition def = new DefaultTransactionDefinition();
	def.setPropagationBehavior(TransactionDefinition.PROPAGATION_NESTED);
	def.setTimeout(Integer.parseInt(this.getSettingsBundle().getString("transactionTimeOut.base.highTimeOut")));
	status = this.getMyTransactionManager().getTransaction(def);
	newOrder.setId(oldOrder.getId());
	try {
		Map<String,Object> map=new HashMap<String, Object>();
		this.getExpansesServices().initEntityDictionary();
		IncomeDetail oldPaidAmountDetail=null;
		 Double oldPaidAmount = 0.0;

		try {
		    map=new HashMap<String, Object>();
			map.put("sellerOrderId", oldOrder.getId());
			map.put("typeId", IncomeTypesEnum.INTST_PAY);
			  oldPaidAmountDetail=	(IncomeDetail) this.getBaseService().getBean(IncomeDetail.class, map);
			  oldPaidAmount = oldPaidAmountDetail.getAmount();

		}catch (InvalidReferenceException e) {
			oldPaidAmountDetail=null;
			oldPaidAmount=0.0;
			}
 			
			boolean sellerChanged=false;
//========================================if seller changed case============================================================================	
			int sellerId=oldOrder.getSellerId();
			
			if(!seller.getName().equals(oldOrder.getSeller().getName()) ) {
				
				sellerId=(int)	saveSeller(seller).getId();
				sellerChanged=true;
			 }
			
			newOrder.setSeller(null);
			newOrder.setSellerId(sellerId);
			seller.setId(sellerId);
			
//========================================handel loanBag changed case============================================================================	
			int  newLoanBagId=0;
			
			 
				
	       clearOrderFromSellerLoanBag(oldOrder.getSeller(), oldOrder.getSeasonId(), oldOrder.getTotalCost(), oldPaidAmount);
		   newLoanBagId=saveAndUpdateSellerLoanBag(seller, newOrder.getSeasonId(), newOrder.getTotalCost(), paidAmount);
		   newOrder.setSellerLoanBagId(newLoanBagId);
			 
//=====================================get installment and update it ============================================================================
			
				if(oldPaidAmountDetail!=null&&paidAmount>0.0) {
				map=new HashMap<String, Object>();
				map.put("sellerOrderId", oldOrder.getId());
				Installment installment=(Installment) this.getBaseService().getBean(Installment.class, map);
				installment.setSellerLoanBagId(newLoanBagId);
				installment.setAmount(paidAmount);
			    this.getBaseService().addEditBean(installment);
				}	
				else if(paidAmount==0.0&&oldPaidAmountDetail!=null) {
					map=new HashMap<String, Object>();
					map.put("sellerOrderId", oldOrder.getId());
					Installment installment=(Installment) this.getBaseService().getBean(Installment.class, map);
				
				    this.getBaseService().deleteBean(installment);
					
					
				}
				
			
 //===============================change income detail =========================================================================================
				
			
			  if(oldPaidAmountDetail!=null&&paidAmount==0.0)
					  
					 {
		 			 	 				
	 				  this.getBaseService().deleteBean(oldPaidAmountDetail);
					  recalculateSafeBalance(seasonId);
	 		 		}
			  else if (  oldPaidAmountDetail!=null&&oldPaidAmountDetail.getAmount()!=paidAmount&&paidAmount>0.0) {
				  
				  if(sellerChanged)
				  {  oldPaidAmountDetail.setSellerId(seller.getId());
				  oldPaidAmountDetail.setSellerOrderId(newOrder.getId());
				  	}double amount=Math.abs(oldPaidAmountDetail.getAmount()-paidAmount);
	 	 			//add or subtract is relative to safe 
	 	 			int operationType=(paidAmount<oldPaidAmountDetail.getAmount() )?SafeTransactionTypeEnum.subtract:SafeTransactionTypeEnum.add;
	 				
	 				this.getExpansesServices().changeIncomeDetailAmount(oldPaidAmountDetail, amount, operationType);
	 				
				  
				  
			  }
			  
			  else if (  oldPaidAmountDetail!=null&&oldPaidAmountDetail.getAmount()==paidAmount&&paidAmount>0.0&&sellerChanged) {
				  
				  oldPaidAmountDetail.setSellerId(seller.getId());
				  oldPaidAmountDetail.setSellerOrderId(newOrder.getId());
				  this.getBaseService().addEditBean(oldPaidAmountDetail);
				  
				  
			  }
			  
			  else if (  oldPaidAmountDetail==null&&paidAmount>0.0) {
				  
					saveSellerInstalment(seller.getId(), oldOrder.getId(),newLoanBagId, oldOrder.getFridageId(), paidAmount, oldOrder.getOrderDate(), "");

				  
				  
			  }
	//============================================ detail  ================================================================		
				List<SellerOrderWeight> orderdetail=new ArrayList<SellerOrderWeight>();
				for (Iterator iterator = newOrder.getOrderWeights().iterator(); iterator.hasNext();) {
					SellerOrderWeight temp = (SellerOrderWeight) iterator.next();
					temp.setSellerOrderId(newOrder.getId());
					orderdetail.add(temp);
					
				}
				// to update seller order detail by new after saving into database and take new 
				//its new id from database
				this.getBaseService().addEditBeans(orderdetail);
	
		
		
		this.getBaseService().addEditBean(newOrder);

		this.getMyTransactionManager().commit(status);

	    
	} catch (DataBaseException e) {
		this.getMyTransactionManager().rollback(status);
		logger.log(Level.SEVERE,e.getMessage());
		throw new Exception();

	}finally {
		//this.getMyTransactionManager().rollback(status);
		closeTransaction(status);

	}
	
	
	
}




public void recalculateSafeBalance(int seasonId) {
	Map<String,Object> map=new HashMap<String, Object>();
	map.put("incomeDetail.income.seasonId=", seasonId);
	
	Map<String,Object> map2=new HashMap<String, Object>();
	map2.put("outcomeDetail.outcome.seasonId=", seasonId);
	
	Double totalIncome=0.0;
	Double totaloutcome=0.0;

	try {
		totalIncome=(Double) aggregate("IncomeDetail incomeDetail", "sum", "amount", map);
		totalIncome=(totalIncome==null)?0.0:totalIncome;
		totaloutcome=(Double) aggregate("OutcomeDetail outcomeDetail", "sum", "amount", map2);
		totaloutcome=(totaloutcome==null)?0.0:totaloutcome;
		
		
		map2=new HashMap<String, Object>();
		map2.put("seasonId", seasonId);
		
		Safe safe=(Safe)this.getBaseService().findAllBeans(Safe.class,map2,null).get(0);
		double temp=totalIncome-totaloutcome;
		safe.setBalance(safe.getBaseAmount()+temp);
		this.getBaseService().addEditBean(safe);
		
		
		

	} catch (DataBaseException | EmptyResultSetException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
	
	
	
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




public int saveAndUpdateSellerLoanBag(Seller seller,int seasonId,double orderCost,double paidAmount) throws DataBaseException {
	double currentloan=0;
	SellerLoanBag bag=findSellerLoanBag(seller.getId(), seasonId);
	
	bag.setDueLoan(bag.getDueLoan()+orderCost);;
	bag.setPaidAmount(bag.getPaidAmount()+paidAmount);
	currentloan=(bag.getPriorLoan()+bag.getDueLoan())-paidAmount;
	bag.setCurrentLoan(currentloan);
		
	
	
		this.getBaseService().addEditBean(bag);
//---------------------------------------------------------------------------
		String key=hashDisctionaryEntityKey(SellerLoanBag.class, bag.getId());
		entityDictionary.put(key, bag);
//---------------------------------------------------------------------------

	return bag.getId();
	}
public int clearOrderFromSellerLoanBag(Seller seller,int seasonId,double orderCost,double paidAmount) throws DataBaseException {
	double currentloan=0;
	SellerLoanBag bag= findSellerLoanBag(seller.getId(), seasonId);
	
	bag.setDueLoan(bag.getDueLoan()-orderCost);;
	bag.setPaidAmount(bag.getPaidAmount()-paidAmount);
	currentloan=(bag.getPriorLoan()+bag.getDueLoan())-paidAmount;
	bag.setCurrentLoan(currentloan);
		
	
	
		this.getBaseService().addEditBean(bag);
 //---------------------------------------------------------------------------
		String key=hashDisctionaryEntityKey(SellerLoanBag.class, bag.getId());
		entityDictionary.put(key, bag);
 //---------------------------------------------------------------------------

	
	return bag.getId();
	
	
}
public Seller  saveSeller(Seller seller) throws DataBaseException, InvalidReferenceException {
	
	

	
	
	if (seller.getTypeId()==SellerTypeEnum.permenant) {
	try {
		Map <String,Object>m=new HashMap<String,Object>();
		m.put("name", seller.getName());
		seller=(Seller) this.getBaseService().findAllBeans(Seller.class, m, null).get(0);
		
		
		return seller;
	} catch (DataBaseException | EmptyResultSetException e) {
		this.getBaseService().addBean(seller);

		
	}
	}
	
	else if (seller.getTypeId()==SellerTypeEnum.cash) {
		
		seller=(Seller) this.getBaseService().getBean(Seller.class, SalesAction.CashId);

		
		
	}
	

	
	return seller;
	
}


public void saveIncomeDetail(IncomeDetail incomeDetail,Date date) throws DataBaseException {
	
	
	Income income=findIncome(date);
	incomeDetail.setIncomeId(income.getId());
	incomeDetail.setResipeintName(ApplicationContext.currentUser.getUsername());

	income.setTotalAmount(income.getTotalAmount()+incomeDetail.getAmount());
	this.getBaseService().addEditBean(incomeDetail);

	this.getBaseService().addEditBean(income);

	
	
	
	
}
	public  Income findIncome(Date date) {
		
		
		Income income=new Income();
		income.setIncomeDate(date);
		income.setTotalAmount(0.0);
	 
		
		try {
			
			income=	(Income) this.getSalesDao().getIncome(date).get(0);
			
			return income;
		} catch (DataBaseException | EmptyResultSetException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
			logger.log(logger.getLevel().INFO, "error.emptyRS incomeDate of date "+date.toString());
		}
		
		
		
		try {
			this.getBaseService().addBean(income);
		} catch (DataBaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return income;
		
		
		
		
		
		
		
		
		
	}
	public Income updateincome(int id ,double amount) throws DataBaseException, InvalidReferenceException {
	
	
	Income income=(Income) this.getBaseService().getBean(Income.class, id);
	income.setTotalAmount(income.getTotalAmount()+amount);
	
	return income;
	
	
}


public SellerLoanBag findSellerLoanBag(int sellerId,int seasonId) throws DataBaseException {
	Map<String,Object> m=new HashMap<String,Object>();
	m.put("sellerId", sellerId);
	m.put("seasonId", seasonId);
	
	
 	try {
		 SellerLoanBag bag=(SellerLoanBag) this.getBaseService().findAllBeans(SellerLoanBag.class,m,null ).get(0);
		 String key=hashDisctionaryEntityKey(SellerLoanBag.class, bag.getId());
		 bag=(entityDictionary.get(key)!=null)?(SellerLoanBag) entityDictionary.get(key):bag;
		  		return bag;

	} catch (DataBaseException | EmptyResultSetException e) {
		// TODO Auto-generated catch block
		//e.printStackTrace();
	}
	SellerLoanBag bag=new SellerLoanBag();
	bag.setPriorLoan(0.0);
	bag.setSellerId(sellerId);
	bag.setSeasonId(ApplicationContext.season.getId());
	
	
	this.baseService.addBean(bag);
	 String key=hashDisctionaryEntityKey(SellerLoanBag.class, bag.getId());
	 entityDictionary.put(key, bag);
	
	return bag;
	
	
	
	
	
	
}
@Override
public void saveSellerInstalment(int sellerId,int sellerOrderId,int sellerLoanBagId ,int fridageId,double amount,Date date,String notes) throws DataBaseException {
	
	Installment installment=new Installment();
	installment.setInstalmentDate(date);
	installment.setAmount(amount);
	installment.setSellerLoanBagId(sellerLoanBagId);
	installment.setNotes(notes);
	if(sellerOrderId!=0)
	{
		installment.setSellerOrderId(sellerOrderId);
		
	}
		this.getBaseService().addBean(installment);
		IncomeDetail incomeDetail=new IncomeDetail();
		incomeDetail.setAmount(amount);
		incomeDetail.setFridageId(fridageId);
		incomeDetail.setResipeintName("");
		incomeDetail.setNotes(notes);
		incomeDetail.setSellerId(sellerId);
		incomeDetail.setTypeId(IncomeTypesEnum.INTST_PAY);
		incomeDetail.setTypeName(String.valueOf(IncomeTypesEnum.INTST_PAY));
		incomeDetail.setInstallmentId(installment.getId());
		if(sellerOrderId!=0)
		{
			incomeDetail.setSellerOrderId(sellerOrderId);
			
		}
		saveIncomeDetail(incomeDetail, date);
	
}

private void deleteOldSellerOrder(SellerOrder order) throws DataBaseException  {
	
	TransactionStatus status = null;

	DefaultTransactionDefinition def = new DefaultTransactionDefinition();
	def.setPropagationBehavior(TransactionDefinition.PROPAGATION_NESTED);
	def.setTimeout(Integer.parseInt(this.getSettingsBundle().getString("transactionTimeOut.base.highTimeOut")));
	status = this.getMyTransactionManager().getTransaction(def);

	
	
	try {
		Seller seller=order.getSeller();
		switch(seller.getTypeId()) {
		
		case SellerTypeEnum.cash:
			
			Map<String,Object> map=new HashMap<String, Object>();

			map.put("sellerOrderId", order.getId());
			try {
			IncomeDetail incomeDetail= (IncomeDetail)this.getBaseRetrievalService().findBean(IncomeDetail.class, map);
			this.getBaseService().deleteBean(incomeDetail);
			recalculateSafeBalance(order.getSeasonId());
			
			}catch (InvalidReferenceException e) {
				// TODO: handle exception
			}
			
		
			this.getBaseService().deleteBean(order);

			break;
		case SellerTypeEnum.permenant:
		     map=new HashMap<String, Object>();

			map.put("sellerOrderId", order.getId());
			try {
			IncomeDetail incomeDetail= (IncomeDetail)this.getBaseRetrievalService().findBean(IncomeDetail.class, map);
			
			
			this.getBaseService().deleteBean(incomeDetail);
			recalculateSafeBalance(order.getSeasonId());
			
			}catch (InvalidReferenceException e) {
				// TODO: handle exception
			}
			
			
			//=========================delete installment============================
			try {
				Installment installment= (Installment)this.getBaseRetrievalService().findBean(Installment.class, map);
						
				this.getBaseService().deleteBean(installment);
				
				}catch (InvalidReferenceException e) {
					// TODO: handle exception
				}
			
			//================================recalculate loan bag after deleting order======================
			int sellerId=order.getSellerId();
			int seasonId=order.getSeasonId();
			this.getBaseService().deleteBean(order);
			recalculeSellerLoanBag(seasonId, sellerId);
			
		
			
			break;
		}
		

		this.getMyTransactionManager().commit(status);
		logger.log(Level.INFO,"tranasction completed succfully");
		
		
	} catch (DataBaseException e) {
		this.getMyTransactionManager().rollback(status);
		logger.log(Level.SEVERE,e.getMessage());
		throw new DataBaseException("err.deleting.sellerOrder");

	}finally {		//this.getMyTransactionManager().rollback(status);
		closeTransaction(status);

	}
	
	
	
	
}




public void recalculeSellerLoanBag(int seasonId,int sellerId) {
	Map<String,Object> map=new HashMap<String, Object>();
	map.put("seasonId=", seasonId);
	map.put("sellerId=", sellerId);
	
	
	
	Double ordersCost=0.0;
	Double totalPaidAmount=0.0;

	try {
		
		SellerLoanBag bag= findSellerLoanBag(sellerId, seasonId);
		Map<String,Object> map2=new HashMap<String, Object>();
		map2.put("sellerLoanBagId=", bag.getId());
		
		
		ordersCost=(Double) aggregate("SellerOrder", "sum", "totalCost", map);
		ordersCost=(ordersCost==null)?0.0:ordersCost;
		
		
		
		
		totalPaidAmount=(Double) aggregate("Installment", "sum", "amount", map2);
		totalPaidAmount=(totalPaidAmount==null)?0.0:totalPaidAmount;
		
	
		double temp=(bag.getPriorLoan()+ordersCost)-totalPaidAmount;
		bag.setPaidAmount(totalPaidAmount);
		bag.setDueLoan(ordersCost);
		bag.setCurrentLoan(temp);
		
		this.getBaseService().addEditBean(bag);
		
		
		

	} catch (DataBaseException | EmptyResultSetException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
	
	
	
}







    public double getSellerLoan(int sellerId, int seasonId) {
    	Map<String,Object> map=new HashMap<String, Object>();
    	map.put("sellerId", sellerId);
    	map.put("seasonId", seasonId);

    	try {
			SellerLoanBag bag= (SellerLoanBag) this.getBaseService().getBean(SellerLoanBag.class, map);
		return	bag.getCurrentLoan();
			
    	} catch (DataBaseException | InvalidReferenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	
    	
    	return 0.0;
    }
    public double   getSellerOrdersTotalPrice(int sellerId, int seasonId) {
    	Map<String,Object> map=new HashMap<String, Object>();
    	map.put("sellerId", sellerId);
    	map.put("seasonId", seasonId);

    	try {
			return (double) this.getBaseRetrievalService().aggregate("SellerOrder", "sum", "totalCost", map);
		} catch (DataBaseException | EmptyResultSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	
    	
		return 0.0;
		
    }
    
	 public List getSellersDebts( int seasonId,int active) throws EmptyResultSetException, DataBaseException {
	 return this.salesDao.getSellersDebts(seasonId,active);
			 
	 }
public ResourceBundle getSettingsBundle() {
	return settingsBundle;
}



public double getSeasonStartTotalSellersLoan(int seasonId) {
	
	Map<String,Object> map=new HashMap<String, Object>();
	map.put("seasonId=", seasonId);
	
	try {
		double value= (double) this.aggregate("SellerLoanBag", "sum", "priorLoan", map);
		return value;
	} catch (DataBaseException | EmptyResultSetException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	return 0.0;
	
	
	
}





public double getSeasoncCurrentotalSellersLoan(int seasonId) {
	
	Map<String,Object> map=new HashMap<String, Object>();
	map.put("seasonId=", seasonId);
	
	try {
		double value= (double) this.aggregate("SellerLoanBag", "sum", "currentLoan", map);
		return value;
	} catch (DataBaseException | EmptyResultSetException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	return 0.0;
	
	
	
}
@Override
public SellerLoanBag getSellerLoanBag(int sellerId,int seasonId) throws DataBaseException {
	Map<String,Object> map=new HashMap<String, Object>();
	map.put("sellerId", sellerId);
	map.put("seasonId", seasonId);
	   SellerLoanBag loan=null;
			try {
				loan = (SellerLoanBag) this.getBaseService().getBean(SellerLoanBag.class, map);
			} catch ( InvalidReferenceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	
	
	return loan;
	
	
	
}

	/*
	 * @Override public void payOffSellerOrder(int sellerId,int sellerOrderId,double
	 * amount,Date date,String notes,int seasonId,int fridageId) throws
	 * DataBaseException {
	 * 
	 * 
	 * SellerLoanBag loanBag=getSellerLoanBag(sellerId, seasonId);
	 * 
	 * Installment inst=new Installment(); inst.setSellerLoanBagId(loanBag.getId());
	 * inst.setSellerOrderId(sellerOrderId); inst.setAmount(amount);
	 * inst.setInstalmentDate(date); inst.setNotes(notes);
	 * 
	 * 
	 * 
	 * 
	 * TransactionStatus status = null;
	 * 
	 * DefaultTransactionDefinition def = new DefaultTransactionDefinition();
	 * def.setPropagationBehavior(TransactionDefinition.PROPAGATION_NESTED);
	 * def.setTimeout(Integer.parseInt(this.getSettingsBundle().getString(
	 * "transactionTimeOut.base.highTimeOut"))); status =
	 * this.getMyTransactionManager().getTransaction(def);
	 * 
	 * 
	 * try {
	 * 
	 * this.getBaseService().addBean(inst); this.saveSellerInstalment(sellerId,
	 * sellerOrderId, sellerLoanBagId, fridageId, amount, date, notes);
	 * this.getExpansesServices().incomeTransaction(date, amount, notes,
	 * IncomeTypesEnum.INTST_PAY, sellerId, inst.getId(), fridageId, seasonId); }
	 * 
	 * 
	 * 
	 * catch(DataBaseException dbEx) {
	 * this.getMyTransactionManager().rollback(status); throw new
	 * DataBaseException(dbEx.getMessage());
	 * 
	 * } finally { this.closeTransaction(status); }
	 * 
	 * 
	 * 
	 * }
	 * 
	 */


private String hashDisctionaryEntityKey(Class<?>beanClass, Integer identifier) {
	
	String key="";
	
	key=beanClass.getName()+identifier;
	
	
	
	
	
	
	return key;
	
	
	
	
	
	
	
	
	
}



@Override
public List getSellersLoanSummary(Date fromDate, Date toDate, int seasonId)
		throws EmptyResultSetException, DataBaseException {
 	return this.getSalesDao().getSellersLoanSummary(fromDate, toDate, seasonId);
}

}
