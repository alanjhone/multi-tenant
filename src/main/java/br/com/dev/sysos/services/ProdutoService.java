/**
 * 
 */
package br.com.dev.sysos.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.dev.sysos.dao.ProdutoDao;
import br.com.dev.sysos.domain.shared.Produto;

/**
 * @author Alan Jhone
 *
 */
@Component
public class ProdutoService extends CrudService<Produto>{

    /**
     * Objeto DAO da entidade Usuario
     */
    @Autowired
    private ProdutoDao produtoDao;
    
    public List<Produto> listar() {
        return produtoDao.listar();
    }
	
}
