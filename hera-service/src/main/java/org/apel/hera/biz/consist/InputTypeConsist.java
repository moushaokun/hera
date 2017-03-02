package org.apel.hera.biz.consist;

public enum InputTypeConsist {
	
	TEXT("text") {
		@Override
		public String createFormInput() {
			return "\t\t\t\t\t\t\t<el-input v-model=\"form.#fieldCodeName#\" size=\"small\" placeholder=\"请输入#fieldName#\"></el-input>\n";
		}
	},PASSWORD("password") {
		@Override
		public String createFormInput() {
			return "\t\t\t\t\t\t\t<el-input v-model=\"form.#fieldCodeName#\" type=\"password\" size=\"small\" placeholder=\"请输入#fieldName#\"></el-input>\n";
		}
	},SELECT("select") {
		@Override
		public String createFormInput() {
			return "\t\t\t\t\t\t\t<el-select size=\"small\" v-model=\"form.#fieldCodeName#\" placeholder=\"请选择\">\n\t\t\t\t\t\t\t\t<el-option label=\"选项一\" value=\"选项一\"></el-option>\n\t\t\t\t\t\t\t</el-select>\n";
		}
	},SWITCH("switch") {
		@Override
		public String createFormInput() {
			return "\t\t\t\t\t\t\t<el-switch on-text=\"是\" off-text=\"否\" v-model=\"form.#fieldCodeName#\"></el-switch>\n";
		}
	},DATE("date") {
		@Override
		public String createFormInput() {
			return "\t\t\t\t\t\t\t<el-date-picker size=\"small\" v-model=\"form.#fieldCodeName#\" type=\"datetime\" placeholder=\"选择日期\"></el-date-picker>\n";
		}
	},
	CHECKBOX("checkbox") {
		@Override
		public String createFormInput() {
			return "\t\t\t\t\t\t\t<el-input v-model=\"form.#fieldCodeName#\" size=\"small\" placeholder=\"请输入#fieldName#\"></el-input>\n";
		}
	},RADIO("radio") {
		@Override
		public String createFormInput() {
			return "\t\t\t\t\t\t\t<el-input v-model=\"form.#fieldCodeName#\" size=\"small\" placeholder=\"请输入#fieldName#\"></el-input>\n";
		}
	},TEXTAREA("textarea") {
		@Override
		public String createFormInput() {
			return "\t\t\t\t\t\t\t<el-input v-model=\"form.#fieldCodeName#\" type=\"textarea\" size=\"small\" placeholder=\"请输入#fieldName#\"></el-input>\n";
		}
	},NUMBER("number") {
		@Override
		public String createFormInput() {
			return "\t\t\t\t\t\t\t<el-input-number size=\"small\" v-model=\"form.#fieldCodeName#\" :min=\"0\"></el-input-number>\n";
		}
	};
	
	
	private String name;
	
	public static final String html_COL_TEMPLATE = "\t\t\t\t\t<el-col :span=\"#span#\">\n\t\t\t\t\t\t<el-form-item label=\"#fieldName#\" prop=\"#fieldCodeName#\">\n#formInput#\t\t\t\t\t\t</el-form-item>\n\t\t\t\t\t</el-col>\n";
	public static final String PLACEHOLDER_FORMINPUT = "#formInput#";
	public static final String PLACEHOLDER_SPAN = "#span#";
	public static final String PLACEHOLDER_FIELDNAME = "#fieldName#";
	public static final String PLACEHOLDER_FIELDCODENAME = "#fieldCodeName#";
	
	public static final String HTML_ROW_START_TEMPLATE = "\t\t\t\t<el-row>\n";
	public static final String HTML_ROW_END_TEMPLATE = "\t\t\t\t</el-row>\n";
	
	public static final String HTML_EMPTY_COL_TEMPLATE = "\t\t\t\t\t<el-col :span=\"#span#\"></el-col>\n";
	public static final String HTML_SEARCH_OPTION_TEMPLATE = "\t\t\t\t\t\t\t\t<el-option label=\"#fieldName#\" value=\"#fieldCodeName#\"></el-option>\n";
	
	private InputTypeConsist(String name){
		this.name = name;
	}
	
	public static InputTypeConsist get(String name){
		InputTypeConsist[] values = InputTypeConsist.values();
		for (InputTypeConsist inputTypeConsist : values) {
			if(inputTypeConsist.toString().equals(name)){
				return inputTypeConsist;
			}
		}
		return null;
	}
	
	public abstract String createFormInput();

	@Override
	public String toString() {
		return this.name;
	}
	
}
