package org.apel.hera.biz.domain;

import java.util.List;

public class JavaCoreParam  extends TemplateParam{
	
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
