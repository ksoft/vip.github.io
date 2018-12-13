package com.jlt.common.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @author Billy.Zhang
 * @date 2018/10/14
 */
@Configuration
@MapperScan(basePackages = {"com.jlt.datasync.mapper.saas"}, sqlSessionFactoryRef = "sqlSessionFactorySaas")
public class SaasMybatisConfig {
    @Autowired
    @Qualifier("saasDataSource")
    private DataSource saasDataSource;


    @Bean("sqlSessionFactorySaas")
    @Primary
    public SqlSessionFactory sqlSessionFactorySaas() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(saasDataSource);
        return factoryBean.getObject();
    }

    @Bean("sqlSessionTemplateSaas")
    public SqlSessionTemplate sqlSessionTemplateSaas() throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactorySaas()); // 使用上面配置的Factory
        return template;
    }
}
