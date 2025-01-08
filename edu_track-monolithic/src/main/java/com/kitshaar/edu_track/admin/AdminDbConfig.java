package com.kitshaar.edu_track.admin;


import javax.sql.DataSource;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashMap;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.kitshaar.edu_track.admin.repositories",
        entityManagerFactoryRef = "adminEntityManagerFactory",
        transactionManagerRef = "adminTransactionManager"
)
@EntityScan(basePackages = "com.kitshaar.edu_track.admin.models")
public class AdminDbConfig {

    @Autowired
    Environment env;

    @Primary
    @Bean(name = "adminDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.admin")
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setUrl(env.getProperty("spring.datasource.admin.url"));
        ds.setUsername(env.getProperty("spring.datasource.admin.username"));
        ds.setPassword(env.getProperty("spring.datasource.admin.password"));
        ds.setDriverClassName(Objects.requireNonNull(env.getProperty("spring.datasource.admin.driver-class-name")));

        return  ds;
    }

    @Primary
    @Bean(name = "adminEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean adminEntityManagerFactory(@Qualifier("adminDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(dataSource);
        JpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        bean.setJpaVendorAdapter(adapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        bean.setJpaPropertyMap(properties);
        bean.setPackagesToScan("com.kitshaar.edu_track.admin.models");
        return bean;
    }


    @Primary
    @Bean(name = "adminTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("adminEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
