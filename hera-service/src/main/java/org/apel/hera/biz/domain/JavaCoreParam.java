package org.apel.hera.biz.domain;

import java.util.List;

public class JavaCoreParam {
	
	public final static String ROOT_PACKAGE = "#rootPackage#";
	public final static String IMPORT_PACKAGE = "#importPackage#";
	public final static String TABLE_NAME = "#tableName#";
	public final static String CLASS_NAME = "#domain#";
	public final static String DOMAIN_NAME = "#domainName#";
	public final static String MODULE_NAME = "#moduleName#";
	public final static String FIELDS = "#fields#";
	public final static String FIELD_METHODS = "#fieldMethods#";
	public final static String DOMAIN_TEMPLATE = "domain.template";
	public final static String SERVICE_TEMPLATE = "service.template";
	public final static String SERVICE_IMPL_TEMPLATE = "serviceImpl.template";
	public final static String REPOSITORY_TEMPLATE = "dao.template";
	public final static String CONTROLLER_TEMPLATE = "controller.template";
	public final static String I18N_TEMPLATE = "i18n.template";
	public final static String HTML_TEMPLATE = "html.template";
	public final static String JS_TEMPLATE = "js.template";
	public final static String SEARCH_FIELDS = "#search_fields#";
	public final static String FORM_FIELDS = "#form_fields#";
	public final static String FORM_FIELDS_JSON = "#formFieldsJson#";
	public final static String VALIDATE_RULES = "#validateRules#";
	public final static String DATE_FIELDS_TO_DATE = "#dateFieldsToDate#";
	public final static String DATE_FIELDS_TO_STR = "#dateFieldsToStr#";
	public final static String JQGRID_COL_NAMES = "#jqGridColNames#";
	public final static String JQGRID_COL_MODEL = "#jqGridColModel#";
	public final static String JQGRID_COL_NAMES_START = "['ID',";
	public final static String JQGRID_COL_NAMES_END = "'创建时间']";
	public final static String JQGRID_COL_MODEL_START = "\t\t\t{ name: 'id', index:'id',hidden: true},\n";
	public final static String JQGRID_COL_MODEL_END = "\t\t\t{ name: 'createDate', index:'createDate',align:'center', expType:'date',expValue:'yyyy-MM-dd',searchoptions:{dataInit:PlatformUI.defaultJqueryUIDatePick}, sortable: true ,formatter:'date',formatoptions: { srcformat: 'U', newformat: 'Y-m-d H:i:s' }}";
	public final static String JQGRID_COL_MODEL_DATE = "\t\t\t{ name: '#fieldCodeName#', index:'#fieldCodeName#',align:'center', expType:'date',expValue:'yyyy-MM-dd',searchoptions:{dataInit:PlatformUI.defaultJqueryUIDatePick}, sortable: true ,formatter:'date',formatoptions: { srcformat: 'U', newformat: 'Y-m-d H:i:s' }},\n";
	public final static String JQGRID_COL_MODEL_BOOLEAN = "\t\t\t{ name:'#fieldCodeName#', index:'#fieldCodeName#',expType:'boolean',expValue:{'true':'是','false':'否'},align:'center', formatter: PlatformUI.defaultYNFormatter, stype:'select', searchoptions:{value:'true:是;false:否'}},\n";
	public final static String JQGRID_COL_MODEL_COMMON = "\t\t\t{ name: '#fieldCodeName#', index:'#fieldCodeName#', align:'center', sortable: true},\n";
	public final static String DATE_FIELDS_TO_DATE_TEMPLATE = "\t\t\t\t\t\tdata.#fieldName# = new Date(data.#fieldName#);\n";
	public final static String DATE_FIELDS_TO_STR_TEMPLATE = "\t\t\t\t\t\tdata.#fieldName# = ExtendDate.getFormatDateByLong(data.#fieldName#.getTime(), 'yyyy-MM-dd');";
	
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
	
	public static String getTabString(int num){
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < num; i++) {
			sb.append("\t");
		}
		return sb.toString();
	}

}
