package com.sanguine.webclub.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webclub.model.clsWebClubFacilityMasterModel;
import com.sanguine.webclub.model.clsWebClubFacilityMasterModel_ID;

@Repository("clsWebClubFacilityMasterDao")
public class clsWebClubFacilityMasterDaoImpl implements clsWebClubFacilityMasterDao{

	@Autowired
	private SessionFactory WebClubSessionFactory;

	@Override
	public void funAddUpdateWebClubFacilityMaster(clsWebClubFacilityMasterModel objMaster){
		WebClubSessionFactory.getCurrentSession().saveOrUpdate(objMaster);
	}

	@Override
	public clsWebClubFacilityMasterModel funGetWebClubFacilityMaster(String docCode,String clientCode){
		return (clsWebClubFacilityMasterModel) WebClubSessionFactory.getCurrentSession().get(clsWebClubFacilityMasterModel.class,new clsWebClubFacilityMasterModel_ID(docCode,clientCode));
	}	
	
}
