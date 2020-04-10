package App.com.billing.view.invoice.pay;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.controlsfx.glyphfont.FontAwesome;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.sun.xml.internal.ws.handler.HandlerException;

import App.com.billing.action.BillingAction;
import App.com.billing.view.beans.InvoiceWeight;
import App.core.Enum.CustomerTypeEnum;
import App.core.Enum.InvoiceStatusEnum;
import App.core.Enum.ProductTypeEnum;
import App.core.UIComponents.customTable.Column;
import App.core.UIComponents.customTable.CustomTable;
import App.core.applicationContext.ApplicationContext;
import App.core.beans.CustomerOrder;
import App.core.exception.BusinessLogicViolationException;
import App.core.exception.DataBaseException;
import App.core.exception.EmptyResultSetException;
import App.core.exception.HandleErr;
import App.core.exception.InvalidReferenceException;
import App.core.validator.Validator;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import net.sf.jasperreports.engine.JRException;

public class InvoicePersenter extends BillingAction  implements Initializable {
	
	Logger logger = Logger.getLogger(this.getClass().getName());	
	
	  @FXML
	    private AnchorPane weightTable_loc;

	    @FXML
	    private JFXButton generate_btn;

	    @FXML
	    private GridPane form_grid;

	    @FXML
	    private AnchorPane root_pane;

	    @FXML
	    private Label weights_label;

	    @FXML
	    private JFXButton printInvoice_btn;
    
     private CustomerOrder invoice;


	private int typeId;


	private int invoiceId;

    private JFXSnackbar snackBar;

	private CustomTable<InvoiceWeight> invoiceWeights;

	private JFXTextField invoiceId_TF;

	private JFXTextField netWeight_TF;

	private Label netWeight_label;

	private Label lost_label;

	private JFXTextField lost_TF;

	private JFXTextField count_TF;

	private Label count_label;

	private Label grossWeight_label;

	private JFXTextField invoiceDate_TF;

	private JFXTextField productName_TF;

	private JFXTextField nolun_TF;

	private JFXTextField grossWeight_TF;

	private JFXTextField totalAmount_TF;

	private JFXTextField vehicleType_TF;
	@FXML
	private JFXTextArea notes_TA;

	private JFXTextField commision_TF;

	private JFXTextField netAmount_TF;

	private JFXTextField gift_TF;

	private Label invoiceId_label;

	private Label invoiceDate_label;

	private Label productName_label;

	private Label nolun_label;

	private Label totalAmount_label;

	private Label gift_label;

	private Label commision_label;

	private Label netAmount_label;

