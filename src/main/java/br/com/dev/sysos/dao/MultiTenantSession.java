/**
 * 
 */
package br.com.dev.sysos.dao;

import org.hibernate.MultiTenancyStrategy;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * @author Alan Jhone
 *
 */
public class MultiTenantSession {

    private static ServiceRegistry serviceRegistry;
    private static SessionFactory sessionFactory;

    private MultiTenantSession() {
    }

    public static Session getMultiTenantSession(String tenant) {

        if (sessionFactory == null) {
            sessionFactory = buildSessionFactory();
        }

        return sessionFactory.withOptions().tenantIdentifier(tenant).openSession();
    }

    @SuppressWarnings("deprecation")
	private static SessionFactory buildSessionFactory() {
        Configuration cfg = new Configuration().configure(Environment.DATASOURCE);
        cfg.getProperties().put(Environment.MULTI_TENANT, MultiTenancyStrategy.SCHEMA);

        serviceRegistry = new ServiceRegistryBuilder().applySettings(cfg.getProperties()).buildServiceRegistry();

        return cfg.buildSessionFactory(serviceRegistry);
    }


}