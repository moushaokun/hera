package org.apel.hera.biz.service;

import java.util.List;

import org.apel.gaia.infrastructure.BizCommonService;
import org.apel.hera.biz.domain.ModuleRowColumn;

public interface ModuleRowColService extends BizCommonService<ModuleRowColumn, String>{

	String addRowCol(String moduleId, Integer colNum);

	List<ModuleRowColumn> findByModuleId(String moduleId);

	
	
}
