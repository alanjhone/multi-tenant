package br.com.dev.sysos.dao;


import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate4.HibernateTemplate;
import br.com.dev.sysos.exceptions.NegocioException;

/**
 * GenericDao
 *
 * @author Alan Jhone
 *
 */
@SuppressWarnings("unchecked")
public class GenericDao extends HibernateTemplate {

    private static Session session;

    /**
     * Buscar por id.
     * @param id
     * @param classe
     * @return
     */
    public <T> T buscar(int id, Class <T> classe) {
        Session session = getSession();
        T obj = (T) session.get(classe, id);
        closeSession();
        return obj;
    }

    /**
     * Listar ordenando pelo campo.
     * @param classe
     * @param campo
     * @return
     */
    @Deprecated
    public <T> List<T> listar(Class<T> classe, String campo) {
        DetachedCriteria c = DetachedCriteria.forClass(classe);
        if(campo != null)
        {
            c.addOrder(Order.asc(campo));
        }
        c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return (List<T>) findByCriteria(c);
    }

    /**
     * Listar ordenando por campo.
     * @param classe
     * @param campo
     * @return
     */
    public <T> List<T> listar(Class<T> classe, String campo, boolean isAsc) {
        DetachedCriteria c = DetachedCriteria.forClass(classe);
        if(campo != null)
        {
            if(isAsc)
                c.addOrder(Order.asc(campo));
            else
                c.addOrder(Order.desc(campo));
        }
        c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return (List<T>) findByCriteria(c);
    }

    /**
     * Remover um objeto.
     * @param obj
     */
    
    public void remover(Object obj) {
        Session session = getSession();
        session.clear();
        session.delete(obj);
        session.flush();
        closeSession();
    }

	/**
     * Método usado para armazenar um objeto no banco de dados.
     * 
     * @param objeto
     * @return
     * @throws DAOException
     */
    
	
	public void inserir(Object obj) throws NegocioException {		
		try {
			Session session = getSession();
			session.persist(obj);
		} catch (Exception hibernateException) {
			throw new NegocioException(hibernateException.getMessage());
		}
	}
    
    /**
     * Salvar ou atualizar um objeto.
     * @param obj
     */
	
	
    public void salvarOuAtualizar(Object obj) {
        Session session = getSession();
        session.clear();
        session.saveOrUpdate(obj);
        session.flush();
        session.clear();
    }

    /**
     * Count all
     * @param classe
     * @return
     */
    public <T> Integer countAll(Class<T> classe) {
        DetachedCriteria c = DetachedCriteria.forClass(classe);
        c.setProjection(Projections.count("id"));
        List<T> result = (List<T>) findByCriteria(c);
        return (Integer) ((Number)result.get(0)).intValue();
    }

    /**
     * Retornar a sessão do hibernate.
     * @return
     */
    public Session getSession() {
    	
    	try {
    		session = getSessionFactory().getCurrentSession();
    	}catch (Exception e) {
    		if(session == null || !session.isOpen())
    			session = getSessionFactory().openSession();
    	}
    	
        return session;
    }

    /**
     * Fechar a sessão do hibernate.
     * @return
     */
    public void closeSession() {
        
    }
    
    /**
     * Iniciar uma transação
     */
    public void startTransaction(){
        getSession().beginTransaction();
    }

    /**
     * Commit numa transação
     */
    public void commitTransaction(){
        getSession().getTransaction().commit();
    }

    /**
     * rollback nos commits da transação
     */
    public void rollbackTransaction(){
        getSession().getTransaction().rollback();
    }


}
