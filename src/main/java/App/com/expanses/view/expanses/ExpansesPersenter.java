package App.com.expanses.view.expanses;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.controlsfx.glyphfont.FontAwesome;
import org.hibernate.criterion.Order;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import App.com.expanses.action.ExpansesAction;
import App.com.expanses.view.addOutcome.AddOutcomeView;
import App.com.expanses.view.beans.IncomeVB;
import App.com.expanses.view.beans.OutcomeVB;
import App.core.Enum.IncomeTypesEnum;
import App.core.Enum.OutcomeTypeEnum;
import App.core.UIComponents.comboBox.ComboBoxItem;
import App.core.UIComponents.customTable.Column;
import App.core.UIComponents.customTable.CustomTable;
import App.core.UIComponents.customTable.CustomTableActions;
import App.core.applicationContext.ApplicationContext;
import App.core.beans.Contractor;
import App.core.beans.Customer;
import App.core.beans.Income;
import App.core.beans.IncomeDetail;
import App.core.beans.Loaners;
import App.core.beans.Outcome;
import App.core.beans.OutcomeDetail;
import App.core.beans.Seller;
import App.core.exception.DataBaseException;
import App.core.exception.EmptyResultSetException;
import App.core.exception.InvalidReferenceException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ExpansesPersenter extends ExpansesAction implements Initializable, CustomTableActions {

	
	Logger logger = Logger.getLogger(this.getClass().getName());	


    @FXML
    private Label initailBalance_label;

    @FXML
    private AnchorPane incomeTable_loc;

    @FXML
    private Tab income_tab;

    @FXML
    private Label initailBalanceValue_label;

    @FXML
    private HBox outcomeHeader_Pane;

    @FXML
    private Label currentBalanceValue_label;

    @FXML
    private Tab outcome_Tab;

    @FXML
    private HBox incomeHeader_Pane;

    @FXML
    private Label currentBalance_label;

    @FXML
    private AnchorPane outcomeTable_loc;

	
	private CustomTable<IncomeVB>incomeCustomTable; 
	private CustomTable<OutcomeVB>outcomeCustomTable; 
	
	
    private JFXComboBox<ComboBoxItem> incomeMonth_CB;
    private JFXComboBox<ComboBoxItem> incomeDay_CB;
    private JFXComboBox<ComboBoxItem> outcomeMonth_CB;
    private JFXComboBox<ComboBoxItem> outcomeDay_CB;

    private Label incomeTotalAmount_label;
    private Label incomeTotalAmountValue_label;

    private Label outcomeTotalAmount_label;
    private Label outcomeTotalAmountValue_label;

	private SimpleDateFormat daySdf=new SimpleDateFormat("dd");
	SimpleDateFormat monthSDF=new SimpleDateFormat("yyyy-MM");
	//----------------------------------------

	 private JFXButton addIncome_btn;
	    private JFXButton editIncome_btn;
	    private List <ComboBoxItem>incomeDays;
		private Map <Integer,Double>incomeDayAmount;
//----------------------------------------
	    private JFXButton addOutcome_btn;
	    private JFXButton editOutcome_btn;
		private List <ComboBoxItem>outcomeDays;
		private Map <Integer,Double>outcomeDayAmount;
		//----------------------------------------

		
	public ExpansesPersenter() {
		incomeDays=new ArrayList<ComboBoxItem>();
		incomeDayAmount=new HashMap<Integer, Double>();
		//----------------------------------------
		outcomeDays=new ArrayList<ComboBoxItem>();
		outcomeDayAmount=new HashMap<Integer, Double>();
	
	}
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	  	  logger.log(Level.INFO,"============================================================================================================");
	init();
		
	}
  private void init() {
	  
	  addIncome_btn=new JFXButton(this.getMessage("button.add"));
	  addIncome_btn.setGraphic(new FontAwesome().create(FontAwesome.Glyph.PLUS));
 	  addIncome_btn.getStyleClass().setAll("btn-xs","btn-primary");  
	  addIncome_btn.setOnAction(e -> {
    //	addEditOutcom();
    });	
	       
	  
	 	editIncome_btn=new JFXButton(this.getMessage("button.edit"));
	 	editIncome_btn.setGraphic(new FontAwesome().create(FontAwesome.Glyph.EDIT));
	 	editIncome_btn.setDisable(true);
	 	editIncome_btn.getStyleClass().setAll("btn-xs","btn-primary");  
	 	editIncome_btn.setOnAction(e -> {
	 		
	 		if (outcomeCustomTable.getTable().getSelectionModel().getSelectedCells()!=null)
	 		{
	 			int id =((OutcomeVB)outcomeCustomTable.getTable().getSelectionModel().getSelectedItem()).getId();
            	addEditOutcome(id);
      	
	 		}
      });	
//---------------------------------------------------------------------------------------------------------------------------------------------------------
		  addOutcome_btn=new JFXButton(this.getMessage("button.add"));
		  addOutcome_btn.setGraphic(new FontAwesome().create(FontAwesome.Glyph.PLUS));
 		  addOutcome_btn.getStyleClass().setAll("btn-xs","btn-primary");  
		  addOutcome_btn.setOnAction(e -> {
	    	addEditOutcome(0);
	    });	
		       
		  
		 	editOutcome_btn=new JFXButton(this.getMessage("button.edit"));
		 	editOutcome_btn.setGraphic(new FontAwesome().create(FontAwesome.Glyph.EDIT));
		 	editOutcome_btn.setDisable(true);
		 	editOutcome_btn.getStyleClass().setAll("btn-xs","btn-primary");  
		 	editOutcome_btn.setOnAction(e -> {
		 		if(!outcomeCustomTable.getTable().getSelectionModel().isEmpty())
		 		{
		 			int id=((OutcomeVB) outcomeCustomTable.getTable().getSelectionModel().getSelectedItem()).getId();
		 			addEditOutcome(id);
		 		}
	      	
	      });		
	  
//---------------------------------------------------------------------------------------------------------------------------------------------------------
  
	  outcome_Tab.setText(getMessage("label.safe.outcome.data"));
	  income_tab.setText(getMessage("label.safe.income.data"));

	  
	  
	  
	  //==============================================================================================================

	  
 		currentBalance_label.setText(this.getMessage("label.safe.balance"));
		initailBalance_label.setText(this.getMessage("label.safe.intialBalance"));
		 


  //==============================================================================================================
	  
		List outcomeColumns=prepareOutcomeTabelColumns();
		List incomeColumns=prepareIncomeTableColumns();
		prepareIncomeHeaderNodes();
		 prepareOutcomeHeaderNodes();

 //==============================================================================================================
		
		incomeCustomTable=new CustomTable<IncomeVB>(incomeColumns, null, null, null, null, CustomTable.tableCard, IncomeVB.class);
		outcomeCustomTable=new CustomTable<OutcomeVB>(outcomeColumns, null, null, null, this, CustomTable.tableCard, OutcomeVB.class);
		 
		
		fitToAnchorePane(incomeCustomTable.getCutomTableComponent());
		fitToAnchorePane(outcomeCustomTable.getCutomTableComponent());
	  
		incomeTable_loc.getChildren().addAll(incomeCustomTable.getCutomTableComponent());
		outcomeTable_loc.getChildren().addAll(outcomeCustomTable.getCutomTableComponent());
 //==============================================================================================================
	  fillIncomeMonthes();
	  fillOutcomeMonthes();
  //==============================================================================================================
	  
  }
  
  
  
	 
 	private void addEditOutcome(int id ) {
      



		
		
 
		this.request=new HashMap<String,Object>();
		
		request.put("outcomeDetailId", id);
		
 		AddOutcomeView form=new AddOutcomeView();
		URL u=	 getClass().getClassLoader().getResource("appResources/custom.css");

   	    String css =u.toExternalForm();
		Scene scene1= new Scene(form.getView(), 350, 420);
		Stage popupwindow=new Stage();
		popupwindow.setResizable(false);
		popupwindow.initStyle(StageStyle.TRANSPARENT);

 		scene1.getStylesheets().addAll(css); 
		popupwindow.initModality(Modality.APPLICATION_MODAL);
		      
		popupwindow.setScene(scene1);
	popupwindow.setOnHiding( ev -> {
			
    	//ComboBoxItem day=outcomeDay_CB.getSelectionModel().getSelectedItem();

// loadOutcomeData(day.getValue());			
    	fillOutcomeMonthes();    
    	//loadOutcomeData(day.getValue());		
			
		});
		      
		popupwindow.showAndWait();
		
		

	
	
		
}
	
	
	
	private List<Column> prepareOutcomeTabelColumns(){
        
        List<Column> columns=new ArrayList<Column>();
  
       Column c2=new Column(this.getMessage("label.money.amount"), "amount", "string", 10, true);
       columns.add(c2);
       
        c2=new Column(this.getMessage("label.name"), "name", "string", 20, true);
       columns.add(c2);
       
        c2=new Column(this.getMessage("label.safe.outcome.Expansekind"), "type", "string", 20, true);
       columns.add(c2);
       
        c2=new Column(this.getMessage("invoice.No"), "orderTage", "string", 20, true);
       columns.add(c2);
       
        c2=new Column(this.getMessage("label.fridage.name"), "fridageName", "string", 10, true);
       columns.add(c2);
       
        c2=new Column(this.getMessage("label.report"), "report", "string", 20, true);
       columns.add(c2);
       
        
   
   
   return columns;
   
   
   
   
   
   
   }
  
  
  
