package org.apel.hera.biz.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 模块行列配置
 * 
 * @author lijian
 *
 */
@Entity
@Table(name = "robot_module_row_col")
public class ModuleRowColumn {

	@Id
	private String id;

	/**
	 * 行号
	 */
	private Integer row;

	/**
	 * 列数
	 */
	private Integer colNum;
	
	private String moduleId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getRow() {
		return row;
	}

	public void setRow(Integer row) {
		this.row = row;
	}

	public Integer getColNum() {
		return colNum;
	}

	public void setColNum(Integer colNum) {
		this.colNum = colNum;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	
}
