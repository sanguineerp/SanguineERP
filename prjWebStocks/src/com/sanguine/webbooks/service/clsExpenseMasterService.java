package com.sanguine.webbooks.service;

import com.sanguine.model.clsGroupMasterModel;
import com.sanguine.webbooks.model.clsExpenseMasterModel;
import com.sanguine.webbooks.model.clsWebBooksAccountMasterModel;

public interface clsExpenseMasterService {

	public void funAddExpense(clsExpenseMasterModel exp);
	
	public clsExpenseMasterModel funGetExpense(String expenseCode, String clientCode);

}
