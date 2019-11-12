package br.com.dev.sysos.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.dev.sysos.domain.administrativo.Empresa;
import br.com.dev.sysos.domain.administrativo.Pessoa;
import br.com.dev.sysos.domain.administrativo.Usuario;


/**
 * UsuarioDao
 *
 * @author Alan Jhone
 *
 */
@Component
public class UsuarioDao extends GenericDao {

    /**
     * Construtor que recebe o session factory do hibernate.
     *
     * @param sf
     */
    @Autowired
    public UsuarioDao(SessionFactory sf) {
        setSessionFactory(sf);
    }

    /**
     * Método que retorna a listagem de usuários
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Usuario> listar() {
    	List<Usuario> usuarios = getSession().createQuery("select u from Usuario u where ativo = true and u.solicitacaoAcesso = false order by u.pessoa.nome").list();
    	closeSession();
    	return usuarios;
    }

    /**
     * Buscar o usuário pelo CPF informado.
     *
     * @param cpf
     * @return
     */
    public Usuario getUsuarioByCpf(String cpf) {
        String hql = "select u from Usuario u WHERE u.pessoa.cpf= :cpf and ativo = true";
        Query query = getSession().createQuery(hql);
        query.setString("cpf", cpf);
        Usuario usuario = (Usuario) query.uniqueResult();
        
        closeSession();
        
        return usuario;

    }

    /**
     * Buscar o usuário pelo login informado.
     *
     * @param login
     * @return
     */
    public Usuario getByLogin(String login) {
        String hql = "select u from Usuario u join u.usuarioEmpresas WHERE u.login = :loginUsuario and u.ativo = true";
        Query query = getSession().createQuery(hql);
        query.setString("loginUsuario", login);
        Usuario usuario = (Usuario) query.uniqueResult();
        
        closeSession();
        
        return usuario;
    }

    
    public Pessoa getCpf(String cpf) {
        String hql = "select p from Pessoa p WHERE p.cpf= :cpf";
        Query query = getSession().createQuery(hql);
        query.setString("cpf", cpf);
        Pessoa pessoa = (Pessoa) query.uniqueResult();
        
        closeSession();
        
        return pessoa;
    }

	public List<Empresa> findEmpresaByUsuarioId(int usuarioId) {
        String hql = "SELECT ue.empresa FROM Usuario u join u.usuarioEmpresas ue WHERE u.id = :usuarioId";
        Query query = getSession().createQuery(hql);
        query.setInteger("usuarioId", usuarioId);
        @SuppressWarnings("unchecked")
		List<Empresa> empresas = (List<Empresa>) query.list();
        
        closeSession();
        
        return empresas;
	}

	public List<Usuario> findAllSolicitacoes() {
		String hql = "SELECT u FROM Usuario u where u.solicitacaoAcesso = true order by u.pessoa.nome";
    	List<Usuario> usuarios = getSession().createQuery(hql).list();
    	closeSession();
    	return usuarios;
	}
	
	@SuppressWarnings("unchecked")
	public List<Usuario> findSolicitacoesByEmpresa(int idEmpresa) {
		String hql = "SELECT u FROM Usuario u join u.usuarioEmpresas ue where u.solicitacaoAcesso = true and ue.empresa.id = :idEmpresa order by u.pessoa.nome";
		Query query = getSession().createQuery(hql);
		query.setInteger("idEmpresa", idEmpresa);
    	
		List<Usuario> usuarios = (List<Usuario>) query.list();
    	
    	closeSession();
    	return usuarios;
	}

	public List<Usuario> findbyEmpresa(int idEmpresa) {
		String hql = "SELECT u FROM Usuario u join u.usuarioEmpresas ue WHERE ue.empresa.id = :idEmpresa and u.solicitacaoAcesso = false order by u.pessoa.nome";
		Query query = getSession().createQuery(hql);
		query.setInteger("idEmpresa", idEmpresa);
    	
		List<Usuario> usuarios = (List<Usuario>) query.list();
    	
    	closeSession();
    	return usuarios;
	}
	
	
     
}
