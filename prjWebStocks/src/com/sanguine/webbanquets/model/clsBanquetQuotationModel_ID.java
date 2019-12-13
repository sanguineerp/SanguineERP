package com.sanguine.webbanquets.model;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
@Embeddable
@SuppressWarnings("serial")

public class clsBanquetQuotationModel_ID  implements Serializable{

	    //Variable Declaration
		@Column(name="strQuotationNo")
		private String strQuotationNo;



		@Column(name="strClientCode")
		private String strClientCode;

		public clsBanquetQuotationModel_ID(){}
		public clsBanquetQuotationModel_ID(String strQuotationNo,String strClientCode){
			this.strQuotationNo=strQuotationNo;
			this.strClientCode=strClientCode;
		}

	    //Setter-Getter Methods

		public String getStrQuotationNo() {
			return strQuotationNo;
		}
		public void setStrQuotationNo(String strQuotationNo) {
			this.strQuotationNo = strQuotationNo;
		}
		public String getStrClientCode(){
			return strClientCode;
		}
		public void setStrClientCode(String strClientCode){
			this. strClientCode = strClientCode;
		}


	//HashCode and Equals Funtions
		@Override
		public boolean equals(Object obj) {
			clsBanquetQuotationModel_ID objModelId = (clsBanquetQuotationModel_ID)obj;
			if(this.strQuotationNo.equals(objModelId.getStrQuotationNo())&& this.strClientCode.equals(objModelId.getStrClientCode())){
				return true;
			}
			else{
				return false;
			}
		}

		@Override
		public int hashCode() {
			return this.strQuotationNo.hashCode()+this.strClientCode.hashCode();
		}

}
