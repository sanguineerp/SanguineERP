package com.sanguine.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.dao.clsStructureUpdateDao;

@Service("clsStructureUpdateService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class clsStructureUpdateServiceImpl implements clsStructureUpdateService {
	@Autowired
	private clsStructureUpdateDao objStructureUpdateDao;

	@Override
	public void funUpdateStructure(String clientCode, HttpServletRequest req) {
		objStructureUpdateDao.funUpdateStructure(clientCode,req);
	}

	@Override
	public void funClearTransaction(String clientCode, String[] str) {
		objStructureUpdateDao.funClearTransaction(clientCode, str);

	}

	@Override
	public void funClearMaster(String clientCode, String[] str) {
		objStructureUpdateDao.funClearMaster(clientCode, str);

	}

	@Override
	public void funClearTransactionByProperty(String clientCode, String[] str, String propName) {
		objStructureUpdateDao.funClearTransactionByProperty(clientCode, str, propName);
	}

	@Override
	public void funUpdateWebBooksStructure(String clientCode, HttpServletRequest req) {
		objStructureUpdateDao.funUpdateWebBooksStructure(clientCode,req);
	}
	@Override
	public void funClearWebBooksMaster(String clientCode, String[] str) {
		objStructureUpdateDao.funClearWebBooksMaster(clientCode, str);

	}

	@Override
	public void funClearWebBooksTransactionByProperty(String clientCode, String[] str, String propName) {
		objStructureUpdateDao.funClearWebBooksTransactionByProperty(clientCode, str, propName);
	}
	
	@Override
	public void funClearWebBooksTransaction(String clientCode, String[] str) {
		objStructureUpdateDao.funClearWebBooksTransaction(clientCode, str);

	}

}
