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
@MapperScan(basePackages = {"com.jlt.datasync.mapper.pms"}, sqlSessionFactoryRef = "sqlSessionFactoryPms")
public class PmsMybatisConfig {
    @Autowired
    @Qualifier("pmsDataSource")
    private DataSource pmsDataSource;


    @Bean("sqlSessionFactoryPms")
    public SqlSessionFactory sqlSessionFactoryPms() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(pmsDataSource);
        return factoryBean.getObject();
    }

    @Bean("sqlSessionTemplatePms")
    public SqlSessionTemplate sqlSessionTemplatePms() throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactoryPms()); // 使用上面配置的Factory
        return template;
    }
}
