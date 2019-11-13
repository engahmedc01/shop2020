/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package App.com.Customer.discharge.view;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import App.com.Customer.action.CustomerBaseAction;
import App.core.Enum.CustomerTypeEnum;
import App.core.Enum.VechileTypeEnum;
import App.core.UIComponents.comboBox.ComboBoxItem;
import App.core.UIComponents.customTable.Column;
import App.core.UIComponents.customTable.CustomTable;
import App.core.beans.CustomerOrder;
import App.core.beans.Product;
import App.core.beans.Store;
import App.core.exception.DataBaseException;
import App.core.exception.EmptyResultSetException;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

/**
 *
 * @author ahmed
 */
public class InitCustomerDischargePresenter extends CustomerBaseAction  implements Initializable {
    
    
      @FXML
    private Label datePicker_Label;

    @FXML
    private FlowPane headLoc;

    @FXML
    private VBox gridLoc;
    @FXML
    private GridPane gridePanel;
    
    private CustomTable<CustomerViewBean> gride;
    private JFXDatePicker datePicker;
    public InitCustomerDischargePresenter(){}
    private   JFXTextField name;
    private   JFXTextField phone;
    private   JFXTextField address;
    private   JFXTextField grossWeight;
    private   JFXTextField count;
    private   JFXTextField nolun;
    private   JFXTextField gift;
    private   JFXTextField code;
    private   JFXTextArea notes;
    private   JFXButton saveBtn;
    private  JFXComboBox<ComboBoxItem> productTyp;
    private  JFXComboBox<ComboBoxItem> storeLocation;
    private   JFXComboBox<ComboBoxItem> vehicleTypeBox;
    private final  int tableWidth=400;
    private final  int tableHeight=1100;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	
   	init();
    	
  }
    
    
    
    private void init()  {
    	
   
		  List<Column> columns=prepareTabelColumns(); 
		  List<JFXButton> buttons=prepareButtons();
		  List<CustomerViewBean>data=loadData(new Date());
		  gride=new CustomTable<CustomerViewBean>(columns,buttons,null,data,null,CustomTable.headTableCard,CustomerViewBean.class); 
		  AnchorPane  anchorPane=gride.getCutomTableComponent();
		 
   
    anchorPane.setMaxHeight(gridLoc.getPrefHeight());
    anchorPane.setMaxWidth(gridLoc.getMaxWidth());
   
		
		  gridLoc.getChildren().setAll(anchorPane);
		  gridLoc.getParent();
		  anchorPane.getParent(); gridLoc.getChildren();
		  
		  
		  initInputGridPane();
    	
    	
    }
