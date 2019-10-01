package com.sanguine.webbanquets.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webbanquets.model.clsEquipmentModel;
import com.sanguine.webbanquets.model.clsEquipmentModel_ID;

@Repository("clsEquipmentDao")
public class clsEquipmentDaoImpl implements clsEquipmentDao{

	@Autowired
	SessionFactory webPMSSessionFactory; 

	@Override
	@Transactional(value = "WebPMSTransactionManager")
	public void funAddUpdateEquipment(clsEquipmentModel objMaster){
		webPMSSessionFactory.getCurrentSession().saveOrUpdate(objMaster);
	}

	@Override
	@Transactional(value = "WebPMSTransactionManager")
	public clsEquipmentModel funGetEquipment(String docCode,String clientCode){
		return (clsEquipmentModel) webPMSSessionFactory.getCurrentSession().get(clsEquipmentModel.class,new clsEquipmentModel_ID(docCode,clientCode));
	}


}
