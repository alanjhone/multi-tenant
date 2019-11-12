package br.com.dev.sysos.helpers;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 
 * 
 * 
 * @author Alan Jhone
 *
 */
public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext ctx = null;

    public static ApplicationContext getApplicationContext() {
        if (ctx == null) {
            // recupera o ServletContext
            ServletContext servletContext = null;

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();

            if (attr != null && attr.getRequest() != null) {
                HttpSession session = attr.getRequest().getSession();
                servletContext = (session != null) ? session
                        .getServletContext() : null;
            }

            if (servletContext != null)
                ctx = WebApplicationContextUtils
                        .getRequiredWebApplicationContext(servletContext);
        }

        return ctx;
    }

    public void setApplicationContext(ApplicationContext ctx)
            throws BeansException {
        ApplicationContextProvider.ctx = ctx;
    }
}