package App.com.expanses.dao;

import java.util.Date;
import java.util.List;

import App.core.exception.DataBaseException;
import App.core.exception.EmptyResultSetException;

public interface IExpansesDao {
	
	 public List getOutcome(Date date) throws EmptyResultSetException, DataBaseException ;
	 public List getIncome(Date date) throws EmptyResultSetException, DataBaseException ;
	 public List getIncomeMonthes(int seasonId) throws EmptyResultSetException, DataBaseException ;
	 public List getOutcomeMonthes(int seasonId) throws EmptyResultSetException, DataBaseException ;
	 public List getOutcomeDays(String month) throws EmptyResultSetException, DataBaseException ;
	 public List getIncomeDays(String month) throws EmptyResultSetException, DataBaseException ;
	 public List getLoanerDebts(int loanerId, String type) throws EmptyResultSetException, DataBaseException ;
	 public List getLoanerInstalments(int loanerId, String type) throws EmptyResultSetException, DataBaseException ;
	List getIncomeDates(int seasonId) throws EmptyResultSetException, DataBaseException;
	List inExactMatchSearchloanerName(String loanerName, String loanerType)
			throws EmptyResultSetException, DataBaseException;
}
