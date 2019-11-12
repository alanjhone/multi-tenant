package br.com.dev.sysos.helpers;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Alan Jhone
 *
 */

public class FlywayMigrate {

	
	private static final Logger logger = LoggerFactory.getLogger(FlywayMigrate.class);
	
    public void migrate(String schema) {

    	try {
            logger.debug("Iniciando migração");
        	
        	DataSource ds = (DataSource)ApplicationContextProvider.getApplicationContext().getBean("dataSource");
        	
            Flyway flyway = Flyway.configure().
            		schemas(schema).
            		dataSource(ds).
            		baselineOnMigrate(true).
            		locations(new String[] {"classpath:db/migration/shared/"}).
            		outOfOrder(true).load();
            
            flyway.repair();
            flyway.migrate();
            
            logger.debug("Migração finalizada");
        } catch (Throwable e ){
            logger.error("Erro ao realizar a migração do flyway.", e);
        }
        
    }	
}
