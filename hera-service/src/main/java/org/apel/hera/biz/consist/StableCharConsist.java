package org.apel.hera.biz.consist;

/**
 * 固定字符和占位符常量
 * @author lijian
 *
 */
public interface StableCharConsist {

	public static final String html_COL_TEMPLATE = "\t\t\t\t\t<el-col :span=\"#span#\">\n\t\t\t\t\t\t<el-form-item label=\"#fieldName#\" prop=\"#fieldCodeName#\">\n#formInput#\t\t\t\t\t\t</el-form-item>\n\t\t\t\t\t</el-col>\n";
	public static final String PLACEHOLDER_FORMINPUT = "#formInput#";
	public static final String PLACEHOLDER_SPAN = "#span#";
	public static final String PLACEHOLDER_FIELDNAME = "#fieldName#";
	public static final String PLACEHOLDER_FIELDCODENAME = "#fieldCodeName#";
	
	public static final String HTML_ROW_START_TEMPLATE = "\t\t\t\t<el-row>\n";
	public static final String HTML_ROW_END_TEMPLATE = "\t\t\t\t</el-row>\n";
	
	public static final String HTML_EMPTY_COL_TEMPLATE = "\t\t\t\t\t<el-col :span=\"#span#\"></el-col>\n";
	public static final String HTML_SEARCH_OPTION_TEMPLATE = "\t\t\t\t\t\t\t\t<el-option label=\"#fieldName#\" value=\"#fieldCodeName#\"></el-option>\n";
	
	public static final String CONNECTION_CONFIG = "#connection_config#";
	public static final String DRUID_CONFIG = "#druid_config#";
	
	public final static String ROOT_PACKAGE = "#rootPackage#";
	public final static String IMPORT_PACKAGE = "#importPackage#";
	public final static String TABLE_NAME = "#tableName#";
	public final static String CLASS_NAME = "#domain#";
	public final static String DOMAIN_NAME = "#domainName#";
	public final static String MODULE_NAME = "#moduleName#";
	public final static String FIELDS = "#fields#";
	public final static String FIELD_METHODS = "#fieldMethods#";
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
	
	public static final String WEB_PORT = "#web_port#";
	public static final String CONTEXT_PATH = "#context_path#";
	public static final String APP_ID = "#appId#";
	public static final String CAS_URL = "#cas_url#";
	public static final String ZK_URL = "#zk_url#";
	public static final String ARTIFACT_ID = "#artifactId#";
	public static final String PACKAGE_NAME = "#packageName#";
	public static final String MVN_VERSION = "#mvnVersion#";
	public static final String CAS_INTEGRATE = "#cas_integrate#";
	public static final String CAS_INTEGRATE_VALUE = "<dependency>\n\t\t\t<groupId>org.apel</groupId>\n\t\t\t<artifactId>poseidon-security-plug</artifactId>\n\t\t\t<version>1.0.2-SNAPSHOT</version>\n\t\t</dependency>\n";
	
	public static final String REPO_LOCATION = "#repository_location#";
	public static final String NEXUS_USER_NAME = "#nexus_username#";
	public static final String NEXUS_PASSWORD = "#nexus_password#";
	//http://192.168.0.2:81/nexus/content/groups/public
	public static final String NEXUS_SNAPSHOT_ADDRESS = "#nexus_snapshot_address#";
	public static final String NEXUS_RELEASE_ADDRESS = "#nexus_release_address#";
	public static final String JDK_VERSION = "#jdk_version#";
}
