package com.baomidou.config;

import org.apache.maven.plugins.annotations.Parameter;

/**
 * @author tzg
 * @ClassName: TemplateConfig
 * @Description: 模板路径配置项
 * @date 2016年11月10日 下午4:45:12
 */
public class TemplateConfig {

    @Parameter(defaultValue = ConstVal.TEMPLATE_ENTITY)
    private String entity;

    @Parameter(defaultValue = ConstVal.TEMPLATE_SERVICE)
    private String service;

    @Parameter(defaultValue = ConstVal.TEMPLATE_SERVICEIMPL)
    private String serviceImpl;

    @Parameter(defaultValue = ConstVal.TEMPLATE_MAPPER)
    private String mapper;

    @Parameter(defaultValue = ConstVal.TEMPLATE_VO)
    private String vo;

    @Parameter(defaultValue = ConstVal.TEMPLATE_XML)
    private String xml;

    @Parameter(defaultValue = ConstVal.TEMPLATE_JSP)
    private String jsp;
    @Parameter(defaultValue = ConstVal.TEMPLATE_ADD)
    private String add;
    @Parameter(defaultValue = ConstVal.TEMPLATE_EDIT)
    private String edit;
    @Parameter(defaultValue = ConstVal.TEMPLATE_VIEW)
    private String view;

    @Parameter(defaultValue = ConstVal.TEMPLATE_CONTROLLER)
    private String controller;


    public String getEntity() {
        if (entity == null) return ConstVal.TEMPLATE_ENTITY;
        return entity;
    }

    public String getService() {
        if (service == null) return ConstVal.TEMPLATE_SERVICE;
        return service;
    }

    public String getServiceImpl() {
        if (serviceImpl == null) return ConstVal.TEMPLATE_SERVICEIMPL;
        return serviceImpl;
    }

    public String getMapper() {
        if (mapper == null) return ConstVal.TEMPLATE_MAPPER;
        return mapper;
    }

    public String getXml() {
        if (xml == null) return ConstVal.TEMPLATE_XML;
        return xml;
    }

    public String getJsp() {
        if (jsp == null) return ConstVal.TEMPLATE_JSP;
        return jsp;
    }

    public String getController() {
        if (controller == null) return ConstVal.TEMPLATE_CONTROLLER;
        return controller;
    }

    public String getAdd() {
        if (add == null) return ConstVal.TEMPLATE_ADD;
        return add;
    }

    public String getEdit() {
        if (edit == null) return ConstVal.TEMPLATE_EDIT;
        return edit;
    }

    public String getView() {
        if (view == null) return ConstVal.TEMPLATE_VIEW;
        return view;
    }

    public String getVo() {
        if (vo == null) return ConstVal.TEMPLATE_VO;
        return vo;
    }
}
