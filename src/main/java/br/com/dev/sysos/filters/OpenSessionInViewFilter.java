/**
 * 
 */
package br.com.dev.sysos.filters;


import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.dev.sysos.domain.administrativo.Usuario;
import br.com.dev.sysos.helpers.Administrativo;

import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.orm.hibernate4.support.OpenSessionInViewInterceptor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Servlet 2.3 Filter that binds a Hibernate Session to the thread for the entire
 * processing of the request. Intended for the "Open Session in View" pattern,
 * i.e. to allow for lazy loading in web views despite the original transactions
 * already being completed.
 *
 * <p>This filter makes Hibernate Sessions available via the current thread, which
 * will be autodetected by transaction managers. It is suitable for service layer
 * transactions via {@link org.springframework.orm.hibernate4.HibernateTransactionManager}
 * as well as for non-transactional execution (if configured appropriately).
 *
 * <p><b>NOTE</b>: This filter will by default <i>not</i> flush the Hibernate Session,
 * with the flush mode set to <code>FlushMode.NEVER</code>. It assumes to be used
 * in combination with service layer transactions that care for the flushing: The
 * active transaction manager will temporarily change the flush mode to
 * <code>FlushMode.AUTO</code> during a read-write transaction, with the flush
 * mode reset to <code>FlushMode.NEVER</code> at the end of each transaction.
 *
 * <p><b>WARNING:</b> Applying this filter to existing logic can cause issues that
 * have not appeared before, through the use of a single Hibernate Session for the
 * processing of an entire request. In particular, the reassociation of persistent
 * objects with a Hibernate Session has to occur at the very beginning of request
 * processing, to avoid clashes with already loaded instances of the same objects.
 *
 * <p>Looks up the SessionFactory in Spring's root web application context.
 * Supports a "sessionFactoryBeanName" filter init-param in <code>web.xml</code>;
 * the default bean name is "sessionFactory". Looks up the SessionFactory on each
 * request, to avoid initialization order issues (when using ContextLoaderServlet,
 * the root application context will get initialized <i>after</i> this filter).
 *
 * @author Juergen Hoeller
 * @since 3.1
 * @see #lookupSessionFactory
 * @see OpenSessionInViewInterceptor
 * @see org.springframework.orm.hibernate4.HibernateTransactionManager
 * @see org.springframework.transaction.support.TransactionSynchronizationManager
 */

public class OpenSessionInViewFilter extends OncePerRequestFilter {

    public static final String DEFAULT_SESSION_FACTORY_BEAN_NAME = "sessionFactory";

    private static final Logger logger = LoggerFactory.getLogger(OpenSessionInViewFilter.class);

    private String sessionFactoryBeanName = DEFAULT_SESSION_FACTORY_BEAN_NAME;

    public void setSessionFactoryBeanName(String sessionFactoryBeanName) {
        this.sessionFactoryBeanName = sessionFactoryBeanName;
    }

    protected String getSessionFactoryBeanName() {
        return this.sessionFactoryBeanName;
    }


    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Usuario usuarioLogado = null;
		
        if (principal instanceof Usuario) {
        	usuarioLogado = (Usuario) principal;
        }
        
        SessionFactory sessionFactory = lookupSessionFactory(request);
        boolean participate = false;

        if (TransactionSynchronizationManager.hasResource(sessionFactory) || usuarioLogado == null) {
            // Do not modify the Session: just set the participate flag.
            participate = true;
        }
        else {
            logger.debug("Opening Hibernate Session in OpenSessionInViewFilter");
            Session session = openSession(sessionFactory, usuarioLogado);
            TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
        }

        try {
            filterChain.doFilter(request, response);
        }

        finally {
            if (!participate) {
                SessionHolder sessionHolder =
                        (SessionHolder) TransactionSynchronizationManager.unbindResource(sessionFactory);
                logger.debug("Closing Hibernate Session in OpenSessionInViewFilter");
                SessionFactoryUtils.closeSession(sessionHolder.getSession());
            }
        }
    }

    protected SessionFactory lookupSessionFactory(HttpServletRequest request) {
        return lookupSessionFactory();
    }

    protected SessionFactory lookupSessionFactory() {
        if (logger.isDebugEnabled()) {
            logger.debug("Using SessionFactory '" + getSessionFactoryBeanName() + "' for OpenSessionInViewFilter");
        }
        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        return wac.getBean(getSessionFactoryBeanName(), SessionFactory.class);
    }

    protected Session openSession(SessionFactory sessionFactory, Usuario usuarioLogado) throws DataAccessResourceFailureException {
        try {
        	
        	String schemaAcesso = usuarioLogado.getSchemaAcesso();
        	
        	if(schemaAcesso.equalsIgnoreCase(Administrativo.SCHEMA_ACESSO_ADMIN))
        		schemaAcesso = "public";
        	
            Session session = sessionFactory.withOptions().tenantIdentifier(schemaAcesso).openSession();
            session.setFlushMode(FlushMode.MANUAL);
            return session;
        }
        catch (HibernateException ex) {
            throw new DataAccessResourceFailureException("Could not open Hibernate Session", ex);
        }
    }
}
