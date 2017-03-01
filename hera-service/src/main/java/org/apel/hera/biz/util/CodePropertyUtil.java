package org.apel.hera.biz.util;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apel.hera.biz.domain.Domain;
import org.apel.hera.biz.domain.Field;
import org.apel.hera.biz.domain.JavaCoreParam;
import org.apel.hera.biz.domain.Project;
import org.apel.hera.biz.service.CodeGenerationPolicy;
import org.springframework.util.StringUtils;

public class CodePropertyUtil {
	public final static String FIELD_GET_SET_METHOD_TEMPLATE = "\tpublic #dataType# get#upperFieldName#() {\n\t\treturn #fieldName#;\n\t}\n\tpublic void set#upperFieldName#(#dataType# #fieldName#) {\n\t\tthis.#fieldName# = #fieldName#;\n\t}\n";
	
	public static String[] handleCodeJavaProperty(List<Field> fields){
		StringBuffer fieldTemplateBuilder = new StringBuffer();
		StringBuffer fieldMethodBuilder = new StringBuffer();
		String importPackage = "";
		for (Field field : fields) {
			String name = field.getName();
			String dataType = field.getDataType();
			String fieldName = field.getCodeName();
			String upperFieldName = String.valueOf(Character.toUpperCase(fieldName.charAt(0))) + fieldName.substring(1, fieldName.length());
			//填充注释
			if(!StringUtils.isEmpty(name)){
				fieldTemplateBuilder.append("\t// " + name + "\n");
			}
			if(StringUtils.isEmpty(dataType)){
				dataType = "String";
			}
			if(dataType.equals("Date")){
				importPackage = "import org.springframework.format.annotation.DateTimeFormat;\n";
				fieldTemplateBuilder.append("\t@DateTimeFormat(pattern = \"yyyy-MM-dd\")\n");
			}
			//填充字段
			fieldTemplateBuilder.append("\tprivate " + dataType + "  " + fieldName + ";\n");
			//填充字段的get set方法
			fieldMethodBuilder.append(
				FIELD_GET_SET_METHOD_TEMPLATE
					.replaceAll("#upperFieldName#", upperFieldName)
					.replaceAll("#fieldName#", fieldName)
					.replaceAll("#dataType#", dataType)
			);
		}
		String fieldTemplate = fieldTemplateBuilder.toString();
		String fieldMethodTemplate = fieldMethodBuilder.toString();
		String[] result = new String[3];
		result[0] = fieldTemplate;
		result[1] = fieldMethodTemplate;
		result[2] = importPackage;
		return result;
	}
	
	public static void main(String[] args) throws Exception {
		Project project = new Project();
		project.setPackageName("org.apel.test");
		Domain domain = new Domain();
		domain.setClassName("Person");
		domain.setTableName("t_person");
		domain.setProject(project);
		List<Field> fields = new ArrayList<>();
		Field field = new Field();
		field.setCodeName("birthday");
		field.setName("出生日期");
		field.setDataType("Date");
		fields.add(field);
		Field f2 = new Field();
		f2.setCodeName("succeed");
		f2.setName("是否成功");
		f2.setDataType("Boolean");
		fields.add(f2);
		JavaCoreParam param = new JavaCoreParam();
		param.setFields(fields);
		param.setDomain(domain);
		byte[] data1 = CodeGenerationPolicy.DOMAIN_TEMPLATE.generateSourceCode(param);
		IOUtils.write(data1, new FileOutputStream("D:/Person.java"));
		byte[] data2 = CodeGenerationPolicy.REPOSITORY_TEMPLATE.generateSourceCode(param);
		IOUtils.write(data2, new FileOutputStream("D:/PersonRepository.java"));
		byte[] data3 = CodeGenerationPolicy.SERVICE_TEMPLATE.generateSourceCode(param);
		IOUtils.write(data3, new FileOutputStream("D:/PersonService.java"));
		byte[] data4 = CodeGenerationPolicy.SERVICE_IMPL_TEMPLATE.generateSourceCode(param);
		IOUtils.write(data4, new FileOutputStream("D:/PersonServiceImpl.java"));
		byte[] data5 = CodeGenerationPolicy.CONTROLLER_TEMPLATE.generateSourceCode(param);
		IOUtils.write(data5, new FileOutputStream("D:/PersonController.java"));
		byte[] data6 = CodeGenerationPolicy.I18N_TEMPLATE.generateSourceCode(param);
		IOUtils.write(data6, new FileOutputStream("D:/person_zh_CN.properties"));
	}
	
	
	
}