	private Label vehicleType_label;

 	
	public InvoicePersenter() {
		  invoiceId=(int) this.request.get("invoiceId");
		  typeId=(int) this.request.get("typeId");
//===============================================================================================================================
	
		invoiceId_TF=new JFXTextField();
		invoiceId_TF.getStyleClass().add("TextField");
		invoiceId_TF.setEditable(false);

		 netWeight_TF=new JFXTextField();
		netWeight_TF.getStyleClass().add("TextField");
		netWeight_TF.setEditable(false);

 	 
		lost_TF=new JFXTextField();
		lost_TF.getStyleClass().add("TextField");
		lost_TF.setEditable(false);
		 
		count_TF=new JFXTextField();
		count_TF.getStyleClass().add("TextField");
		count_TF.setEditable(false);
		 
		invoiceDate_TF=new JFXTextField();
		invoiceDate_TF.getStyleClass().add("TextField");
		invoiceDate_TF.setEditable(false);
		 
		productName_TF=new JFXTextField();
		productName_TF.getStyleClass().add("TextField");
		productName_TF.setEditable(false);
		 
		nolun_TF=new JFXTextField();
		nolun_TF.getStyleClass().add("TextField");
		nolun_TF.setEditable(false);
		 
		grossWeight_TF=new JFXTextField();
		grossWeight_TF.getStyleClass().add("TextField");
		grossWeight_TF.setEditable(false);
		 
	    totalAmount_TF=new JFXTextField();
		totalAmount_TF.getStyleClass().add("TextField");
		totalAmount_TF.setEditable(false);
		 
		vehicleType_TF=new JFXTextField();
		vehicleType_TF.getStyleClass().add("TextField");
		vehicleType_TF.setEditable(false);
		 
	 
		commision_TF=new JFXTextField();
		commision_TF.getStyleClass().add("TextField");
		commision_TF.setEditable(false);
		 
		netAmount_TF=new JFXTextField();
		netAmount_TF.getStyleClass().add("TextField");
		netAmount_TF.setEditable(false);
		 
		 gift_TF=new JFXTextField();
		gift_TF.getStyleClass().add("TextField");
		gift_TF.setEditable(false);

		//===============================================================================================================================
		   try{
				
				invoice=	(CustomerOrder) this.getBaseService().getBean(CustomerOrder.class, invoiceId);
			 
			} catch (DataBaseException | InvalidReferenceException e) {
				   alert(AlertType.ERROR, this.getMessage("msg.err"),
							  this.getMessage("msg.err"), 
							 this.getMessage("msg.err.general"));
				e.printStackTrace();
			}

		   
		   
//==========================End of constructor ===================================================================================================

	 	}
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
   logger.setLevel(Level.INFO);

logger.log(logger.getLevel(),"============================================================================================================");
   		

		
		
