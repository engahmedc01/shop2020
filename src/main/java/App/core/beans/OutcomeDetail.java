 package App.core.beans;

public class OutcomeDetail extends BaseBean {
	private int id =-1;
	private String type;
	private Double amount;
	private String spenderName;
	private String notes;
	private int outcomeId;
	private Integer customerId;
	private Integer orderId;
	private int fridageId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getSpenderName() {
		return spenderName;
	}
	public void setSpenderName(String spenderName) {
		this.spenderName = spenderName;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public int getOutcomeId() {
		return outcomeId;
	}
	public void setOutcomeId(int outcomeId) {
		this.outcomeId = outcomeId;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public int getFridageId() {
		return fridageId;
	}
	public void setFridageId(int fridageId) {
		this.fridageId = fridageId;
	}

	
	

}