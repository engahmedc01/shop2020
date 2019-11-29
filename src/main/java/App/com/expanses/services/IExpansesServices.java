package App.com.expanses.services;

import java.util.Date;
import java.util.List;

import App.core.beans.LoanAccount;
import App.core.exception.DataBaseException;
import App.core.exception.EmptyResultSetException;

public interface IExpansesServices {
	 public List getOutcome(Date date) throws EmptyResultSetException, DataBaseException ;
	 public List getIncome(Date date) throws EmptyResultSetException, DataBaseException ;
	 public List getIncomeMonthes(int seasonId) throws EmptyResultSetException, DataBaseException ;
	 public List getOutcomeMonthes(int seasonId) throws EmptyResultSetException, DataBaseException ;
	 public List getOutcomeDays(String month) throws EmptyResultSetException, DataBaseException ;
	 public List getIncomeDays(String month) throws EmptyResultSetException, DataBaseException ;
	 public void outcomeTransaction(Date date,double amount, String notes, int typeId, int customerId, int orderId, int fridageId) throws DataBaseException;
	 public void loanPayTansaction(String name,Date date,double amount,int type,String notes,int fridageId)throws DataBaseException ;
		
		
	 public Double getSafeBalance(int seasonId) ;
	 public LoanAccount getLoanerAccount(String name);
}