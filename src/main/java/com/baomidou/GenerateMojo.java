package com.baomidou;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import com.baomidou.config.ConstVal;
import com.baomidou.config.TemplateConfig;
import com.baomidou.config.builder.ConfigBuilder;
import com.baomidou.config.po.TableInfo;

/**
 * 生成文件
 *
 * @author YangHu, tangguo
 * @since 2016/8/30
 */
@Mojo(name = "code", threadSafe = true)
public class GenerateMojo extends AbstractGenerateMojo {

    /**
     * velocity引擎
     */
    private VelocityEngine engine;

    /**
     * 输出文件
     */
    private Map<String, String> outputFiles;

    public void execute() throws MojoExecutionException, MojoFailureException {
        log.info("==========================准备生成文件...==========================");
        // 初始化配置
        initConfig();
        // 初始化输出文件路径模板
        initOutputFiles();
        // 创建输出文件路径
        mkdirs(config.getPathInfo());
        // 获取上下文
        Map<String, VelocityContext> ctxData = analyzeData(config);
        //mybatis字符串
        List<String> list = new ArrayList<String>();
        // 循环生成文件
        int i = 1000;
        for (Map.Entry<String, VelocityContext> ctx : ctxData.entrySet()) {
            log.info("ctx.getKey() = " + ctx.getKey() + ",ctx.getValue() =" + ctx.getValue());
            batchOutput(ctx.getKey(), ctx.getValue());
            String menuname = ctx.getKey().toLowerCase();
            String menunamecode = menuname;
            list.add("INSERT INTO `sys_permission` VALUES ('" + i + "', '\u0001', '" + menuname + "', '0', '" + menunamecode + "', 'menu', null);");
            int j = i + 1;
            list.add("INSERT INTO `sys_permission` VALUES ('" + j + "', '\u0001', '" + menuname + "搜索', '" + i + "', '" + menunamecode + "_search', 'button', null);");
            j = j + 1;
            list.add("INSERT INTO `sys_permission` VALUES ('" + j + "', '\u0001', '" + menuname + "新增', '" + i + "', '" + menunamecode + "_add', 'button', null);");
            j = j + 1;
            list.add("INSERT INTO `sys_permission` VALUES ('" + j + "', '\u0001', '" + menuname + "编辑', '" + i + "', '" + menunamecode + "_edit', 'button', null);");
            j = j + 1;
            list.add("INSERT INTO `sys_permission` VALUES ('" + j + "', '\u0001', '" + menuname + "查看', '" + i + "', '" + menunamecode + "_view', 'button', null);");
            j = j + 1;
            list.add("INSERT INTO `sys_permission` VALUES ('" + j + "', '\u0001', '" + menuname + "删除', '" + i + "', '" + menunamecode + "_delete', 'button', null);");
            i = j + 1;
        }
        log.info("==========================打开输出目录...==========================");
        // 打开输出目录
        if (isOpen()) {
            try {
                String osName = System.getProperty("os.name");
                if (osName != null) {
                    if (osName.contains("Mac")) {
                        Runtime.getRuntime().exec("open " + getOutputDir());
                    } else if (osName.contains("Windows")) {
                        Runtime.getRuntime().exec("cmd /c start " + getOutputDir());
                    } else {
                        log.info("文件输出目录:" + getOutputDir());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.info("==========================文件生成完成！！！==========================");
        for (String str : list) {
            log.info(str);
        }
    }

    /**
     * 分析数据
     *
     * @param config 总配置信息
     * @return 解析数据结果集
     */
    private Map<String, VelocityContext> analyzeData(ConfigBuilder config) {
        List<TableInfo> tableList = config.getTableInfoList();
        Map<String, String> packageInfo = config.getPackageInfo();
        Map<String, VelocityContext> ctxData = new HashMap<String, VelocityContext>();
        String superEntityClass = getSuperClassName(config.getSuperEntityClass());
        String superMapperClass = getSuperClassName(config.getSuperMapperClass());
        String superServiceClass = getSuperClassName(config.getSuperServiceClass());
        String superServiceImplClass = getSuperClassName(config.getSuperServiceImplClass());
        String superControllerClass = getSuperClassName(config.getSuperControllerClass());
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        for (TableInfo tableInfo : tableList) {
            VelocityContext ctx = new VelocityContext();
            ctx.put("package", packageInfo);
            ctx.put("table", tableInfo);
            ctx.put("entity", tableInfo.getEntityName());
            ctx.put("addTabeName", !tableInfo.getEntityName().toLowerCase().equals(tableInfo.getName().toLowerCase()));
            ctx.put("idGenType", config.getIdType());
            ctx.put("superEntityClassPackage", config.getSuperEntityClass());
            ctx.put("superEntityClass", superEntityClass);
            ctx.put("superMapperClassPackage", config.getSuperMapperClass());
            ctx.put("superMapperClass", superMapperClass);
            ctx.put("superServiceClassPackage", config.getSuperServiceClass());
            ctx.put("superServiceClass", superServiceClass);
            ctx.put("superServiceImplClassPackage", config.getSuperServiceImplClass());
            ctx.put("superServiceImplClass", superServiceImplClass);
            ctx.put("superControllerClassPackage", config.getSuperControllerClass());
            ctx.put("superControllerClass", superControllerClass);
            ctx.put("enableCache", isEnableCache());
            ctx.put("author", getAuthor());
            ctx.put("activeRecord", isActiveRecord());
            ctx.put("date", date);
            ctxData.put(tableInfo.getEntityName(), ctx);
        }
        return ctxData;
    }

    /**
     * 获取类名
     *
     * @param classPath
     * @return
     */
    private String getSuperClassName(String classPath) {
        if (StringUtils.isBlank(classPath))
            return null;
        return classPath.substring(classPath.lastIndexOf(".") + 1);
    }

    /**
     * 处理输出目录
     *
     * @param pathInfo 路径信息
     */
    private void mkdirs(Map<String, String> pathInfo) {
        for (Map.Entry<String, String> entry : pathInfo.entrySet()) {
            File dir = new File(entry.getValue());
            if (!dir.exists()) {
                boolean result = dir.mkdirs();
                if (result) {
                    log.info("创建目录： [" + entry.getValue() + "]");
                }
            }
        }
    }

    /**
     * 初始化输出目录
     */
    private void initOutputFiles() {
        log.info("initOutputFiles");
        outputFiles = new HashMap<String, String>();
        Map<String, String> pathInfo = config.getPathInfo();
        outputFiles.put(ConstVal.ENTITY, pathInfo.get(ConstVal.ENTITY_PATH) + ConstVal.ENTITY_NAME);
        outputFiles.put(ConstVal.MAPPER, pathInfo.get(ConstVal.MAPPER_PATH) + ConstVal.MAPPER_NAME);
        outputFiles.put(ConstVal.VO, pathInfo.get(ConstVal.VO_PATH) + ConstVal.ENTITY_NAME);
        outputFiles.put(ConstVal.XML, pathInfo.get(ConstVal.XML_PATH) + ConstVal.XML_NAME);
        outputFiles.put(ConstVal.JSP, pathInfo.get(ConstVal.JSP_PATH) + ConstVal.JSP_NAME);
        outputFiles.put(ConstVal.SERIVCE, pathInfo.get(ConstVal.SERIVCE_PATH) + ConstVal.SERVICE_NAME);
        outputFiles.put(ConstVal.SERVICEIMPL, pathInfo.get(ConstVal.SERVICEIMPL_PATH) + ConstVal.SERVICEIMPL_NAME);
        outputFiles.put(ConstVal.CONTROLLER, pathInfo.get(ConstVal.CONTROLLER_PATH) + ConstVal.CONTROLLER_NAME);
        outputFiles.put(ConstVal.FEIGNCLIENT, pathInfo.get(ConstVal.FEIGNCLIENT_PATH) + ConstVal.FEIGNCLIENT_NAME);
        outputFiles.put(ConstVal.DEGRADEDFEIGNCLIEN, pathInfo.get(ConstVal.DEGRADEDFEIGNCLIEN_PATH) + ConstVal.DEGRADEDFEIGNCLIEN_NAME);
        outputFiles.put(ConstVal.APICONTROLLER, pathInfo.get(ConstVal.APICONTROLLER_PATH) + ConstVal.APICONTROLLER_NAME);
    }

    /**
     * 合成上下文与模板
     *
     * @param context vm上下文
     */
    private void batchOutput(String entityName, VelocityContext context) {
        try {
            String entityFile = String.format(outputFiles.get(ConstVal.ENTITY), entityName);
            String mapperFile = String.format(outputFiles.get(ConstVal.MAPPER), entityName);
            String voFile = String.format(outputFiles.get(ConstVal.VO), entityName + "VO");
            String xmlFile = String.format(outputFiles.get(ConstVal.XML), entityName);
            String jsppath = outputFiles.get(ConstVal.JSP);
            jsppath = jsppath.substring(0, jsppath.indexOf("%s"));
            jsppath = jsppath + entityName.toLowerCase();
            File dir = new File(jsppath);

            if (!dir.exists()) {
                boolean result = dir.mkdirs();
                if (result) {
                    log.info("创建目录： [" + jsppath + "]");
                }
            }
            jsppath = jsppath + ConstVal.JSP_NAME;
//			outputFiles.put(ConstVal.JSP,jsppath);
            String jspFile = String.format(jsppath, "list");
            String addFile = String.format(jsppath, "add");
            String editFile = String.format(jsppath, "edit");
            String viewFile = String.format(jsppath, "view");
            String serviceFile = String.format(outputFiles.get(ConstVal.SERIVCE), entityName);
            String implFile = String.format(outputFiles.get(ConstVal.SERVICEIMPL), entityName);
            String controllerFile = String.format(outputFiles.get(ConstVal.CONTROLLER), entityName);
            String feignFile = String.format(outputFiles.get(ConstVal.FEIGNCLIENT), entityName);
            String degradedFeignClienFile = String.format(outputFiles.get(ConstVal.DEGRADEDFEIGNCLIEN), entityName);
            String apiControllerFile = String.format(outputFiles.get(ConstVal.APICONTROLLER), entityName);

            TemplateConfig template = config.getTemplate();

            // 根据override标识来判断是否需要创建文件
            if (isCreate(entityFile)) {
                vmToFile(context, template.getEntity(), entityFile);
            }
            if (isCreate(voFile)) {
                log.info("voFile =====" + voFile);
                vmToFile(context, template.getVo(), voFile);
            }
            if (isCreate(mapperFile)) {
                log.info("文件****" + mapperFile + "==");
                vmToFile(context, template.getMapper(), mapperFile);
            }
            if (isCreate(xmlFile)) {
                vmToFile(context, template.getXml(), xmlFile);
            }
            if (isCreate(jspFile)) {
                vmToFile(context, template.getJsp(), jspFile);
            }
            if (isCreate(addFile)) {
                vmToFile(context, template.getAdd(), addFile);
            }
            if (isCreate(editFile)) {
                vmToFile(context, template.getEdit(), editFile);
            }
            if (isCreate(viewFile)) {
                vmToFile(context, template.getView(), viewFile);
            }
            if (isCreate(serviceFile)) {
                vmToFile(context, template.getService(), serviceFile);
            }
            if (isCreate(implFile)) {
                vmToFile(context, template.getServiceImpl(), implFile);
            }
            if (isCreate(controllerFile)) {
                vmToFile(context, template.getController(), controllerFile);
            }

            if (isCreate(feignFile)) {
                vmToFile(context, template.getFeignClient(), feignFile);
            }

            if (isCreate(degradedFeignClienFile)) {
                vmToFile(context, template.getDegradedFeignClien(), degradedFeignClienFile);
            }

            if (isCreate(apiControllerFile)) {
                vmToFile(context, template.getApiController(), apiControllerFile);
            }

        } catch (IOException e) {
            log.error("无法创建文件，请检查配置信息！");
            e.printStackTrace();
        }
    }

    /**
     * 将模板转化成为文件
     *
     * @param context      内容对象
     * @param templatePath 模板文件
     * @param outputFile   文件生成的目录
     */
    private void vmToFile(VelocityContext context, String templatePath, String outputFile) throws IOException {
        log.info("templatePath*{" + templatePath + "}*||*{" + outputFile + "}");
        VelocityEngine velocity = getVelocityEngine();
        Template template = velocity.getTemplate(templatePath, ConstVal.UTF8);
        FileOutputStream fos = new FileOutputStream(outputFile);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos, ConstVal.UTF8));
        template.merge(context, writer);
        writer.close();
        log.info("模板:" + templatePath + ";  文件:" + outputFile);
    }

    /**
     * 设置模版引擎，主要指向获取模版路径
     */
    private VelocityEngine getVelocityEngine() {
        if (engine == null) {
            Properties p = new Properties();
            p.setProperty(ConstVal.VM_LOADPATH_KEY, ConstVal.VM_LOADPATH_VALUE);
            p.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, "");
            p.setProperty(Velocity.ENCODING_DEFAULT, ConstVal.UTF8);
            p.setProperty(Velocity.INPUT_ENCODING, ConstVal.UTF8);
            p.setProperty(Velocity.OUTPUT_ENCODING, ConstVal.UTF8);
            p.setProperty("file.resource.loader.unicode", "true");
            engine = new VelocityEngine(p);
        }
        return engine;
    }

    /**
     * 检测文件是否存在
     *
     * @return 是否
     */
    private boolean isCreate(String filePath) {
        File file = new File(filePath);
        return !file.exists() || isFileOverride();
    }

}
