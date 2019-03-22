package com.sanguine.webbooks.bean;

import java.util.ArrayList;
import java.util.List;

import com.sanguine.webbooks.model.clsPettyCashEntryDtlModel;

public class clsPettyCashEntryBean {

	
	public String strVouchNo;
	
	public String dteVouchDate;
	
	public String strNarration;
	
	public double dblGrandTotal;
	
    List<clsPettyCashEntryDtlModel > listPettyCashDtl=new ArrayList<clsPettyCashEntryDtlModel>();  



	public String getDteVouchDate() {
		return dteVouchDate;
	}

	public void setDteVouchDate(String dteVouchDate) {
		this.dteVouchDate = dteVouchDate;
	}

	public String getStrNarration() {
		return strNarration;
	}

	public void setStrNarration(String strNarration) {
		this.strNarration = strNarration;
	}

	public List<clsPettyCashEntryDtlModel> getListPettyCashDtl() {
		return listPettyCashDtl;
	}

	public void setListPettyCashDtl(List<clsPettyCashEntryDtlModel> listPettyCashDtl) {
		this.listPettyCashDtl = listPettyCashDtl;
	}

	public String getStrVouchNo() {
		return strVouchNo;
	}

	public void setStrVouchNo(String strVouchNo) {
		this.strVouchNo = strVouchNo;
	}

	public double getDblGrandTotal() {
		return dblGrandTotal;
	}

	public void setDblGrandTotal(double dblGrandTotal) {
		this.dblGrandTotal = dblGrandTotal;
	}
	

	

}
