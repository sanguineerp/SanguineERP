package com.sanguine.webbanquets.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import com.sanguine.webbanquets.dao.clsCostCenterMasterDao;
import com.sanguine.webbanquets.model.clsCostCenterMasterModel;

@Service("clsCostCenterMasterService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false,value = "WebPMSTransactionManager")
public class clsCostCenterMasterServiceImpl implements clsCostCenterMasterService{
	@Autowired
	private clsCostCenterMasterDao objCostCenterMasterDao;

	@Override
	public void funAddUpdateCostCenterMaster(clsCostCenterMasterModel objMaster){
		objCostCenterMasterDao.funAddUpdateCostCenterMaster(objMaster);
	}

	@Override
	public clsCostCenterMasterModel funGetCostCenterMaster(String docCode,String clientCode){
		return objCostCenterMasterDao.funGetCostCenterMaster(docCode,clientCode);
	}



}