		init();
	}

 private  void init() {
 
		snackBar=new JFXSnackbar(root_pane);

	
//======================================================labels names=========================================================================
	  
 
			invoiceId_label =new Label(this.getMessage("invoice.No"));
		 	invoiceDate_label=new Label(this.getMessage("invoice.date"));
		 	productName_label=new Label(this.getMessage("label.product"));
		 	nolun_label=new Label(this.getMessage("label.nolun"));
		 	grossWeight_label=new Label(this.getMessage("label.grossWeight"));
		 	netWeight_label=new Label(this.getMessage("label.netWeight"));
		 	lost_label=new Label(this.getMessage("label.empty"));
		 	count_label=new Label(this.getMessage("label.count.sabait"));
		 	totalAmount_label=new Label(this.getMessage("label.total.amount"));
		 	gift_label=new Label(this.getMessage("label.gift"));
		 	commision_label=new Label(this.getMessage("label.commision"));
		 	netAmount_label=new Label((typeId==CustomerTypeEnum.purchases)?this.getMessage("button.purchases.price"):this.getMessage("label.netAmount"));
		 	vehicleType_label=new Label(this.getMessage("label.vehicle.type"));
	

//===============================================================================================================================
		
		
		List invoiceWeightsColumns=prepareInvoiceWeightsColumns();
		List invoiceWeightsData=loadInvoiceWeights(invoiceId,invoice.getProductId());
		invoiceWeights=new CustomTable<InvoiceWeight>(invoiceWeightsColumns, null, null, invoiceWeightsData, null, CustomTable.tableCard, InvoiceWeight.class);
		fitToAnchorePane(invoiceWeights.getCutomTableComponent());
		invoiceWeights.getCutomTableComponent().setPrefSize(100, 150);
		weightTable_loc.getChildren().addAll(invoiceWeights.getCutomTableComponent());


		
//===============================================================================================================================
	
		
		
		
		
		
		
		
		render();

		  	 
 }
 
 private List prepareInvoiceWeightsColumns() {

     

     List<Column> columns=new ArrayList<Column>();

    
  
     Column  c=new Column(this.getMessage("label.money.amount"), "amount", "double", 40, true);
     columns.add(c);
      
     c=new Column(this.getMessage("label.invoice.unitePrice"), "unitePrice", "double", 30, true);
     columns.add(c);
   
       c=new Column(this.getMessage("label.weight")+"/"+getMessage("label.count"), "weight", "double", 30, true);
     columns.add(c);
    
 
           
     
return columns;






 }


 private void render() {

	 SimpleDateFormat sdf=new SimpleDateFormat("YYYY-MM-dd"); 
	 weights_label.setText(getMessage("invoice.weightsSummary"));
	 weights_label.setMinWidth(200);
		
//========================================================================================
		 
		 printInvoice_btn.setText(getMessage("button.print"));
		 printInvoice_btn.setGraphic(new FontAwesome().create(FontAwesome.Glyph.SAVE));
		 printInvoice_btn.getStyleClass().setAll("btn","btn-info","btn-sm");                     //(2)
		 printInvoice_btn.setOnMouseClicked((new EventHandler<MouseEvent>() { 
		    	   public void handle(MouseEvent event) { 
		    		      System.out.println("printInvoice_btn has been clicked"); 
		    		      
		    		    print(invoiceId);
		    		      
		    		      
		    		      
		    		   } 
		    		}));
		 
		 
		// printInvoice_btn.setDisable(true);

		 generate_btn.setText(getMessage("button.invoice.pay"));
		generate_btn.setGraphic(new FontAwesome().create(FontAwesome.Glyph.SAVE));
	    generate_btn.getStyleClass().setAll("btn","btn-info","btn-sm");                     //(2)
	    generate_btn.setOnMouseClicked((new EventHandler<MouseEvent>() { 
	    	   public void handle(MouseEvent event) { 
	    		      System.out.println("generate_btn has been clicked"); 
	    		      
						try {
							generate();
 							alert(AlertType.CONFIRMATION, "", "", getMessage("msg.billing.invoiceHasbeenGenerated"));
							
						} catch (DataBaseException e) {
							e.printStackTrace();
					    	   alert(AlertType.ERROR, getMessage("msg.err"),getMessage("msg.err"), getMessage("msg.err.general"));

						} catch (BusinessLogicViolationException e) {
							System.out.print(getMessage("msg.error.inputData"));
							
						}
				 
	    		      
	    		      
	    		      
	    		   } 
	    		}));
			
	    
	    //========================================================================================

	 
	    //====================================================================================================================
	    int renderindex=0;
	    form_grid.getChildren().removeAll();
		 form_grid.getChildren().clear();
			 
		 form_grid.add(invoiceId_label, 0, renderindex);
		 form_grid.add(invoiceId_TF, 1, renderindex);
		 renderindex++;
		 
		 form_grid.add(invoiceDate_label, 0, renderindex);
		 form_grid.add(invoiceDate_TF, 1, renderindex);
		 renderindex++;
		 
		 form_grid.add(productName_label, 0, renderindex);
		 form_grid.add(productName_TF, 1, renderindex);
		 renderindex++;
		 
		 
		 form_grid.add(nolun_label, 0,renderindex);
		 form_grid.add(nolun_TF, 1, renderindex);
		 renderindex++;
		 
   //====================================================================================================================

	  int product_id = invoice.getProductId();

		 if(product_id==ProductTypeEnum.local_bannana)
		 {
			  
				form_grid.add(grossWeight_label,0, renderindex);
				 form_grid.add(grossWeight_TF, 1, renderindex);
				 renderindex++;
				 
				 form_grid.add(netWeight_label, 0, renderindex);
				 form_grid.add(netWeight_TF, 1, renderindex);
				 renderindex++;
				 
				 form_grid.add(lost_label, 0, renderindex);
				 form_grid.add(lost_TF, 1, renderindex);
				 renderindex++;
				 
				 form_grid.add(count_label, 0, renderindex);
				 form_grid.add(count_TF, 1, renderindex);
				 renderindex++;
 
			 
		 }
		 
		 if(product_id==ProductTypeEnum.imported)
		 {
			 
			 String s= getMessage("label.count")+ "/"   ;
			 		 s+= (product_id==ProductTypeEnum.imported)?getMessage("label.box.cartoon"):getMessage("label.box");
			 		grossWeight_label.setText( s);
			 
			// this.count_TF.setText(String.valueOf(invoice.getGrossweight()));

			 
			 
			 form_grid.add(grossWeight_label,0, renderindex);
			 form_grid.add(grossWeight_TF, 1, renderindex);
			 renderindex++;
			 
 		 }
		 
		 
		 //label.box.cartoon
		 //label.box
		 
		 
//====================================================================================================================
	 
		 form_grid.add(totalAmount_label, 0, renderindex);
		 form_grid.add(totalAmount_TF, 1, renderindex);
		 renderindex++;
		  
		 form_grid.add(gift_label, 0, renderindex);
		 form_grid.add(gift_TF, 1, renderindex);
		 renderindex++;
		 
		 form_grid.add(commision_label, 0, renderindex);
		 form_grid.add(commision_TF, 1, renderindex);
		 renderindex++;
		 
		 form_grid.add(netAmount_label, 0, renderindex);
		 form_grid.add(netAmount_TF, 1, renderindex);
		 renderindex++;
		 
		 form_grid.add(vehicleType_label, 0, renderindex);
		 form_grid.add(vehicleType_TF, 1, renderindex);
		 renderindex++;
		 
		 
	 //====================================================================================================================
	        
		//========================================================================================
		 	     double totalAmount = getCustomerOrderTotalPrice(invoice.getId());
		 	     double netWeight=getCustomerOrderNetWeight(invoice.getId());
			   	 this.invoiceId_TF.setText(String.valueOf(invoice.getId()));
				 this.invoiceDate_TF.setText(sdf.format(invoice.getOrderDate()));
				 this.productName_TF.setText(invoice.getProduct().getName());
				 this.nolun_TF.setText(String.valueOf(invoice.getNolun()));
				 this.grossWeight_TF.setText(String.valueOf(invoice.getGrossweight()));
				 this.count_TF.setText(String.valueOf(invoice.getUnits()));
		 		 this.netWeight_TF.setText(String.valueOf(netWeight));
			     this.totalAmount_TF.setText(String.valueOf(totalAmount));
		 		 this.vehicleType_TF.setText(String.valueOf(invoice.getVehicleType().getName()));
				 this.notes_TA.setText(invoice.getNotes());
				 this.gift_TF.setText(String.valueOf(invoice.getTips()));

			        double commision = Math.rint(totalAmount * ApplicationContext.Cusotmer_Order_Ration);
			        this.commision_TF.setText(String.valueOf(commision));

			        double netPrice = totalAmount - (invoice.getNolun() + commision);
		 	        this.netAmount_TF.setText(String.valueOf(netPrice));

			        

			        if (typeId==CustomerTypeEnum.purchases) {
			            commision = totalAmount - (invoice.getBuyPrice() + invoice.getNolun());
		 	            this.commision_TF.setText(String.valueOf(commision));
			            this.netAmount_TF.setText(String.valueOf(invoice.getBuyPrice()));

			        }

		  	        double ration = invoice.getGrossweight() - netWeight;
			        this.lost_TF.setText(String.valueOf(ration));
		  
	 

 		 
		 

		 
 
  }
 
 
 
 
		 protected void generate() throws DataBaseException, BusinessLogicViolationException {
			 
			 
		     
		// TODO add your handling code here:
	 
 
		
	 
		invoice.setDued(1);
		invoice.setDueDate(new Date());
		invoice.setInvoiceStatus(InvoiceStatusEnum.ARCHIVED);
  		
	
		this.getBillingService().payInvoice(invoice);
 	
		generate_btn.setDisable(true);
 	 
		
			 
			 
			
}

		 
 private void  print(int orderId) {
	 if (invoice.getProductId() == ProductTypeEnum.local_bannana) {
					

		 			try {
						 Resource r=new ClassPathResource("reports/billing/10001/invoice.jrxml"); 
logger.log(Level.INFO, "------------------------------------------------------------------------------------reports/billing/invoice.jrxml");
			 		InputStream report = null;
					try {
						report = new FileInputStream ( r.getFile().getPath());
					} catch (FileNotFoundException e) {
						
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			 		Map<String, Object> param = new HashMap<String, Object>();

					param.put("orderID", orderId);
					
		 		    param.put("SUBREPORT_DIR", new ClassPathResource("reports/billing/10001").getPath() + "/");
		 			param.put("order_id",invoice.getId());

				 	getBaseService().printReport(param, report);
			 		
					} catch (DataBaseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JRException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				} else if (invoice.getProductId()  == ProductTypeEnum.imported) { 
					
					
					

					

		 			try {
						 Resource r=new ClassPathResource("reports/billing/10002/invoicePro2.jrxml"); 
logger.log(Level.INFO, "------------------------------------------------------------------------------------reports/billing/invoicePro2.jrxml");
			 		InputStream report = null;
					try {
						report = new FileInputStream ( r.getFile().getPath());
					} catch (FileNotFoundException e) {
						
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			 		Map<String, Object> param = new HashMap<String, Object>();

					param.put("orderID", orderId);
					
		 		    param.put("SUBREPORT_DIR", new ClassPathResource("reports/billing/10002").getPath() + "/");
		 			param.put("order_id",invoice.getId());

				 	getBaseService().printReport(param, report);
			 		
					} catch (DataBaseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JRException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				  				}
			 
			 
		 }
		 
		 
		 
		 
		 
 

private List loadInvoiceWeights(int id,int productId) {
	 
	 
	 
		List data=new ArrayList();
     try {
		List orerWeights  = this.getBillingService().getCustomersOrderWeights(id);
		
		for (Iterator iterator = orerWeights.iterator(); iterator.hasNext();) {
			Object[] row = (Object[]) iterator.next();
			Integer count=Integer.parseInt(String.valueOf(row[3]));
			
			double quantity=(productId==ProductTypeEnum.imported)?count:(double)row[2];
			
			InvoiceWeight weight=new InvoiceWeight((double)row[0],(double)row[1],quantity);
			data.add(weight);
			
			
		}
	
	} catch (EmptyResultSetException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (DataBaseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
return data;
}

private double getCustomerOrderNetWeight(int orderId) {
	  
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("customerOrderId=", orderId);
	 
		Double netWeight=0.0;
		try {
			netWeight=(Double) this.getBaseRetrievalService().aggregate("SellerOrderWeight", "sum", "netQuantity", map);
		} catch (DataBaseException | EmptyResultSetException e) {
			// TODO Auto-generated catch block
			netWeight=0.0;
		}
		
		
	return netWeight;
		 
 
  
 }
 
 

private void fitToAnchorePane(Node node) {
	
	
	AnchorPane.setTopAnchor(node,  0.0); 
	AnchorPane.setLeftAnchor(node,  0.0); 
	AnchorPane.setRightAnchor(node,  0.0); 
	AnchorPane.setBottomAnchor(node,  0.0); 
	
	
	
} 
 private double getCustomerOrderTotalPrice(int orderId) {
	 
   Map<String,Object> map=new HashMap<String, Object>();
		map.put("customerOrderId=", orderId);
	 
		Double netWeight=0.0;
		try {
			netWeight=(Double) this.getBaseRetrievalService().aggregate("SellerOrderWeight", "sum", "amount", map);
		} catch (DataBaseException | EmptyResultSetException e) {
			// TODO Auto-generated catch block
			netWeight=0.0;
		}
		
		
	return netWeight;
		 
 
  
 }
 
 
	private void alert(AlertType alertType,String title,String headerText,String message) {
		 Alert a = new Alert(alertType);
		 a.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
		 a.setTitle(title );
		 a.setHeaderText(headerText);
		 a.setContentText(message); 
	    a.show(); 
	 
	}
	
	
    boolean validateForm() {
	   
        if (gift_TF.getText().isEmpty()) {
        	snackBar.show(this.getMessage("msg.err.required.gift"), 1000);

             return false;
        } 
        
        
        
             return true;
        
    }
    
}
