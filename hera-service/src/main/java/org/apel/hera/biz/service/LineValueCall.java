package org.apel.hera.biz.service;

/**
 * 
 * 对于模板文件中每一行的行数据处理回调接口
 * @author lijian
 *
 */
@FunctionalInterface
public interface LineValueCall {

	String exec(String line);

}
