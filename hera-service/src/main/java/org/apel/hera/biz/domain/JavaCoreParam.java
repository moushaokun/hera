package org.apel.hera.biz.domain;

import java.util.List;

public class JavaCoreParam {
	
	public final static String ROOT_PACKAGE = "#rootPackage#";
	public final static String IMPORT_PACKAGE = "#importPackage#";
	public final static String TABLE_NAME = "#tableName#";
	public final static String CLASS_NAME = "#domain#";
	public final static String DOMAIN_NAME = "#domainName#";
	public final static String FIELDS = "#fields#";
	public final static String FIELD_METHODS = "#fieldMethods#";
	public final static String DOMAIN_TEMPLATE = "domain.template";
	public final static String SERVICE_TEMPLATE = "service.template";
	public final static String SERVICE_IMPL_TEMPLATE = "serviceImpl.template";
	public final static String REPOSITORY_TEMPLATE = "dao.template";
	public final static String CONTROLLER_TEMPLATE = "controller.template";
	public final static String I18N_TEMPLATE = "i18n.template";
	
	private Domain domain;

	private List<Field> fields;

	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

}
