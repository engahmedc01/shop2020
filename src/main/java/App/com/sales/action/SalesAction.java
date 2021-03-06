package App.com.sales.action;

import java.util.Map;

import App.com.expanses.services.IExpansesServices;
import App.com.inventory.spring.IInventoryService;
import App.com.sales.spring.ISalesService;
import App.core.action.BaseAction;

public class SalesAction extends BaseAction {
	
	private IExpansesServices expansesService;

	private ISalesService salesService;
	
	private IInventoryService inventoryService;
      public  static  Map<String, Object> request;
     public  static  Map<String, Object> response;
     public static final int CashId=200;

	public SalesAction() {
 
		salesService= (ISalesService) getSpringBeanFactory().getBean("salesService"); 
		expansesService= (IExpansesServices) getSpringBeanFactory().getBean("expansesService"); 
		inventoryService= (IInventoryService) getSpringBeanFactory().getBean("inventoryService"); 

		
		
		
		
	}
	public ISalesService getSalesService() {
		return salesService;
	}
	public void setSalesService(ISalesService salesService) {
		this.salesService = salesService;
	}
	 
	public IExpansesServices getExpansesService() {
		return expansesService;
	}
	public IInventoryService getInventoryService() {
		return inventoryService;
	}
	public void setInventoryService(IInventoryService inventoryService) {
		this.inventoryService = inventoryService;
	}
	

	

}
