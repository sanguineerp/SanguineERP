package com.sanguine.webbanquets.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webbanquets.bean.clsBanquetSetupMasterBean;
import com.sanguine.webbanquets.bean.clsBanquetWeekendMasterBean;
import com.sanguine.webbanquets.dao.clsBanquetWeekendMasterDao;
import com.sanguine.webbanquets.model.clsBanquetWeekendMasterModel;

@Service("clsBanquetWeekendMasterService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false,value = "WebPMSTransactionManager")
public class clsBanquetWeekendMasterServiceImpl implements clsBanquetWeekendMasterService{
	@Autowired
	private clsBanquetWeekendMasterDao objBanquetWeekendMasterDao;

	@Override
	public void funAddUpdateBanquetWeekendMaster(clsBanquetWeekendMasterModel objMaster){
		objBanquetWeekendMasterDao.funAddUpdateBanquetWeekendMaster(objMaster);
	}

	@Override
	public clsBanquetWeekendMasterModel funGetBanquetWeekendMaster(String clientCode){
		return objBanquetWeekendMasterDao.funGetBanquetWeekendMaster(clientCode);
	}

	@Override
	public clsBanquetSetupMasterBean funGetWeekendMaster(String clientCode){
		return objBanquetWeekendMasterDao.funGetWeekendMaster(clientCode);
	}

	@Override
	public void funDeleteWeekendMaster(String clientCode) {
		 objBanquetWeekendMasterDao.funDeleteWeekendMaster(clientCode);
		
	}

	
	


}
