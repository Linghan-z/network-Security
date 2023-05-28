package com.zlhhh.networksecurity.utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;

import java.sql.Types;
import java.util.Collections;

/**
 * mp代码生成器
 */
public class CodeGenerator {
    public static void main(String[] args) {
        generate();
    }
    private static void generate() {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/vue?serverTimezone=GMT%2B8", "root", "19991125")
                .globalConfig(builder -> {
                    builder.author("zlhhh") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("/Users/zlh/networkSecurity/src/main/java/"); // 指定输出目录
                })
                .dataSourceConfig(builder -> builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                    int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                    if (typeCode == Types.SMALLINT) {
                        // 自定义类型转换
                        return DbColumnType.INTEGER;
                    }
                    return typeRegistry.getColumnType(metaInfo);

                }))
                .packageConfig(builder -> {
                    builder.parent("com.zlhhh.networksecurity") // 设置父包名
                            .moduleName(null) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "/Users/zlh/networkSecurity/src/main/resources/mapper/")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("sys_user") // 设置需要生成的表名
                            .addTablePrefix("sys_", "t_"); // 设置过滤表前缀
                    builder.serviceBuilder().enableFileOverride(); // 文件覆盖配置
                    builder.controllerBuilder().enableFileOverride()  // 文件覆盖配置
                            .enableHyphenStyle()  //开启驼峰转连字符
                            .enableRestStyle();  //开启生成@RestController 控制器
                    builder.entityBuilder().enableLombok()  // Lombok
                            .enableFileOverride();  // 文件覆盖配置
                    builder.mapperBuilder().enableFileOverride();  // 文件覆盖配置
                })

//                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
