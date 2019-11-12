/**
 * 
 */
package br.com.dev.sysos.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.dev.sysos.domain.shared.Produto;

/**
 * @author Alan Jhone
 *
 */
@Component
public class ProdutoDao extends GenericDao{

    /**
     * Construtor que recebe o session factory do hibernate.
     *
     * @param sf
     */
    @Autowired
    public ProdutoDao(SessionFactory sf) {
        setSessionFactory(sf);
    }
	
    @SuppressWarnings("unchecked")
    public List<Produto> listar() {
    	List<Produto> produtos = getSession().createQuery("select p from Produto p order by p.nome").list();
    	
    	closeSession();
    	
    	return produtos;
    }
    
}
