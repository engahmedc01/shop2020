package App.core.beans;


public class ContractorAccount extends BaseBean {
	private int id=-1;
	private int contractorId;
	private Double totalAmount;
	private Contractor contractor;
	
	public int getContractorId() {
		return contractorId;
	}
	
	
	
	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public void setContractorId(int contractorId) {
		this.contractorId = contractorId;
	}
	public Double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}



	public Contractor getContractor() {
		return contractor;
	}



	public void setContractor(Contractor contractor) {
		this.contractor = contractor;
	}
	

}