private void initInputGridPane() {
	

	Label typeLabel=new Label(this.getMessage("label.customer.Type"));

	Label nameLabel=new Label(this.getMessage("customer.name"));
	
	Label phoneLabel=new Label(this.getMessage("customer.phone"));
	
	Label addressLabel=new Label(this.getMessage("customer.address"));
	
	Label productIdLabel=new Label(this.getMessage("label.product"));
	
	Label grossWeightLabel=new Label(this.getMessage("label.grossWeight"));
	
	Label countLabel=new Label(this.getMessage("label.count.sabait"));
	
	Label storeIdLabel=new Label(this.getMessage("label.store.name"));
	
	Label giftLabel=new Label(this.getMessage("label.gift"));
	
	Label vehicelTypeLabel=new Label(this.getMessage("label.vehicle.type"));
	
	Label notesLabel=new Label(this.getMessage("label.notes"));
	
	Label tageLabel=new Label(this.getMessage("label.code"));
	
	Label nolunLabel=new Label(this.getMessage("label.nolun"));
	Label dateLabel=new Label(this.getMessage("label.date"));


JFXComboBox<ComboBoxItem> cutomerBox=new JFXComboBox();
cutomerBox.getStyleClass().add("comboBox");

cutomerBox.getItems().add(new ComboBoxItem(CustomerTypeEnum.kareem,this.getMessage("customer.type.karrem")));
cutomerBox.getItems().add(new ComboBoxItem(CustomerTypeEnum.mahmed,this.getMessage("customer.type.mahmed")));
cutomerBox.getItems().add(new ComboBoxItem(CustomerTypeEnum.normal,this.getMessage("customer.type.normal")));
cutomerBox.getItems().add(new ComboBoxItem(CustomerTypeEnum.purchases,this.getMessage("customer.type.purchaes")));


JFXComboBox<ComboBoxItem> vehicleTypeBox=new JFXComboBox();
vehicleTypeBox.getStyleClass().add("comboBox");

vehicleTypeBox.getItems().add(new ComboBoxItem(VechileTypeEnum.van,this.getMessage("label.vehicle.van")));
vehicleTypeBox.getItems().add(new ComboBoxItem(VechileTypeEnum.car,this.getMessage("label.vehicle.car")));


	datePicker=new JFXDatePicker();

	datePicker.getEditor().setAlignment(Pos.CENTER);
	datePicker.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
	datePicker.setConverter(new StringConverter<LocalDate>()
	{
	    private DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("dd/MM/yyyy");

	    @Override
	    public String toString(LocalDate localDate)
	    {
	        if(localDate==null)
	            return "";
	        return dateTimeFormatter.format(localDate);
	    }

	    @Override
	    public LocalDate fromString(String dateString)
	    {
	        if(dateString==null || dateString.trim().isEmpty())
	        {
	            return null;
	        }
	        return LocalDate.parse(dateString,dateTimeFormatter);
	    }
	});    
	


	 name=new JFXTextField();
	 name.getStyleClass().add("TextField");

	 
	 
	 phone=new JFXTextField();
	 phone.getStyleClass().add("TextField");
	
	 
	 address=new JFXTextField();
	 address.getStyleClass().add("TextField");
	 
	 grossWeight=new JFXTextField();
	 grossWeight.getStyleClass().add("TextField");
	 
	 count=new JFXTextField();
	 count.getStyleClass().add("TextField");
	 
	 
	 nolun=new JFXTextField();
	 nolun.getStyleClass().add("TextField");
	 
	 gift=new JFXTextField();
	 gift.getStyleClass().add("TextField");
	
	 code=new JFXTextField();
	 code.getStyleClass().add("TextField");
	 
	 notes=new JFXTextArea();
	 notes.getStyleClass().add("textArea");
	 
	productTyp=new <ComboBoxItem> JFXComboBox();
	productTyp.getStyleClass().add("comboBox");
	
	saveBtn	=new JFXButton(this.getMessage("button.save"));
	Text layoutIcon = FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.TABLE);
    layoutIcon.getStyleClass().addAll("button-icon", "layout-button-icon");    	    
    saveBtn.setGraphic(layoutIcon);
    saveBtn.setStyle("-fx-margin:100px");
    saveBtn.getStyleClass().setAll("btn","btn-primary");  
	
	
	
	
	storeLocation=new <ComboBoxItem> JFXComboBox();
	productTyp.getStyleClass().add("comboBox");

	storeLocation.getStyleClass().add("comboBox");
try {
	List products=this.getBaseService().findAllBeans(Product.class);
	for (Object p : products) {
		Product prod=(Product) p;
		productTyp.getItems().add(new ComboBoxItem(prod.getId(),prod.getName()));

	}
	
			
			  List stores=this.getBaseService().findAllBeans(Store.class);
			  for (Object it :stores) 
			  { Store store=(Store) it; storeLocation.getItems().add(new
			  ComboBoxItem(store.getId(),String.valueOf(store.getId())));
			  
			  }
			 
	
	
} catch (DataBaseException | EmptyResultSetException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}