private List<Column> prepareIncomeTableColumns(){
        
        List<Column> columns=new ArrayList<Column>();
  
       Column c2=new Column(this.getMessage("label.money.amount"), "amount", "string", 15, true);
       columns.add(c2);
       
       c2=new Column(this.getMessage("label.safe.income.kind"), "type", "string", 25, true);
       columns.add(c2);
      
       
       c2=new Column(this.getMessage("label.name"), "name", "string", 25, true);
       columns.add(c2);
   
       
        c2=new Column(this.getMessage("label.report"), "notes", "string", 35, true);
       columns.add(c2);
       
        
   
   
   return columns;
   
   
   
   
   
   
   }
  
  
  
  
  private void prepareIncomeHeaderNodes() {
	 
	  
	  this.incomeDay_CB=new JFXComboBox<ComboBoxItem>();
      this.incomeMonth_CB=new JFXComboBox<ComboBoxItem>();
      incomeDay_CB.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>()
        {
            public void changed(ObservableValue<? extends Number> ov,
                    final Number oldvalue, final Number newvalue)
            {
            	
            	ComboBoxItem day=incomeDay_CB.getSelectionModel().getSelectedItem();
             if(day!=null)
             {	
            	 loadIncomeData(day.getValue());
             	double amount =incomeDayAmount.get(day.getValue());	
             	incomeTotalAmountValue_label.setText(String.valueOf(amount));
             	
             }	
			 }
        });
		
      incomeMonth_CB.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>()
      {
          public void changed(ObservableValue<? extends Number> ov,
                  final Number oldvalue, final Number newvalue)
          {
          	String key=incomeMonth_CB.getSelectionModel().getSelectedItem().getText();
        	  setIncomDayseParentKey(key);
	 	
          }
      });
		
			
	
      incomeTotalAmount_label=new Label(this.getMessage("label.total"));
  
	  this.incomeTotalAmountValue_label=new Label("0.0");
	 // this.incomeHeader_Pane.getChildren().add(addIncome_btn);
	//  this.incomeHeader_Pane.getChildren().add(editIncome_btn);

	  
	  
	    HBox tempBox=new HBox();
	      tempBox.setPrefWidth(60);
		  this.incomeHeader_Pane.getChildren().add(tempBox);

	  
	  this.incomeHeader_Pane.getChildren().add(incomeDay_CB);
	  this.incomeHeader_Pane.getChildren().add(incomeMonth_CB);
	  this.incomeHeader_Pane.getChildren().add(incomeTotalAmount_label);
	  this.incomeHeader_Pane.getChildren().add(incomeTotalAmountValue_label);

	  
	  
	  
  }
  
 
   private void  loadIncomeData(int incomeId) {
		

	   try {
		
		   Map<String,Object> map=new HashMap<String, Object>();
		   map.put("incomeId", incomeId);
	List data=	this.getBaseService().findAllBeans(IncomeDetail.class, map, null);
	List tableData=new ArrayList();
	double totalCash=0.0;
	double totalAmount=0.0;
	
		for (Iterator iterator = data.iterator(); iterator.hasNext();) {
			IncomeDetail detail = (IncomeDetail) iterator.next();
			IncomeVB row=new IncomeVB();
			
			totalAmount+=detail.getAmount();
			
		
			String name="";
			if (detail.getTypeId()==IncomeTypesEnum.INTST_PAY) {
				Seller seller=(Seller) this.getBaseService().getBean(Seller.class, detail.getSellerId());
				name=seller.getName();
			}
			else if (detail.getTypeId()==IncomeTypesEnum.IN_LOAN||detail.getTypeId()==IncomeTypesEnum.IN_PAY_LOAN)
			{
				Loaners loaner=(Loaners) this.getBaseService().getBean(Loaners.class, detail.getSellerId());
				name=loaner.getName();
			}
			else if(detail.getTypeId()==IncomeTypesEnum.CASH) {
				totalCash+=detail.getAmount();

			continue;
				
			}
			
			row.setAmount(detail.getAmount());
			row.setId(detail.getId());
			row.setName(name);
			row.setNotes(detail.getNotes());
			row.setType(detail.getType().getName());
			tableData.add(row);
		}
		
		IncomeVB row=new IncomeVB();
		row.setId(0);
		row.setAmount(totalCash);
		row.setType(this.getMessage("seller.type.cash"));
		row.setNotes(" ");
		row.setName(" ");
		tableData.add(0,row);
		this.incomeCustomTable.loadTableData(tableData);
		
		this.incomeTotalAmountValue_label.setText(String.valueOf(totalAmount));
		
		
	} catch (EmptyResultSetException e) {
		
	} catch (DataBaseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InvalidReferenceException e) {
		// TODO Auto-generated catch block
			logger.warning(" no seller found ");
	}
	   
	   
	   
	   
	   
	   
	   
	   
   }
  
 
 
   
  
  //000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
  
  private void fillOutcomeMonthes() {
	  
	  outcomeMonth_CB.getItems().clear();
	  outcomeDays.clear();
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("seasonId", ApplicationContext.season.getId());
		String parentKey="";
		 List<ComboBoxItem> outcomeMonthes=new ArrayList<ComboBoxItem>();
	  try {
		  
		  
		List result=this.getBaseService().findAllBeans(Outcome.class,map,Order.desc("outcomeDate"));
	
		List comboData=new ArrayList();
	  for (Iterator iterator = result.iterator(); iterator.hasNext();) {
			Outcome temp = (Outcome) iterator.next();
			
			String  day=daySdf.format(temp.getOutcomeDate());
			String  month=monthSDF.format(temp.getOutcomeDate());

			
			if(!parentKey.equals(month))
			{
			  int value=Integer.parseInt(month.split("-")[1]);
			  ComboBoxItem item=new ComboBoxItem(value,month); 
			 
			  outcomeMonthes.add(item);
			}
//-----------------amount --------------------------------------------------------------------------------------------------------

			outcomeDayAmount.put(temp.getId(), temp.getTotalOutcome());
			
//-----------------days --------------------------------------------------------------------------------------------------------
			parentKey	=monthSDF.format(temp.getOutcomeDate());
			ComboBoxItem boxItem=new  ComboBoxItem(temp.getId() , day, parentKey);
			outcomeDays.add(boxItem);
//-------------------------------------------------------------------------------------------------------------------------
	
		}
	  
	  this.outcomeMonth_CB.getItems().clear();
	  this.outcomeMonth_CB.getItems().setAll(outcomeMonthes);
	  outcomeMonth_CB.getSelectionModel().selectFirst();
 	  
	  String key=outcomeMonth_CB.getSelectionModel().getSelectedItem().getText();
	  setOutcomDayseParentKey(key);
	  
	  
	  
	  
	  
	  } catch (DataBaseException | EmptyResultSetException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
	  
	  
	  
  }
  
	private void fillIncomeMonthes() {
		
		
		List <ComboBoxItem>incomeMonthes=new ArrayList<ComboBoxItem>();

		
		
		try {
			String parentKey="";
			List result =this.getExpansesServices().getIncomeDates(ApplicationContext.season.getId());
			for (Iterator iterator = result.iterator(); iterator.hasNext();) {
				Income temp = (Income) iterator.next();
				
				String  day=daySdf.format(temp.getIncomeDate());
				String  month=monthSDF.format(temp.getIncomeDate());

				if(!parentKey.equals(month))
				{
				  int value=Integer.parseInt(month.split("-")[1]);
 				  ComboBoxItem item=new ComboBoxItem(value,month); 
				  incomeMonthes.add(item);
				}
//-----------------amount --------------------------------------------------------------------------------------------------------
	
				incomeDayAmount.put(temp.getId(), temp.getTotalAmount());
				
//-----------------days --------------------------------------------------------------------------------------------------------
				parentKey	=monthSDF.format(temp.getIncomeDate());
 				ComboBoxItem boxItem=new  ComboBoxItem(temp.getId() , day, parentKey);
 				incomeDays.add(boxItem);
//-------------------------------------------------------------------------------------------------------------------------
		
			}
			 
			
			
		} catch (EmptyResultSetException | DataBaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	 
	   
		  this.incomeMonth_CB.getItems().clear();
		  this.incomeMonth_CB.getItems().setAll(incomeMonthes);
		  incomeMonth_CB.getSelectionModel().selectFirst(); 
		 
		  String key=incomeMonth_CB.getSelectionModel().getSelectedItem().getText();
		  setIncomDayseParentKey(key);
	 
		 }
 
  
  private void loadOutcomeData(int outcomeId ) {
	  
	 try {
		 
		 
		 
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("outcomeId", outcomeId);
		List data=	this.getBaseService().findAllBeans(OutcomeDetail.class, map, null);
		List tableData=new ArrayList();
		double totalAmount=0.0;
		
		for (Iterator iterator = data.iterator(); iterator.hasNext();) {
			OutcomeDetail detail = (OutcomeDetail) iterator.next();
			
			OutcomeVB row=new OutcomeVB();
			row.setId(detail.getId());
			row.setAmount(detail.getAmount());
           
			String orderTag = (detail.getOrderId()==null||detail.getOrderId()==-1 )? "" :String.valueOf(detail.getOrderId()) ;

			row.setReport(detail.getNotes());
			row.setOrderTage(orderTag);
			row.setType(detail.getType().getNameAr());
			row.setFridageName(detail.getFridage().getName());
			totalAmount+=detail.getAmount();
			String name=" ";
			if(detail.getCustomerId()!=-1) {
				
	 if(detail.getTypeId()==OutcomeTypeEnum.K_L||detail.getTypeId()==OutcomeTypeEnum.K_V||detail.getTypeId()==OutcomeTypeEnum.K_S)

	 {
		    Contractor contractor=(Contractor) this.getBaseService().getBean(Contractor.class, detail.getCustomerId());
			name=contractor.getName();
		 
		 
     } else if (detail.getTypeId()==OutcomeTypeEnum.OUT_LOAN||detail.getTypeId()==OutcomeTypeEnum.OUT_PAY_LOAN) {
				
    	     Loaners loaner=(Loaners) this.getBaseService().getBean(Loaners.class, detail.getCustomerId());
			name=loaner.getName();
				
			}
     else {
        
	     Customer customer=(Customer) this.getBaseService().getBean(Customer.class, detail.getCustomerId());

    	 name =customer.getName();

     }	}
		
			 row.setName(name);
			 tableData.add(row);
			 this.outcomeTotalAmountValue_label.setText(String.valueOf(totalAmount));
		
		}
		
		
		
		this.outcomeCustomTable.loadTableData(tableData);

		
	} catch (EmptyResultSetException | DataBaseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InvalidReferenceException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
  
	  
	  
  }
  
  
  
  private void prepareOutcomeHeaderNodes() {
	 
	  
	  this.outcomeDay_CB=new JFXComboBox<ComboBoxItem>();
      this.outcomeMonth_CB=new JFXComboBox<ComboBoxItem>();
      outcomeDay_CB.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>()
        {
            public void changed(ObservableValue<? extends Number> ov,
                    final Number oldvalue, final Number newvalue)
            {
            	
            	ComboBoxItem day=outcomeDay_CB.getSelectionModel().getSelectedItem();
            	if(day!=null) {
            		double amount=outcomeDayAmount.get(day.getValue());
            		outcomeTotalAmountValue_label.setText(String.valueOf(amount));
					loadOutcomeData(day.getValue());
					
            	}
			 }
        });
		
      outcomeMonth_CB.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>()
      {
          public void changed(ObservableValue<? extends Number> ov,
                  final Number oldvalue, final Number newvalue)
          {
          	
        	
        	  if(!outcomeMonth_CB.getSelectionModel().isEmpty()) {
        		  String key=outcomeMonth_CB.getSelectionModel().getSelectedItem().getText();
              	  setOutcomDayseParentKey(key);  
        	  }
        	 
	 	
          }
      });
		
			
	
      
      
	  this.outcomeTotalAmount_label =new Label(this.getMessage("label.total"));
	  this.outcomeTotalAmountValue_label=new Label("0.0");
	    HBox tempBox=new HBox();
	      tempBox.setPrefWidth(60);
		  this.incomeHeader_Pane.getChildren().add(tempBox);

	  List nodes=new  ArrayList(Arrays.asList(addOutcome_btn,editOutcome_btn,tempBox, outcomeDay_CB,outcomeMonth_CB,outcomeTotalAmount_label,outcomeTotalAmountValue_label));

	  outcomeHeader_Pane.getChildren().addAll(nodes);
	  
  }
  
  

  private void fitToAnchorePane(Node node) {
  	
  	
  	AnchorPane.setTopAnchor(node,  0.0); 
  	AnchorPane.setLeftAnchor(node,  0.0); 
  	AnchorPane.setRightAnchor(node,  0.0); 
  	AnchorPane.setBottomAnchor(node,  0.0); 
  	
  	
  	
  }  
	
  
  private void alert(AlertType alertType,String title,String headerText,String message) {
		 Alert a = new Alert(alertType);
		 a.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
		 a.setTitle(title );
		 a.setHeaderText(headerText);
		 a.setContentText(message); 
      a.show(); 
	  
}
  

 

    void intiateInPage() {
      
        fillIncomeMonthes();
 
    }
    private void setIncomDayseParentKey( String key ) {
    	 
 		  List<ComboBoxItem> result = incomeDays.stream()  
		  .filter(day -> day.getParentKey().equals(key)) .collect(Collectors.toList());
		  
		  incomeDay_CB.getItems().setAll(result);
		  if(result.size()>0) {
			  incomeDay_CB.getSelectionModel().selectFirst();
			  int incomeId=incomeDay_CB.getSelectionModel().getSelectedItem().getValue();

			loadIncomeData(incomeId);  
		  }
		 
}
    
    //-----------------------------------------------------------------------
    private void setOutcomDayseParentKey( String key ) {
    	outcomeDay_CB.getItems().clear();
		  List<ComboBoxItem> result = outcomeDays.stream()  
		  .filter(day -> day.getParentKey().equals(key)) .collect(Collectors.toList());
		  
		  outcomeDay_CB.getItems().setAll(result);
		  if(result.size()>0) {
			  outcomeDay_CB.getSelectionModel().selectFirst();
			  int outcomeId=outcomeDay_CB.getSelectionModel().getSelectedItem().getValue();
			  loadOutcomeData(outcomeId);
			  
		  }
}


	@Override
	public void rowSelected() {
		editOutcome_btn.setDisable(false);
		
	}


	@Override
	public void rowSelected(Object o) {
		
	}
  
    
    
    
    
    
  
    
    
    
    
    
    

 	
	
    
    
    
    
}
