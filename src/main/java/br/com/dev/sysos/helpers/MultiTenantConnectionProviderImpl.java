/**
 * 
 */
package br.com.dev.sysos.helpers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.HibernateException;
import org.hibernate.cfg.Environment;
import org.hibernate.engine.config.spi.ConfigurationService;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.service.spi.ServiceRegistryAwareService;
import org.hibernate.service.spi.ServiceRegistryImplementor;

/**
 * @author Alan Jhone
 *
 */
public class MultiTenantConnectionProviderImpl implements MultiTenantConnectionProvider, ServiceRegistryAwareService {

    private DataSource lazyDatasource;

    @Override
    public void injectServices(ServiceRegistryImplementor serviceRegistry) {
        Map lSettings = serviceRegistry.getService(ConfigurationService.class).getSettings();
        lazyDatasource = (DataSource) lSettings.get( Environment.DATASOURCE );
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
    	Connection conn = lazyDatasource.getConnection();

		return conn;
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        final Connection connection = getAnyConnection();

        try {
            connection.createStatement().execute("SET SCHEMA '" + tenantIdentifier + "'");
        }
        catch (SQLException e) {
            throw new HibernateException("Could not alter JDBC connection to specified schema [" + tenantIdentifier + "]", e);
        }

        return connection;
    }

	@Override
	public boolean isUnwrappableAs(Class unwrapType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> unwrapType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void releaseAnyConnection(Connection connection) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean supportsAggressiveRelease() {
		// TODO Auto-generated method stub
		return false;
	}
}