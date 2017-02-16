package org.apel.hera.biz.service;

import org.apel.gaia.infrastructure.BizCommonService;
import org.apel.hera.biz.domain.Project;

public interface ProjectService extends BizCommonService<Project, String>{

	/**
	 * 产生工程脚手架
	 * @param projectId 工程id
	 * @return 脚手架zip
	 */
	byte[] generateProjectScaffold(String projectId);
	
}
