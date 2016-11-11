package com.baomidou.config;

import com.baomidou.config.rules.IdClassType;
import com.baomidou.config.rules.IdStrategy;
import com.baomidou.config.rules.NamingStrategy;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * 策略配置项
 *
 * @author YangHu, tangguo
 * @since 2016/8/30
 */
public class StrategyConfig {

    /**
     * 数据库表映射到实体的命名策略
     */
    @Parameter(defaultValue = "nochange")
    private NamingStrategy naming;
    
    private NamingStrategy fieldNaming;
    
    private String tablePrefix;
    
    /**
     * Entity 中的ID生成类型
     */
    @Parameter(defaultValue = "ID_WORKER")
    private IdStrategy idGenType;

    /**
     * 数据库表设计的ID的类型 STIRNG, LONG
     */
    @Parameter(defaultValue = "stringtype")
    private IdClassType serviceIdType;
    
    /**
     * 自定义继承的Entity类全称，带包名
     */
    @Parameter
    private String superEntityClass;
    
    /**
     * 自定义继承的Mapper类全称，带包名
     */
    @Parameter(defaultValue = ConstVal.SUPERD_MAPPER_CLASS)
    private String superMapperClass;
    
    /**
     * 自定义继承的Service类全称，带包名
     */
    @Parameter(defaultValue = ConstVal.SUPERD_SERVICE_CLASS)
    private String superServiceClass;
    
    /**
     * 自定义继承的ServiceImpl类全称，带包名
     */
    @Parameter(defaultValue = ConstVal.SUPERD_SERVICEIMPL_CLASS)
    private String superServiceImplClass;
    
    /**
     * 自定义继承的Controller类全称，带包名
     */
    @Parameter
    private String superControllerClass;

    /*
     *  需要包含的表名（与exclude二选一配置）
     */
    @Parameter
    private String[] include = null;

    /**
     * 需要排除的表名
     */
    @Parameter
    private String[] exclude = null;

    public NamingStrategy getNaming() {
        return naming;
    }
    
    public NamingStrategy getFieldNaming() {
    	if(fieldNaming == null)
    		return naming;
        return fieldNaming;
    }
    
    public String getTablePrefix() {
    	return tablePrefix;
    }

    public IdClassType getServiceIdType() {
        return serviceIdType;
    }

    public IdStrategy getIdGenType() {
        return idGenType;
    }

    public String[] getInclude() {
        return include;
    }

    public String[] getExclude() {
        return exclude;
    }

    public String getSuperServiceClass() {
        return superServiceClass;
    }

	public String getSuperEntityClass() {
		return superEntityClass;
	}

	public void setSuperEntityClass(String superEntityClass) {
		this.superEntityClass = superEntityClass;
	}

	public String getSuperMapperClass() {
		return superMapperClass;
	}

	public String getSuperServiceImplClass() {
		return superServiceImplClass;
	}

	public String getSuperControllerClass() {
		return superControllerClass;
	}

}
