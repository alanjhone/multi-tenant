/**
 * 
 */
package br.com.dev.sysos.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Alan Jhone
 *
 */

@Component
public class UsuarioEmpresaDao extends GenericDao {

    /**
     * Construtor que recebe o session factory do hibernate.
     *
     * @param sf
     */
    @Autowired
    public UsuarioEmpresaDao(SessionFactory sf) {
        setSessionFactory(sf);
    }
    
}
