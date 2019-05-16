package com.baomidou.config;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * 跟包相关的配置项
 *
 * @author YangHu, tangguo
 * @since 2016/8/30
 */
public class PackageConfig {

    /**
     * 父包名。如果为空，将下面子包名必须写全部， 否则就只需写子包名
     */
    @Parameter(defaultValue = "com.baomidou")
    private String parent;
    
    /**
     * 父包模块名。
     */
    private String moduleName;
    
    /**
     * Entity包名
     */
    @Parameter(defaultValue = "entity")
    private String entity;

    @Parameter(defaultValue = "vo")
    private String vo;

    /**
     * Service包名
     */
    @Parameter(defaultValue = "service")
    private String service;

    /**
     * Service Impl包名
     */
    @Parameter(defaultValue = "service.impl")
    private String serviceImpl;
    /**
     * Mapper包名
     */
    @Parameter(defaultValue = "mapper")
    private String mapper;

    /**
     * Mapper XML包名
     */
    @Parameter(defaultValue = "dao.xml")
    private String xml;
    /**
     * Mapper XML包名
     */
    @Parameter(defaultValue = "jsp")
    private String jsp;
    /**
     * Controller包名
     */
    @Parameter(defaultValue = "web")
    private String controller;

    public String getParent() {
    	if(moduleName != null && !"".equals(moduleName.trim()))
    		return parent + "." + moduleName;
        return parent;
    }
    
    public String getModuleName() {
    	return moduleName;
    }

    public String getEntity() {
        return entity;
    }

    public String getVo() {
        return vo;
    }

    public String getService() {
        return service;
    }

    public String getServiceImpl() {
        return serviceImpl;
    }

    public String getMapper() {
        return mapper;
    }

    public String getXml() {
        return xml;
    }
    
    public String getController() {
    	if(StringUtils.isBlank(controller)) return "web";
    	return controller;
    }

    public String getJsp() {
        return jsp;
    }

}
