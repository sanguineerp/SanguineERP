package com.sanguine.webbanquets.model;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
@Embeddable
@SuppressWarnings("serial")

public class clsBanquetBookingModel_ID  implements Serializable{

	    //Variable Declaration
		@Column(name="strBookingNo")
		private String strBookingNo;



		@Column(name="strClientCode")
		private String strClientCode;

		public clsBanquetBookingModel_ID(){}
		public clsBanquetBookingModel_ID(String strBookingNo,String strClientCode){
			this.strBookingNo=strBookingNo;
			this.strClientCode=strClientCode;
		}

	    //Setter-Getter Methods

		public String getStrBookingNo() {
			return strBookingNo;
		}
		public void setStrBookingNo(String strBookingNo) {
			this.strBookingNo = strBookingNo;
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
			clsCostCenterMasterModel_ID objModelId = (clsCostCenterMasterModel_ID)obj;
			if(this.strBookingNo.equals(objModelId.getStrCostCenterCode())&& this.strClientCode.equals(objModelId.getStrClientCode())){
				return true;
			}
			else{
				return false;
			}
		}

		@Override
		public int hashCode() {
			return this.strBookingNo.hashCode()+this.strClientCode.hashCode();
		}

}
