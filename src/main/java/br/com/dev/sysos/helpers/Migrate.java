/**
 * 
 */
package br.com.dev.sysos.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.dev.sysos.dao.EmpresaDao;
import br.com.dev.sysos.domain.administrativo.Empresa;

/**
 * @author Alan Jhone
 *
 */


public class Migrate {

    private final String[] tenantLocations = new String[] {"classpath:db/migration/shared/"};
    
    private final String[] adminLocations = new String[] {"classpath:db/migration/administrador/"};
    
    private final String adminSchemaMigrate = "public";
    
    private DataSource dataSource;	
    
    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(Migrate.class);
    
    @Autowired
    private EmpresaDao empresaDao;
    
    private List<Empresa> empresas;
    
    public Migrate() {
    	empresas = new ArrayList<Empresa>();
	}

	public void repairAndMigrate() {
		try {
			empresas = empresaDao.findAll();
			tenantMigrate(empresas);
		}catch (SQLGrammarException e) {
			logger.debug("Não foi possívl obter empresas");
		}
		
		adminMigrate();

    }

    
	public void tenantMigrate(List<Empresa> empresas) {
		
		if(!empresas.isEmpty()) {
			
			String[] schemas = new String[empresas.size()];
			
			for(int i=0; i < empresas.size(); i++)
				schemas[i] = empresas.get(i).getNome();
			
	        Flyway flyway = Flyway.configure().
	        		schemas(schemas).
	        		dataSource(this.dataSource).
	        		baselineOnMigrate(true).
	        		locations(tenantLocations).
	        		outOfOrder(true).load();
	        
	        flyway.repair();
	        
	        flyway.migrate();
		}
		
	}
	
	public void adminMigrate() {

        Flyway flyway = Flyway.configure().
        		schemas(adminSchemaMigrate).
        		dataSource(this.dataSource).
        		baselineOnMigrate(true).
        		locations(adminLocations).
        		outOfOrder(true).load();
        
        flyway.repair();
        
        flyway.migrate();
		
	}	
	
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
	
}
	