gridePanel.setCenterShape(true);
//gridePanel.setHgap(10);
gridePanel.setAlignment(gridePanel.getAlignment().CENTER);
	


	gridePanel.add(typeLabel, 0, 0);
	gridePanel.add(cutomerBox, 1, 0);
	
	
	gridePanel.add(dateLabel, 2, 0);
	gridePanel.add(datePicker, 3, 0);
	
	
	gridePanel.add(nameLabel, 0, 1);
	gridePanel.add(name, 1, 1);

	gridePanel.add(phoneLabel, 2, 1);
	gridePanel.add(phone, 3, 1);

	gridePanel.add(addressLabel, 0, 2);
	gridePanel.add(address, 1, 2);

	gridePanel.add(productIdLabel, 2, 2);
	gridePanel.add(productTyp, 3, 2);

	gridePanel.add(grossWeightLabel, 0, 3);
	gridePanel.add(grossWeight, 1, 3);

	gridePanel.add(countLabel, 2, 3);
	gridePanel.add(count, 3, 3);

	gridePanel.add(nolunLabel, 0, 4);
	gridePanel.add(nolun, 1, 4);

	gridePanel.add(storeIdLabel, 2, 4);
	gridePanel.add(storeLocation, 3, 4);

	gridePanel.add(vehicelTypeLabel, 0, 5);
	gridePanel.add(vehicleTypeBox, 1, 5);

	gridePanel.add(tageLabel, 2, 5);
	gridePanel.add(code, 3, 5);

	gridePanel.add(notesLabel, 0, 6);
	gridePanel.add(notes, 1,6);

	gridePanel.add(saveBtn, 3,6);
	
	
	
	gridePanel.setMinWidth(900);
	gridePanel.setMinHeight(230);
	gridePanel.getPrefHeight();
	System.out.println("	gridePanel.getPrefHeight();\r\n" + 	gridePanel.getPrefHeight()			);
	
	
}
    
    
    
    private List<Column> prepareTabelColumns(){
    
    
         List<Column> columns=new ArrayList<Column>();
        Column c=new Column(" ", "chk", "chk", 5, true);
        columns.add(c);
        Column c1=new Column("orderId", "orderId", "int", 0, false);
         columns.add(c1);
        Column c2=new Column(this.getMessage("customer.name"), "customerName", "string", 20, true);
           columns.add(c2);
        Column c3=new Column(this.getMessage("label.nolun"), "nowlun", "double", 15, true);
           columns.add(c3);
        Column c4=new Column(this.getMessage("lanel.quantity"), "quantity", "double", 15, true);
           columns.add(c4);
        Column c5=new Column(this.getMessage("label.count"), "count", "int", 10, true);
           columns.add(c5);
        Column c6=new Column(this.getMessage("label.gift"), "gift", "double", 10, true);
           columns.add(c6);
        Column c7=new Column(this.getMessage("label.store.name"), "storeName", "string", 15, true);
           columns.add(c7);
        Column c8=new Column(this.getMessage("finished"), "finished", "int", 10, true);
        columns.add(c8);
    
    
    return columns;
    
    
    
    
    
    
    }
    
    
    private List<JFXButton>prepareButtons(){
    	
    	
    	JFXButton editBtn=new JFXButton(this.getMessage("button.edit"));
    	Text layoutIcon = FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.TABLE);
    	    layoutIcon.getStyleClass().addAll("button-icon", "layout-button-icon");    	    
    	    editBtn.setGraphic(layoutIcon);
    	    editBtn.getStyleClass().setAll("btn","btn-primary");                     //(2)


    	    //editBtn.getStyleClass().add("control-button");
    	    List buttons =new ArrayList<JFXButton>(Arrays.asList(new JFXButton [] {editBtn}))  ;

    	return buttons;
    	
    }


List <CustomerViewBean> loadData(Date date) {
	SimpleDateFormat sdf=new SimpleDateFormat("dd-mm-yyyy");
	Map <String,Object>m=new HashMap<String,Object>();
	

		 
	m.put("orderDate ", "to_date(30/09/2019,'DD/MM/YYYY')");
	List customerOrders=new ArrayList<>();
	List customerViewBeans=new ArrayList<>();
	try {
			Calendar d=Calendar.getInstance();
			d.set(Calendar.YEAR, 2019);
			d.set(Calendar.MONTH, 8);

			d.set(Calendar.DAY_OF_MONTH, 1);
			Date mydate=new Date(d.getTimeInMillis());
			 customerOrders = this.getCustomerService().getCustomerOrders(mydate);
				
		

		for (Object it : customerOrders) {
			CustomerOrder order=(CustomerOrder) it;
			CustomerViewBean viewBean=new CustomerViewBean();
			viewBean.setOrderId(order.getId());
			viewBean.setCount(order.getUnits());
			viewBean.setCustomerName(order.getCustomer().getName());
			viewBean.setFinished(order.getFinished());
			viewBean.setGift(order.getTips());
			viewBean.setQuantity(order.getGrossweight());
			viewBean.setNowlun(order.getNolun());
			viewBean.setProductName(order.getProduct().getName());
			viewBean.setStoreName("1");
			
			customerViewBeans.add(viewBean);

			
		}
		
		
		
	} catch (DataBaseException | EmptyResultSetException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	
	
	return customerViewBeans;
} 
    
    
}