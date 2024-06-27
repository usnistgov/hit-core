/**
 * This software was developed at the National Institute of Standards and Technology by employees of
 * the Federal Government in the course of their official duties. Pursuant to title 17 Section 105
 * of the United States Code this software is not subject to copyright protection and is in the
 * public domain. This is an experimental system. NIST assumes no responsibility whatsoever for its
 * use by other parties, and makes no guarantees, expressed or implied, about its quality,
 * reliability, or any other characteristic. We would appreciate acknowledgement if the software is
 * used. This software can be redistributed and/or modified freely provided that any derivative
 * works bear some notice that they are derived from it, and any modified versions bear some notice
 * that they have been modified.
 */

package gov.nist.hit.core.api.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Harold Affo (NIST)
 * 
 */

@Configuration(value = "DBConfig")
@EnableJpaRepositories(value = "gov.nist.hit")
@PropertySource(value = "classpath:app-config.properties")
@EnableTransactionManagement(proxyTargetClass = true)
@ComponentScan("gov.nist.hit") 
public class DbConfig {

	@Autowired
	private Environment env;

	@Bean
	public DataSource dataSource() {
		final JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
		dsLookup.setResourceRef(true);
		DataSource dataSource = dsLookup.getDataSource("jdbc/base_tool_jndi_dev");
		return dataSource;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource,
			JpaVendorAdapter jpaVendorAdapter) {
		LocalContainerEntityManagerFactoryBean lef = new LocalContainerEntityManagerFactoryBean();
		lef.setDataSource(dataSource);
		lef.setJpaVendorAdapter(jpaVendorAdapter);
		lef.setPackagesToScan("gov.nist.hit");
		lef.setJpaProperties(jpaProperties());
		lef.setPersistenceUnitName("base-tool");
		// lef.setPersistenceUnitManager(persistenceUnitManager);
		lef.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
		return lef;
	}

	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
		jpaVendorAdapter.setShowSql(Boolean.getBoolean(env.getProperty("jpa.showSql")));
		jpaVendorAdapter.setGenerateDdl(Boolean.getBoolean(env.getProperty("jpa.generateDdl")));
		jpaVendorAdapter.setDatabase(Database.valueOf(env.getProperty("jpa.database")));
		jpaVendorAdapter.setDatabasePlatform(env.getProperty("jpa.databasePlatform"));
		return jpaVendorAdapter;
	}

	private Properties jpaProperties() {
		Properties properties = new Properties();
//		 properties.put("hibernate.cache.use_second_level_cache",true);
//		 properties.put("hibernate.cache.region.factory_class",	"org.hibernate.cache.ehcache.EhCacheRegionFactory");
//		 properties.put("hibernate.cache.use_query_cache",	 true);
		 
		properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
		properties.put("hibernate.dialect", env.getProperty("jpa.databasePlatform"));
		properties.put("hibernate.globally_quoted_identifiers",		env.getProperty("hibernate.globally_quoted_identifiers"));

		return properties;
	}

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory);
		transactionManager.setJpaDialect(new HibernateJpaDialect());
		return transactionManager;
	}

	@Bean
	PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	// @Bean
	// public DataSourceInitializer dataSourceInitializer(DataSource
	// authDataSource) {
	// ResourceDatabasePopulator resourceDatabasePopulator = new
	// ResourceDatabasePopulator();
	// DataSourceInitializer dataSourceInitializer = new
	// DataSourceInitializer();
	// dataSourceInitializer.setDataSource(authDataSource);
	// dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
	// return dataSourceInitializer;
	// }

	//
	// @Bean
	// PersistenceUnitManager persistenceUnitManager() {
	// return new DefaultPersistenceUnitManager();
	// }

}
