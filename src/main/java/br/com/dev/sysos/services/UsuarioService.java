package br.com.dev.sysos.services;

import java.util.Collections;
import java.util.List;

import br.com.dev.sysos.dao.EmpresaDao;
import br.com.dev.sysos.dao.UsuarioDao;
import br.com.dev.sysos.domain.administrativo.Empresa;
import br.com.dev.sysos.domain.administrativo.Usuario;
import br.com.dev.sysos.exceptions.NegocioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Service da entidade Usuario
 *
 * @author Alan Jhone
 *
 */
@Component
public class UsuarioService extends CrudService<Usuario> {

    /**
     * Objeto DAO da entidade Usuario
     */
    @Autowired
    private UsuarioDao usuarioDao;
    
    @Autowired
    private EmpresaDao empresaDao;

    /**
     * Método que consulta e verifica se um usuário já existe
     *
     * @param cpf
     * @return
     */
    public Boolean existeUsuario(String cpf){
        cpf = cpf.replaceAll("\\D", "");
        
        if (usuarioDao.getUsuarioByCpf(cpf) == null ) {
            return false;
        } 
        
        return true;
    }
    public Boolean existeLogin(String login){
        if (usuarioDao.getByLogin(login) == null ) {
            return false;
        } 
        
        return true;
    }


    /**
     * Método que retorna a listagem usuarios
     * @return
     */
    public List<Usuario> listar() {
        usuarioDao.getSession().clear();
        return usuarioDao.listar();
    }


    /**
     * Método que consulta usuario a partir do login
     *
     * @param login
     * @return
     */
    public Usuario getByLogin(String login){
        return usuarioDao.getByLogin(login);
    }

    /**
     * Método que consulta a partir do CPF
     *
     * @param cpf
     * @return
     */
    public Usuario getUsuarioByCpf(String cpf){
        return usuarioDao.getUsuarioByCpf(cpf);
    }

    /**
     * Método que consulta usuário a partir do id recebido
     *
     * @param id
     * @return
     */
    public Usuario buscarPorId(int id){
        return usuarioDao.buscar(id, Usuario.class);
    }

    /**
     * Método que retorna os detalhes do usuário logado
     *
     * @return
     */
    public UserDetails getUserDetails() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = null;
        if (principal instanceof UserDetails) {
            userDetails = (UserDetails) principal;
        }
        
        return userDetails;
    }

    /**
     * Exclusão Lógica
     *
     */
    @Override
    public void remover(Usuario obj) throws NegocioException {
        obj.setAtivo(false);
        super.salvarOuAtualizar(obj);
    }

    /**
     * Verificação de login único
     *
     * @return
     */
    public boolean existeUnicoLogin(String login) {
        if (usuarioDao.getByLogin(login) == null ) {
            return false;
        }
        
        return true;
    }
    
	public List<Empresa> findEmpresaByUsuarioId(int usuarioId) {
		return usuarioDao.findEmpresaByUsuarioId(usuarioId);
	}
	
	public List<Usuario> findAllSolicitacoes() {
		return usuarioDao.findAllSolicitacoes();
	}
	
	public void aprovarSolicitacaoAcesso(List<Usuario> usuariosSelecionados, Empresa empresaSelecionada) throws NegocioException {
		for(Usuario usuario : usuariosSelecionados) {
			List<Empresa> empresas = findEmpresaByUsuarioId(usuario.getId());
			empresas.add(empresaSelecionada);
			usuario.setSolicitacaoAcesso(false);
			super.salvarOuAtualizar(usuario);
		}
	}
	
	public List<Usuario> findByEmpresa(String schemaAcesso) {
		Empresa empresa = empresaDao.findBySchemaAcesso(schemaAcesso);
		
		List<Usuario> usuarios = Collections.emptyList();
		
		if(empresa != null) {
			usuarios = usuarioDao.findbyEmpresa(empresa.getId());
		}
		
		return usuarios;
		
	}
	
	public List<Usuario> findAllSolicitacoesByEmpresa(String schemaAcesso) {
		Empresa empresa = empresaDao.findBySchemaAcesso(schemaAcesso);
		
		List<Usuario> usuarios = Collections.emptyList();
		
		if(empresa != null) {
			usuarios = usuarioDao.findSolicitacoesByEmpresa(empresa.getId());
		}
		
		return usuarios;
	}
    
}

