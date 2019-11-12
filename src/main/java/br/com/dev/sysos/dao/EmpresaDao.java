/**
 * 
 */
package br.com.dev.sysos.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.dev.sysos.domain.administrativo.Empresa;
import br.com.dev.sysos.domain.administrativo.Usuario;

/**
 * @author Alan Jhone
 *
 */

@Component
public class EmpresaDao extends GenericDao{

    /**
     * Construtor que recebe o session factory do hibernate.
     *
     * @param sf
     */
    @Autowired
    public EmpresaDao(SessionFactory sf) {
        setSessionFactory(sf);
    }
    
    public List<Empresa> findAllAdm(){
    	List<Empresa> empresas = getSession().createQuery("select e from Empresa e ORDER BY e.nome").list();
    	closeSession();
    	return empresas;
    }
    
    public List<Empresa> findAll(){
    	List<Empresa> empresas = getSession().createQuery("select e from Empresa e WHERE e.nome <> 'public' ORDER BY e.nome").list();
    	closeSession();
    	return empresas;
    }

    public Empresa findByCnpj(String cnpj){
    	String hql = "select e from Empresa e WHERE e.cnpj = :cnpj";
    	Query query = getSession().createQuery(hql);
        query.setString("cnpj", cnpj);
        Empresa empresa = (Empresa) query.uniqueResult();
        closeSession();
        
    	return empresa;
    }

	public Empresa findBySchemaAcesso(String schemaAcesso) {
    	String hql = "select e from Empresa e WHERE e.nome = :schema";
    	Query query = getSession().createQuery(hql);
        query.setString("schema", schemaAcesso);
        Empresa empresa = (Empresa) query.uniqueResult();
        closeSession();
        
    	return empresa;
	}

}
