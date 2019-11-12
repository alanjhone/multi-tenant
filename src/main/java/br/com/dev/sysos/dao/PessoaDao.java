/**
 * 
 */
package br.com.dev.sysos.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.dev.sysos.domain.administrativo.Pessoa;

/**
 * @author Alan Jhone
 *
 */

@Component
public class PessoaDao extends GenericDao {

    @Autowired
    public PessoaDao(SessionFactory sf) {
        setSessionFactory(sf);
    }
	
}
