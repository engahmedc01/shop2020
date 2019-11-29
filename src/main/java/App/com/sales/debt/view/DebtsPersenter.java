package App.com.sales.debt.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;

import App.com.sales.action.SalesAction;
import App.com.sales.debt.view.beans.PrifSellerOrderVB;
import App.com.sales.debt.view.beans.SellerDebtVB;
import App.com.sales.debt.view.beans.SellerInstalmelmentVB;
import App.com.sales.view.beans.SellerOrderDetailVB;
import App.com.sales.view.beans.SellerOrderVB;
import App.core.Enum.SellerTypeEnum;
import App.core.UIComponents.comboBox.ComboBoxItem;
import App.core.UIComponents.customTable.Column;
import App.core.UIComponents.customTable.CustomTable;
import App.core.UIComponents.customTable.CustomTableActions;
import App.core.UIComponents.customTable.PredicatableTable;
import App.core.applicationContext.ApplicationContext;
import App.core.beans.Installment;
import App.core.beans.Seller;
import App.core.beans.SellerLoanBag;
import App.core.beans.SellerOrder;
import App.core.beans.SellerOrderWeight;
import App.core.exception.DataBaseException;
import App.core.exception.EmptyResultSetException;
import App.core.exception.InvalidReferenceException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class DebtsPersenter extends SalesAction implements CustomTableActions,Initializable{
	   
	Logger logger = Logger.getLogger(this.getClass().getName());	

	
	@FXML
    private AnchorPane sellersTable_Loc;

    @FXML
    private AnchorPane sellerInstallments_loc;

    @FXML
    private Label currentDebtValue_label;

    @FXML
    private Label initailDebt_label;

    @FXML
    private Label collecteDebt_label;

    @FXML
    private Label intialDebtValue_label;

    @FXML
    private AnchorPane sellerOders_loc;

    @FXML
    private JFXComboBox<ComboBoxItem> sellerType_CB;

    @FXML
    private Label collectedDebtValue_label;

    @FXML
    private JFXTextField sellerName_TF;

    @FXML
    private Label currentDebt_label;

    @FXML
    private AnchorPane orderData_loc;
	    
	    CustomTable<SellerOrderDetailVB> orderDataCustomTable;
	    CustomTable<PrifSellerOrderVB> sellerOrdersCustomTable;
	    CustomTable<SellerInstalmelmentVB> sellerInstallmentsCustomTable;
	    PredicatableTable<SellerDebtVB> sellersPredicatableTable;

	    
	    
	    
	    
	    
	    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		init();
		loadSellersDebts();
		
		

	}
	
	@SuppressWarnings("unchecked")
	private void init() {
		
		
		List<Column> orderDataColumn=prepareSellerOrderDetailColumns();
		List<Column> prifOrderColumn=preparePrifOrderColumns();
		List<Column> sellerInstallmentColumn=prepareSellerinstallmentColumns();
		List<Column> orderDebtColumn=prepareSellerDeptColumns();
//=========================================================================================================================================
		
		sellerOrdersCustomTable=new CustomTable<PrifSellerOrderVB>(prifOrderColumn, null, null, null, this, CustomTable.tableCard, SellerOrderVB.class);
		sellersPredicatableTable=new PredicatableTable<SellerDebtVB>(orderDebtColumn, null, null, new sellrsTableActionListner(), CustomTable.tableCard, SellerOrderVB.class);
		sellerInstallmentsCustomTable=new CustomTable<SellerInstalmelmentVB>(sellerInstallmentColumn, null, null, null, null, CustomTable.tableCard, SellerOrderVB.class);
		orderDataCustomTable=new CustomTable<SellerOrderDetailVB>(orderDataColumn, null, null, null, null, CustomTable.tableCard, SellerOrderVB.class);
//=========================================================================================================================================
		fitToAnchorePane(sellerOrdersCustomTable.getCutomTableComponent());
		fitToAnchorePane(sellersPredicatableTable.getCutomTableComponent());
		fitToAnchorePane(sellerInstallmentsCustomTable.getCutomTableComponent());
		fitToAnchorePane(orderDataCustomTable.getCutomTableComponent());
		sellerOrdersCustomTable.getCutomTableComponent().setPrefSize(200, 500);
		sellersPredicatableTable.getCutomTableComponent().setPrefSize(150, 500);

//=========================================================================================================================================
		orderData_loc.getChildren().addAll(orderDataCustomTable.getCutomTableComponent());
		sellerOders_loc.getChildren().addAll(sellerOrdersCustomTable.getCutomTableComponent());
		sellersTable_Loc.getChildren().addAll(sellersPredicatableTable.getCutomTableComponent());
		sellerInstallments_loc.getChildren().addAll(sellerInstallmentsCustomTable.getCutomTableComponent());
		
//=========================================================================================================================================
		sellerName_TF.getStyleClass().add("TextField");
		sellerName_TF.setPromptText(this.getMessage("seller.name"));
		
		sellerType_CB.getStyleClass().add("comboBox");
		sellerType_CB.setPromptText(this.getMessage("seller.type"));
		sellerType_CB.getItems().add(new ComboBoxItem(SellerTypeEnum.permenant,this.getMessage("seller.type.permenant")));
		sellerType_CB.getSelectionModel().selectFirst();
		
//=========================================================================================================================================
//		orderDataCustomTable.getTable().set
		JFXTreeTableView <SellerDebtVB>table=sellersPredicatableTable.getTable();
		
		  sellerName_TF.textProperty().addListener((o, oldVal, newVal) -> {	table.setPredicate(personProp -> {
              final SellerDebtVB seller = personProp.getValue();
              return seller.getSellerName().get().contains(newVal);
          });
	});
		
//=========================================================================================================================================
	  currentDebt_label.setText(this.getMessage("label.initailDebt"));
	  initailDebt_label.setText(this.getMessage("label.currentDebt"));
	  collecteDebt_label.setText(this.getMessage("label.collecteDebt"));
	  
	  
	  double intialDebt=this.getSalesService().getSeasonStartTotalSellersLoan(ApplicationContext.season.getId());
	  double currentDebt=this.getSalesService().getSeasoncCurrentotalSellersLoan(ApplicationContext.season.getId());

	  
	  currentDebtValue_label.setText(String.valueOf(currentDebt));
	  intialDebtValue_label.setText(String.valueOf(intialDebt));
	  collectedDebtValue_label.setText(String.valueOf(intialDebt-currentDebt));
	  
		    
		  
	}

	   
	   private void loadSellersDebts() {
		
		try {
			
			List debts=this.getSalesService().getSellersDebts(ApplicationContext.currentUser.getId(),1);
		   List data=new ArrayList();
			for (Iterator iterator = debts.iterator(); iterator.hasNext();) {
				SellerLoanBag bag = (SellerLoanBag) iterator.next();
				SellerDebtVB row=new SellerDebtVB();
				row.setId(bag.getId());
				row.setSellerName(bag.getSeller().getName());

				row.setDueAmount(bag.getCurrentLoan());
				row.setTotalOrdersCost(bag.getDueLoan()+bag.getPriorLoan());
				data.add(row);
				
			}
		this.sellersPredicatableTable.loadTableData(data);
		
		
		} catch (EmptyResultSetException | DataBaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		
		
	}
	
	   
	   @SuppressWarnings("unused")
	private void loadSellerPrifOrdersDebts(int sellerLoanBagId) {
		
		   List data=new ArrayList();

			Map<String,Object> map=new HashMap<String, Object>();
			map.put("sellerLoanBagId",sellerLoanBagId);
		   
		try {
			SellerLoanBag bag=(SellerLoanBag) this.getBaseRetrievalService().getBean(SellerLoanBag.class, sellerLoanBagId);
			if(bag.getPriorLoan()>0) {
			PrifSellerOrderVB row=new PrifSellerOrderVB();
			row.setId(0);
			row.setFridageName(" ");
			row.setOrderDate(this.getMessage("label.oldDebt")+" "+bag.getNotes());
			row.setTotalOrderost(bag.getPriorLoan());
			data.add(row);
			}
			List orders=this.getBaseService().findAllBeans(SellerOrder.class, map, null);
			for (Iterator iterator = orders.iterator(); iterator.hasNext();) {
				SellerOrder order = (SellerOrder) iterator.next();
				PrifSellerOrderVB	 row=new PrifSellerOrderVB();
				row.setId(order.getId());
				row.setFridageName(order.getFridage().getName());
				row.setOrderDate(PrifSellerOrderVB.sdf.format(order.getOrderDate()));
				row.setTotalOrderost(order.getTotalCost());
			
				data.add(row);
				
			}
		this.sellerOrdersCustomTable.loadTableData(data);
		
		
		} catch (EmptyResultSetException | DataBaseException e) {
			logger.warning("not orders Found for sellerloanBagId = "+sellerLoanBagId);

		} catch (InvalidReferenceException e) {
			// TODO Auto-generated catch block
			logger.warning("not sellerLoanBag Found for sellerloanBagId = "+sellerLoanBagId);
		}
		
		
		
		
		
		
		
	}
	
	
	   
	   private void loadSellersInstallments(int sellerLoanBagId) {
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("sellerLoanBagId",sellerLoanBagId);
		try {
			
			List installments=this.getBaseService().findAllBeans(Installment.class, map, null);
		   List data=new ArrayList();
			for (Iterator iterator = installments.iterator(); iterator.hasNext();) {
				Installment installment = (Installment) iterator.next();
				SellerInstalmelmentVB row=new SellerInstalmelmentVB();
				row.setId(installment.getId());
				row.setAmount(installment.getAmount());
				row.setInstDate(SellerInstalmelmentVB.sdf.format(installment.getInstalmentDate()));
				row.setNotes(installment.getNotes());
				
			
				data.add(row);
				
			}
		this.sellerInstallmentsCustomTable.loadTableData(data);
		
		
		} catch (EmptyResultSetException | DataBaseException e) {
			logger.warning("not instalments Found for sellerloanBagId = "+sellerLoanBagId);
		}
		
		
		
		
		
		
		
	}
	
	


@SuppressWarnings("unchecked")
private void loadOrderDetail(int orderId) {
	if(orderId==0){
		orderDataCustomTable.getTable().getItems().clear();
	}
	SellerOrder order;
	try {
		order = (SellerOrder) this.getBaseRetrievalService().getBean(SellerOrder.class, orderId);
	
	List data=new ArrayList();
	
	for (Iterator iterator = order.getOrderWeights().iterator(); iterator.hasNext();) {
		SellerOrderWeight weight = (SellerOrderWeight) iterator.next();
		SellerOrderDetailVB viewBean=new SellerOrderDetailVB();
		viewBean.setAmount(weight.getAmount());
		viewBean.setCount(weight.getPackageNumber());
		viewBean.setCustomerOrderId(weight.getCustomerOrderId());
		viewBean.setCustomerOrderName(weight.getCustomerOrder().getOrderTag());
		viewBean.setGrossWeight(weight.getGrossQuantity());
		viewBean.setNetWeight(weight.getNetQuantity());
		viewBean.setProductId(weight.getProductId());
		viewBean.setProductName(weight.getProduct().getName());
		viewBean.setSellerWeightId(weight.getId());
		viewBean.setUnitePrice(weight.getUnitePrice());
		viewBean.setAmount(weight.getAmount());

		data.add(viewBean);
	}
this.orderDataCustomTable.loadTableData(data);
	
	} catch (DataBaseException | InvalidReferenceException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	
}
	



   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	private void fitToAnchorePane(Node node) {
		
		
		AnchorPane.setTopAnchor(node,  0.0); 
		AnchorPane.setLeftAnchor(node,  0.0); 
		AnchorPane.setRightAnchor(node,  0.0); 
		AnchorPane.setBottomAnchor(node,  0.0); 
		
		
		
	} 
	
	
	
	
	
    private List<Column> prepareSellerOrderDetailColumns(){
        

        List<Column> columns=new ArrayList<Column>();
  
       
     
        Column  c=new Column(this.getMessage("label.product"), "productName", "date", 20, true);
        columns.add(c);
       
      
          c=new Column(this.getMessage("label.count"), "count", "date", 10, true);
        columns.add(c);
       
          c=new Column(this.getMessage("label.grossWeight"), "grossWeight", "date", 15, true);
        columns.add(c);
       
      
        c=new Column(this.getMessage("label.netWeight"), "netWeight", "date", 15, true);
          columns.add(c);
         
          c=new Column(this.getMessage("label.invoice.unitePrice"), "unitePrice", "String", 10, true);
          columns.add(c);
     
          
          c=new Column(this.getMessage("label.money.amount"), "amount", "String", 10, true);
          columns.add(c);
          
          
          
          
          c=new Column(this.getMessage("customer.name"), "customerOrderName", "double", 30, true);
          columns.add(c);
          
          
         
        
   return columns;
   
   
   
   
   
   
   }
 
    private List<Column> prepareSellerinstallmentColumns(){
        

        List<Column> columns=new ArrayList<Column>();
  
       
     
        Column  c=new Column(this.getMessage("label.money.amount"), "amount", "double", 25, true);
        columns.add(c);
       
      
          c=new Column(this.getMessage("label.date"), "instDate", "date", 30, true);
        columns.add(c);
       
          c=new Column(this.getMessage("label.notes"), "notes", "String", 45, true);
        columns.add(c);
       
      
    
          
         
        
   return columns;
   
   
   
   
   
   
   }
 
    private List<Column> prepareSellerDeptColumns(){
        

        List<Column> columns=new ArrayList<Column>();
  
       
     
        Column  c=new Column(this.getMessage("seller.name"), "sellerName", "String", 35, true);
        columns.add(c);
       
      
          c=new Column(this.getMessage("label.money.dueAmount"), "dueAmount", "double", 25, true);
        columns.add(c);
       
          c=new Column(this.getMessage("label.total.bananaPrice"), "totalOrdersCost", "date", 40, true);
        columns.add(c);
       
  
          
          
         
        
   return columns;
   
   
   
   
   
   
   }
 
    private List<Column> preparePrifOrderColumns(){
        

        List<Column> columns=new ArrayList<Column>();
  
       
     
        Column  c=new Column(this.getMessage("label.product"), "orderDate", "date", 50, true);
        columns.add(c);
       
      
          c=new Column(this.getMessage("label.fridage.num"), "fridageName", "String", 25, true);
        columns.add(c);
       
          c=new Column(this.getMessage("label.total.amount"), "totalOrderost", "double", 25, true);
        columns.add(c);
       
      
      
         
        
   return columns;
   
   
   
   
   
   
   }

	@Override
	public void update() {

		// TODO Auto-generated method stub\'\
		
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void add() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rowSelected() {

		
		PrifSellerOrderVB item=	(PrifSellerOrderVB) this.sellerOrdersCustomTable.getTable().getSelectionModel().getSelectedItem();
		loadOrderDetail(item.getId());
		
		
	}
	@Override
	public void rowSelected(Object o) {
		// TODO Auto-generated method stub
		
	} 
	

	
	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	class sellrsTableActionListner implements CustomTableActions 
	{
		JFXTreeTableView<SellerDebtVB> mytable;
		@Override
		public void update() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void save() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void add() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void cancel() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void rowSelected() {
		
		
		
		
		
		
		
		
		
		
		}

		@SuppressWarnings("unchecked")
		@Override
		public void rowSelected(Object table) {
			
			toggelLoadingView();
			
			sellerOrdersCustomTable.getTable().getItems().clear();
			sellerInstallmentsCustomTable.getTable().getItems().clear();
			orderDataCustomTable.getTable().getItems().clear();

			
			mytable=(JFXTreeTableView<SellerDebtVB>) table;
			SellerDebtVB seller=	mytable.getSelectionModel().getSelectedItem().getValue();
			
			loadSellerPrifOrdersDebts(seller.getId());
			loadSellersInstallments(seller.getId());

			
			toggelLoadingView();

			
			
			
			
			
			
			
		} 
	    
	}
	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


	
	
	
	
	
	
	
	
	
}