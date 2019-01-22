package com.sanguine.webbooks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webbooks.dao.clsExpensMasterDao;
import com.sanguine.webbooks.model.clsExpenseMasterModel;

@Service("clsExpenseMasterService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, value = "WebBooksTransactionManager")
public class clsExpenseMasterServiceImpl implements clsExpenseMasterService {
	@Autowired
	clsExpensMasterDao objExpensMasterDao;

	@Override
	public void funAddExpense(clsExpenseMasterModel exp) {

		objExpensMasterDao.funAddExpense(exp);
	}

	@Override
	public clsExpenseMasterModel funGetExpense(String expenseCode,String clientCode) {


		return objExpensMasterDao.funGetExpense(expenseCode, clientCode);

		
	}

}
