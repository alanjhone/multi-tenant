/**
 * 
 */
package br.com.dev.sysos.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.dev.sysos.dao.PessoaDao;
import br.com.dev.sysos.domain.administrativo.Pessoa;
import br.com.dev.sysos.domain.administrativo.Usuario;
import br.com.dev.sysos.exceptions.NegocioException;


/**
 * @author Alan Jhone
 *
 */
@Component
public class PessoaService extends CrudService<Pessoa>{

    /**
     * Objeto DAO da entidade Usuario
     */
    @Autowired
    private PessoaDao pessoaDao;
	
}
