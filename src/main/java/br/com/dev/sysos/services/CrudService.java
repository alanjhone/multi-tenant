package br.com.dev.sysos.services;

import java.io.InputStream;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.dev.sysos.dao.CrudDao;
import br.com.dev.sysos.exceptions.NegocioException;

/**
 * Service genérico
 *
 * @author Alan Jhone
 *
 * @param <T>
 */
@Component
public class CrudService<T> {

    /**
     * Objeto DAO genérico
     */
	
    @Autowired
    private CrudDao dao;

    /**
     * salvar ou atualizar o objeto
     *
     * @param obj
     */
    public void salvarOuAtualizar(T obj) throws NegocioException{
        dao.salvarOuAtualizar(obj);
    }

    public void salvarOuAtualizar(T obj, InputStream is) throws NegocioException{
        dao.salvarOuAtualizar(obj);
    }

    public void inserir(T obj) throws NegocioException{
        dao.inserir(obj);
    }
    
    /**
     * Rremover o objeto
     *
     * @param obj
     */
    public void remover(T obj) throws NegocioException{
        try{
            dao.remover(obj);
        }catch(ConstraintViolationException e){
            throw new NegocioException("O objeto não pode ser excluído, pois possui dependências.");
        }
    }

    /**
     * Count All
     *
     * @param classe
     * @return
     */
    public Integer countAll(Class<T> classe) {
        return dao.countAll(classe);
    }

    /**
     * Buscar pelo id
     *
     * @param id, classe
     * @return
     */
    public T buscar(Integer id, Class<T> classe) {
        return dao.buscar(id, classe);
    }

    /**
     * Buscar todos
     *
     * @param classe
     * @return
     */
    public List<T> findAll(Class<T> classe) {
        return dao.listar(classe, null, true);
    }


    /**
     * Buscar todos com parâmetro 
     *
     * @param classe
     * @return
     */
    public List<T> findAll(Class<T> classe,String campo, boolean isAsc) {
        return dao.listar(classe, campo, isAsc);
    }

}
