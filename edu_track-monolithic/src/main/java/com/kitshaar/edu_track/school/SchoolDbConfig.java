package com.kitshaar.edu_track.school;

import javax.sql.DataSource;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
        basePackages = "com.kitshaar.edu_track.school.repositories",
        entityManagerFactoryRef = "schoolEntityManagerFactory",
        transactionManagerRef = "schoolTransactionManager"
)
@EntityScan(basePackages = "com.kitshaar.edu_track.school.models")
public class SchoolDbConfig {

    @Autowired
    private Environment env;

    @Primary
    @Bean(name = "schoolDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.school")
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setUrl(env.getProperty("spring.datasource.school.url"));
        ds.setUsername(env.getProperty("spring.datasource.school.username"));
        ds.setPassword(env.getProperty("spring.datasource.school.password"));
        ds.setDriverClassName(Objects.requireNonNull(env.getProperty("spring.datasource.school.driver-class-name")));

        return ds;
    }

    @Primary
    @Bean(name = "schoolEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean schoolEntityManagerFactory(@Qualifier("schoolDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(dataSource);
        JpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        bean.setJpaVendorAdapter(adapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        bean.setJpaPropertyMap(properties);
        bean.setPackagesToScan("com.kitshaar.edu_track.school.models");
        return bean;
    }

    @Primary
    @Bean(name = "schoolTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("schoolEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
