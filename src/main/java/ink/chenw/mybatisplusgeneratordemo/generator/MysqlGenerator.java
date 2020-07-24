package ink.chenw.mybatisplusgeneratordemo.generator;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * mysql 代码生成器
 *
 * @author  chenw
 * @date  2020/7/23 17:24
 */
public class MysqlGenerator {
    //--------------------数据库相关配置---------------------
    //数据库连接
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf8&rewriteBatchedStatements=true&useAffectedRows=true&serverTimezone=GMT%2B8&useSSL=false&zeroDateTimeBehavior=CONVERT_TO_NULL";
    //账号
    private static final String USERNAME = "root";
    //密码
    private static final String PASSWORD = "123";
    //连接驱动
    private static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";

    //--------------------全局相关配置-----------------------
    //文件根目录
    private static final String PROJECTPATH = System.getProperty("user.dir");
    //作者名
    private static final String AUTHOR = "chenw";

    //--------------------包相关配置-----------------------
    //基本包名
    private static final String BASE_PACKAGE = "ink.chenw.mybatisplusgeneratordemo";

    //--------------------策略相关配置-----------------------
    //表前缀
    private static final String TABLE_PREFIX = "t_";
    //需要排除生成的表
    private static final String [] EXCLUDE_TABLE = new String[] {"undo_log","test"};


    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    /**
     * RUN THIS
     */
    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator autoGenerator = new AutoGenerator();

        /**
         * 数据源配置
         */
        autoGenerator.setDataSource(new DataSourceConfig()
                .setUrl(DB_URL)
                .setUsername(USERNAME)
                .setPassword(PASSWORD)
                .setDriverName(DRIVER_NAME)
        );

        /**
         * 全局配置
         */
        autoGenerator.setGlobalConfig(new GlobalConfig()
                //输出目录
                .setOutputDir( PROJECTPATH + "/src/main/java")
                //是否覆盖文件
                .setFileOverride(true)
                // 开启 activeRecord 模式
                .setActiveRecord(true)
                // XML 二级缓存
                .setEnableCache(false)
                // XML ResultMap
                .setBaseResultMap(true)
                // XML columList
                .setBaseColumnList(true)
                //生成后打开文件夹
                .setOpen(false)
                //作者
                .setAuthor(AUTHOR)
                // 自定义文件命名，注意 %s 会自动填充表实体属性！
               /* .setMapperName("%sMapper")
                .setXmlName("%sMapper")
                .setServiceName("%sService")
                .setServiceImplName("%sServiceImpl")
                .setControllerName("%sController")*/
        );

        /**
         * 包配置
         */
        PackageConfig packageConfig = new PackageConfig()
                .setParent(BASE_PACKAGE)
                .setEntity("model")
                .setModuleName(scanner("模块名"))
               /*.setMapper("dao")
                .setService("service")
                .setServiceImpl("service.impl")
                .setController("controller")
                .setXml("mapper")*/
                ;
         autoGenerator.setPackageInfo(packageConfig);


        /**
         * 自定义配置
         */
        // 注入自定义配置
        InjectionConfig injectionConfig = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        autoGenerator.setCfg(injectionConfig);

        //自定义文件输出位置（非必须）
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig("/templates/mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return PROJECTPATH + "/src/main/resources/mapper/" + packageConfig.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        injectionConfig.setFileOutConfigList(focList);
        autoGenerator.setTemplate(new TemplateConfig().setXml(null));

        /**
         * 策略配置
         */
        autoGenerator.setStrategy(new StrategyConfig()
                        //表名生成策略
                        .setNaming(NamingStrategy.underline_to_camel)
                        //数据库表字段映射到实体的命名策略
                        .setColumnNaming(NamingStrategy.underline_to_camel)
                        //表前缀
                        .setTablePrefix(TABLE_PREFIX)
                        //是否为lambok模型
                        .setEntityLombokModel(true)
                        //需要排除的表
                        //.setExclude(EXCLUDE_TABLE)
                        .setControllerMappingHyphenStyle(true)
                        .setInclude(scanner("表名,多个使用,分割").split(","))
                        //设置controller类型
                       //.setRestControllerStyle(true)

               /* 基类相关设置
                .setSuperEntityClass("com.baomidou.mybatisplus.samples.generator.common.BaseEntity")
                .setSuperControllerClass("com.baomidou.mybatisplus.samples.generator.common.BaseController")
                .setSuperServiceClass()
                .setSuperServiceImplClass()
                .setSuperMapperClass()*/
        );

        // 选择 freemarker 引擎需要指定如下加，注意 pom 依赖必须有！
        autoGenerator.setTemplateEngine(new FreemarkerTemplateEngine());
        autoGenerator.execute();
    }
}
