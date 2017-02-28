package org.apel.hera.biz.service.impl;

import java.util.List;

import org.apel.gaia.infrastructure.impl.AbstractBizCommonService;
import org.apel.hera.biz.dao.ModuleRowColumnRepository;
import org.apel.hera.biz.domain.ModuleRowColumn;
import org.apel.hera.biz.service.ModuleRowColService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ModuleRowColServiceImpl extends AbstractBizCommonService<ModuleRowColumn, String> implements ModuleRowColService{

	@Override
	public String addRowCol(String moduleId, Integer colNum) {
		synchronized (this) {
			ModuleRowColumn rowCol = new ModuleRowColumn();
			rowCol.setColNum(colNum);
			rowCol.setModuleId(moduleId);
			int count = ((ModuleRowColumnRepository) getRepository())
					.countByModuleId(moduleId);
			rowCol.setRow(count + 1);
			return save(rowCol);
		}
	}

	@Override
	public List<ModuleRowColumn> findByModuleId(String moduleId) {
		return ((ModuleRowColumnRepository) getRepository()).findByModuleIdOrderByRowAsc(moduleId);
	}


	
}
