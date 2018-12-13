package com.jlt.common.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author Billy.Zhang
 * @date 2018/10/14
 */
@Configuration
@MapperScan(basePackages = {"com.jlt.datasync.mapper.upm"}, sqlSessionFactoryRef = "sqlSessionFactoryUpm")
public class UpmMybatisConfig {
    @Autowired
    @Qualifier("upmDataSource")
    private DataSource upmDataSource;


    @Bean("sqlSessionFactoryUpm")
    public SqlSessionFactory sqlSessionFactoryUpm() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(upmDataSource);
        return factoryBean.getObject();
    }

    @Bean("sqlSessionTemplateUpm")
    public SqlSessionTemplate sqlSessionTemplateUpm() throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactoryUpm()); // 使用上面配置的Factory
        return template;
    }
}
