package com.sanguine.webbooks.dao;

import com.sanguine.webbooks.model.clsExpenseMasterModel;

public interface clsExpensMasterDao {
	
	public void funAddExpense(clsExpenseMasterModel exp);

	public clsExpenseMasterModel funGetExpense(String expenseCode,String clientCode);

}