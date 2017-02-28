package org.apel.hera.biz.dao;

import java.util.List;

import org.apel.gaia.persist.dao.CommonRepository;
import org.apel.hera.biz.domain.ModuleRowColumn;

public interface ModuleRowColumnRepository extends CommonRepository<ModuleRowColumn, String>{

	int countByModuleId(String moduleId);

	List<ModuleRowColumn> findByModuleIdOrderByRowAsc(String moduleId);
	
}
