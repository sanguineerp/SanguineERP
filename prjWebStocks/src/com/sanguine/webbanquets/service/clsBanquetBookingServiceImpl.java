package com.sanguine.webbanquets.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webbanquets.dao.clsBanquetBookingDao;
import com.sanguine.webbanquets.model.clsBanquetBookingModelHd;

@Service("clsBanquetBookingService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false,value = "WebPMSTransactionManager")
public class clsBanquetBookingServiceImpl implements clsBanquetBookingService{
	@Autowired
	private clsBanquetBookingDao objBanquetBookingDao;

	
	public void funAddUpdateBanquetBookingHd(clsBanquetBookingModelHd objHdModel){
		objBanquetBookingDao.funAddUpdateBanquetBookingHd(objHdModel);
	}

	
	public clsBanquetBookingModelHd funGetBookingData(String strBookingCode,String strClientCode) {
		return objBanquetBookingDao.funGetBookingData(strBookingCode,strClientCode);
	}
	
	public void funDeleteRecord(String query,String queryType)
	{
	   objBanquetBookingDao.funDeleteRecord(query,queryType);
	}
	
}
