package org.apel.hera.biz.service;

import java.util.List;

import org.apel.gaia.infrastructure.BizCommonService;
import org.apel.hera.biz.domain.Domain;

public interface DomainService extends BizCommonService<Domain, String>{
	
	List<Domain> findByProjectId(String projectId);

	public void generateCode(String domainId, String rootPath);
	
	public byte[] downloadCodeZip(String domainId);
}
