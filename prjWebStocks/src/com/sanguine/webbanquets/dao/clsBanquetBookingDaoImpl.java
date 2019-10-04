package com.sanguine.webbanquets.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webbanquets.model.clsBanquetBookingModelHd;

@Repository("clsBanquetBookingDao")
public class clsBanquetBookingDaoImpl implements clsBanquetBookingDao{

	@Autowired
	private SessionFactory webPMSSessionFactory;

	@Override
	@Transactional(value = "WebPMSTransactionManager")
	public void funAddUpdateBanquetBookingHd(clsBanquetBookingModelHd objHdModel){
		webPMSSessionFactory.getCurrentSession().saveOrUpdate(objHdModel);
	}

	/*public void funAddUpdateBanquetBookingDtl(clsBanquetBookingModelDtl objDtlModel){
		webPMSSessionFactory.getCurrentSession().saveOrUpdate(objDtlModel);
	}*/


}
