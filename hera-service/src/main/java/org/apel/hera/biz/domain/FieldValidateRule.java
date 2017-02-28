package org.apel.hera.biz.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "robot_field_validate_rule")
public class FieldValidateRule {

	@Id
	private String id;

	private String validateType;

	private String errorMsg;

	private String validateTrigger;
	
	private String vValue;

	private String fieldId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValidateType() {
		return validateType;
	}

	public void setValidateType(String validateType) {
		this.validateType = validateType;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getValidateTrigger() {
		return validateTrigger;
	}

	public void setValidateTrigger(String validateTrigger) {
		this.validateTrigger = validateTrigger;
	}

	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

	public String getvValue() {
		return vValue;
	}

	public void setvValue(String vValue) {
		this.vValue = vValue;
	}

}
