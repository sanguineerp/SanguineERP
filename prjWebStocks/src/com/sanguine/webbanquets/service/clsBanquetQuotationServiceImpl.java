package com.sanguine.webbanquets.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webbanquets.dao.clsBanquetQuotationDao;
import com.sanguine.webbanquets.model.clsBanquetQuotationModelHd;

@Service("clsBanquetQuotationService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false,value = "WebPMSTransactionManager")
public class clsBanquetQuotationServiceImpl implements clsBanquetQuotationService{
	@Autowired
	private clsBanquetQuotationDao objBanquetQuotationDao;

	
	public void funAddUpdateBanquetQuotationHd(clsBanquetQuotationModelHd objHdModel){
		objBanquetQuotationDao.funAddUpdateBanquetQuotationHd(objHdModel);
	}

	
	public clsBanquetQuotationModelHd funGetQuotationData(String strQuotationCode,String strClientCode) {
		return objBanquetQuotationDao.funGetQuotationData(strQuotationCode,strClientCode);
	}
	
	public void funDeleteRecord(String query,String queryType)
	{
	   objBanquetQuotationDao.funDeleteRecord(query,queryType);
	}
	
}
