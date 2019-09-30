package com.sanguine.webbanquets.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sanguine.webbanquets.model.clsSampleModel;
import com.sanguine.webbanquets.model.clsSampleModel_ID;

@Repository("clsSampleDao")
public class clsSampleDaoImpl implements clsSampleDao{

	
	@Autowired
	SessionFactory webBanquetSessionFactory; 
	
	@Override
	public void funSaveData(String data) {
		// TODO Auto-generated method stub
		
		
		clsSampleModel obj=new clsSampleModel();
		
		obj.setTest(data);
		obj.setIntId("1");
		
		webBanquetSessionFactory.getCurrentSession().saveOrUpdate(obj);
		
	}

	
}
