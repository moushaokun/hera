package org.apel.hera.biz.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

@Entity
@Table(name = "robot_domain")
public class Domain {

	@Id
	private String id;

	private String domainName;

	private String className;

	private String tableName;
	
	@Transient
	private String domainCodeName;

	@ManyToOne
	@JoinColumn(name = "projectId")
	private Project project;

	@Transient
	private List<ModuleRowColumn> rowCols;

	private Date createDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public List<ModuleRowColumn> getRowCols() {
		return rowCols;
	}

	public void setRowCols(List<ModuleRowColumn> rowCols) {
		this.rowCols = rowCols;
	}

	public String getDomainCodeName() {
		if(!StringUtils.isEmpty(this.className)){
			return String.valueOf(Character.toLowerCase(this.className.charAt(0))) + this.className.substring(1, this.className.length());
		}else{
			return null;
		}
	}

	public void setDomainCodeName(String domainCodeName) {
		this.domainCodeName = domainCodeName;
	}
	
	

}
