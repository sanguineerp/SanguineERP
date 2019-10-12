package com.sanguine.webclub.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.Table;

	@Entity
	@Table(name = "tblcategeorywisefacilitydtl")
	
	@IdClass(clsWebClubCategeoryWiseFacilityModel_ID.class)
	public class clsWebClubCategeoryWiseFacilityModel implements Serializable {
		private static final long serialVersionUID = 1L;
		
		public clsWebClubCategeoryWiseFacilityModel() {
		}
		public clsWebClubCategeoryWiseFacilityModel(String strCatCode,String strClientCode) {
			this.strCatCode = strCatCode;			
			this.strClientCode = strCatCode;
		}


		@Id
		@AttributeOverrides({ @AttributeOverride(name = "strCatCode", column = @Column(name = "strCatCode")),
				@AttributeOverride(name = "strClientCode", column = @Column(name = "strClientCode"))})
		// Variable Declaration
		
		
		@Column(name = "strCatCode")
		private String strCatCode;
		
		@Column(name = "strFacilityCode")
		private String strFacilityCode;		
		
		@Column(name = "strFacilityName")
		private String strFacilityName;

		@Column(name = "strOperationalYN")
		private String strOperationalYN;	
		
		@Column(name = "strClientCode")
		private String strClientCode;		

		// Setter-Getter Methods
		public String getStrFacilityCode() {
			return strFacilityCode;
		}

		public void setStrFacilityCode(String strFacilityCode) {
			this.strFacilityCode = strFacilityCode;
		}
		
		public String getStrCatCode() {
			return strCatCode;
		}

		public void setStrCatCode(String strCatCode) {
			this.strCatCode = (String) setDefaultValue(strCatCode, "NA");
		}

		public String getStrFacilityName() {
			return strFacilityName;
		}

		public void setStrFacilityName(String strFacilityName) {
			this.strFacilityName = (String) setDefaultValue(strFacilityName, "NA");
		}

		public String getStrOperationalYN() {
			return strOperationalYN;
		}

		public void setStrOperationalYN(String strOperationalYN) {
			this.strOperationalYN = (String) setDefaultValue(strOperationalYN, "NA");
		}		

		public String getStrClientCode() {
			return strClientCode;
		}

		public void setStrClientCode(String strClientCode) {
			this.strClientCode = (String) setDefaultValue(strClientCode, "NA");
		}
		// Function to Set Default Values
		private Object setDefaultValue(Object value, Object defaultValue) {
			if (value != null && (value instanceof String && value.toString().length() > 0)) {
				return value;
			} else if (value != null && (value instanceof Double && value.toString().length() > 0)) {
				return value;
			} else if (value != null && (value instanceof Integer && value.toString().length() > 0)) {
				return value;
			} else if (value != null && (value instanceof Long && value.toString().length() > 0)) {
				return value;
			} else {
				return defaultValue;
			}
		}
	}

