/**
 * 
 */
package br.com.dev.sysos.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.dev.sysos.dao.EmpresaDao;
import br.com.dev.sysos.dao.PapelDao;
import br.com.dev.sysos.domain.administrativo.Empresa;
import br.com.dev.sysos.domain.administrativo.Papel;
import br.com.dev.sysos.domain.administrativo.Usuario;
import br.com.dev.sysos.domain.administrativo.UsuarioEmpresa;
import br.com.dev.sysos.exceptions.NegocioException;
import br.com.dev.sysos.exceptions.SegurancaException;
import br.com.dev.sysos.services.PessoaService;
import br.com.dev.sysos.services.UsuarioEmpresaService;
import br.com.dev.sysos.services.UsuarioService;
import br.com.dev.sysos.utils.FacesUtils;

/**
 * @author Alan Jhone
 *
 */

@Component
@Scope("session")
public class UsuarioMBean extends AbstractMBean<Usuario> {

	@Autowired
	private UsuarioEmpresaService usuarioEmpresaService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	private boolean usuarioSelecionado;

	@Autowired
	private PapelDao papelDao;
	
	@Autowired
	private EmpresaDao empresaDao;

	@Autowired
	private PessoaService pessoaService;
	
	private Empresa empresaSelecionada;
	
	private int empresaSelecionadaId;
	
	private List<Empresa> empresas;
	
	private List<Usuario> usuarios;
	
	// Listagem de usuários selecionados na solicitação de acesso usuários
	private List<Usuario> usuariosSelecionados;
	
	public UsuarioMBean() {
		zerarObjs();
	}

	void zerarObjs() {
		obj = new Usuario();
		usuariosSelecionados = new ArrayList<Usuario>();
		empresaSelecionada = new Empresa();
		usuarios = new ArrayList<Usuario>();
		empresas = new ArrayList<Empresa>();	
		empresaSelecionadaId = 0;
		usuarioSelecionado = false;
	}
	
	/* --------------------------------- CONTROLE DE NAVEGAÇÂO DE PÁGINAS --------------------------------- */
	
	@Override
	protected String getDir() {
		return "/pages/usuario/";
	}

	public String voltar() {
		zerarObjs();
		
		return "/pages/home.jsf";
	}
	
	public String entrarCadastro(){
		zerarObjs();
		empresas = getAllEmpresa();
		return getDir() + "form_cadastro_usuario.jsf?faces-redirect=true";
	}
	
	public String entrarListagem(){
		if(verificarAcessoAdministradorGeral()) {
			usuarios = usuarioService.listar();
		}else{
			usuarios = usuarioService.findByEmpresa(getUsuarioLogado().getSchemaAcesso());
		}
		
		return getDir() + "list.jsf?faces-redirect=true";
	}

	public String entrarSolicitacaoAcesso() {
		obj = new Usuario();
		empresaSelecionada = new Empresa();
		
		empresas = empresaDao.findAll();
		
		return "auto_cadastro.jsf";
	}
	
	public String entrarListagemSolicitacoes() {
		zerarObjs();
		
		if(verificarAcessoAdministradorGeral()) {
			usuarios = usuarioService.findAllSolicitacoes();
		}else{
			usuarios = usuarioService.findAllSolicitacoesByEmpresa(getUsuarioLogado().getSchemaAcesso());
		}
		
		return "/pages/vincular_usuario_empresa.jsf?faces-redirect=true";
	}
	
	/* --------------------------------- VALIDAÇÃO/VERIFICAÇÃO DE DADOS --------------------------------- */
	
	public void validarDadosUsuario(Usuario usuario) throws NegocioException {
		
		if(!(usuario.getPessoa() != null && usuario.getPessoa().getNome() != "")) {
			throw new NegocioException("Por favor, preencha todos os campos!");
		}
		
		if(usuarioService.existeLogin(usuario.getLogin())) {
			throw new NegocioException("Já existe um usuário com o login informado!");
		}
		
	}
	
	public void validarDadosEmpresa(Empresa empresa) throws NegocioException {
		
		if(!(empresa != null && empresa.getId() > 0)) {
			throw new NegocioException("Por favor, preencha todos os campos!");
		}
		
	}
	
	public void verificarUsuariosSolicitacaoAcesso() throws NegocioException {
		if(!(usuariosSelecionados != null && !usuariosSelecionados.isEmpty())) {
			throw new NegocioException("Por favor, selecione algum usuário");
		}
		
	}
	
	public void verificarEmpresaSolicitacaoAcesso() throws NegocioException {
		if(!(empresaSelecionada != null && empresaSelecionada.getId() > 0)) {
			throw new NegocioException("Por favor, selecione a empresa");
		}
		
	}
	
	/*--------------------------------- CRUD ---------------------------------*/
	
	public String cadastrar(){
		
		try {
			
			validarDadosUsuario(obj);
			validarDadosEmpresa(empresaSelecionada);
			
			UsuarioEmpresa usuarioEmpresa = new UsuarioEmpresa();
			usuarioEmpresa.setEmpresa(addEmpresaSelecionada(empresaSelecionada.getId()));
			usuarioEmpresa.setPermissoes(addPermissaoAcessoUsuarioComum());
			usuarioEmpresa.setUsuario(obj);
			
			obj.setUsuarioEmpresa(usuarioEmpresa);
			obj.addUsuaEmpresa();
			
			
			pessoaService.salvarOuAtualizar(obj.getPessoa());
			
			usuarioEmpresaService.salvarOuAtualizar(usuarioEmpresa);
			
			FacesUtils.addMensagemSucesso("Cadastro realizado com sucesso");
			
			usuarios = usuarioService.listar();
			return getDir() + "list.jsf?faces-redirect=true";
			
		} catch (NegocioException e) {
			FacesUtils.addMensagemErro(e.getMessage());
		} catch (Exception e) {
			FacesUtils.addMensagemErro("Não foi possível realizar cadastro!");
		}
		
		return getDir() + "";
	}

	public List<Papel> addPermissaoAcessoUsuarioComum() {
		List<Papel> permissoes = new ArrayList<Papel>();
		permissoes.add(papelDao.buscar(Papel.ROLE_USER, Papel.class));
		return permissoes;
	}
	
	public Empresa addEmpresaSelecionada(int empresaId) {
		return empresaDao.buscar(empresaId, Empresa.class);
	}
	
	public String solicitarAcesso() {
		try {

			validarDadosUsuario(obj);
			validarDadosEmpresa(empresaSelecionada);
			
			obj.setSchemaAcesso(empresaSelecionada.getNome());
			obj.setSolicitacaoAcesso(true);
			
			UsuarioEmpresa usuarioEmpresa = new UsuarioEmpresa();
			usuarioEmpresa.setEmpresa(addEmpresaSelecionada(empresaSelecionada.getId()));
			usuarioEmpresa.setPermissoes(addPermissaoAcessoUsuarioComum());
			usuarioEmpresa.setUsuario(obj);
		
			
			obj.setUsuarioEmpresa(usuarioEmpresa);
			obj.addUsuaEmpresa();
			
			
			pessoaService.salvarOuAtualizar(obj.getPessoa());
			
			usuarioEmpresaService.salvarOuAtualizar(usuarioEmpresa);
			
			FacesUtils.addMensagemSucesso("Socilitação enviada com sucesso!");
			
			zerarObjs();
			return "login.jsf?faces-redirect=true";
			
		} catch (NegocioException e) {
			FacesUtils.addMensagemErro(e.getMessage());
		} catch (Exception e) {
			FacesUtils.addMensagemErro("Não foi possível enviar solicitação!");
		}
		
		return "";
		
	}
	
	public List<Empresa> completeEmpresas(String query){
		List<Empresa> allEmpresas = getAllEmpresa();
				
        List<Empresa> filteredEmpresas = allEmpresas.stream()               
                .filter(empresa -> empresa.getNome().toLowerCase().contains(query) || empresa.getCnpj().toLowerCase().contains(query))
                .collect(Collectors.toList());

        return filteredEmpresas;		

	}
	
    public void selecionarUsuario(Usuario usuarioSelecionado) {
    	if(isUsuarioSelecionado()) {
    		usuariosSelecionados.add(usuarioSelecionado);
    	}else {
    		usuariosSelecionados.remove(usuarioSelecionado);
    	}
    }
    
	public void aprovarSolicitacaoAcesso() {
		
		try {
			
			verificarUsuariosSolicitacaoAcesso();
			verificarEmpresaSolicitacaoAcesso();
			
			usuarioService.aprovarSolicitacaoAcesso(usuariosSelecionados, empresaSelecionada);
			zerarObjs();
			usuarios = usuarioService.findAllSolicitacoes();
			
		} catch (NegocioException e) {
			FacesUtils.addMensagemErro(e.getMessage());
		} catch (Exception e) {
			FacesUtils.addMensagemErro("Não foi possível realizar está operação");
		}
		
	}
	
	public List<Empresa> getAllEmpresa(){
		List<Empresa> allEmpresas = new ArrayList<Empresa>();
		
		if(verificarAcessoAdministradorGeral()) {
			allEmpresas = empresaDao.findAllAdm();
		}else {
			allEmpresas.add(empresaDao.findBySchemaAcesso(getUsuarioLogado().getSchemaAcesso()));
		}
		
		return allEmpresas;
	}
	
    /**
     * Método para verificação de papéis do usuário.
     * @param papeis
     * @throws SegurancaException
     */
    public boolean verificarAcessoAdministradorGeral() {      
    	
	    if(getUsuarioLogado() != null && !getUsuarioLogado().getPermissoes().isEmpty()) {
	        Papel permissao = usuarioLogado.getPermissoesCurrentEmpresa(usuarioLogado.getSchemaAcesso()).stream()
	        .filter( papel -> Papel.ROLE_ADMIN_GERAL == papel.getId())
	        .findAny()
	        .orElse(null);
	
	        if(permissao != null)
	            return true;
    	}
        
        return false;
    }
	
	public void rejeitarSolicitacaoAcesso() {
		try {
			verificarUsuariosSolicitacaoAcesso();
		} catch (NegocioException e) {
			FacesUtils.addMensagemErro(e.getMessage());
		}
	}

	public String voltarTelaLogin() {
		zerarObjs();
		
		return "login.jsf";
	}
	
	/**
	 * @return the empresaSelecionada
	 */
	public Empresa getEmpresaSelecionada() {
		return empresaSelecionada;
	}

	/**
	 * @param empresaSelecionada the empresaSelecionada to set
	 */
	public void setEmpresaSelecionada(Empresa empresaSelecionada) {
		this.empresaSelecionada = empresaSelecionada;
	}

	/**
	 * @return the empresas
	 */
	public List<Empresa> getEmpresas() {
		return empresas;
	}
	
	/**
	 * @param empresas the empresas to set
	 */
	public void setEmpresas(List<Empresa> empresas) {
		this.empresas = empresas;
	}

	/**
	 * @return the usuarios
	 */
	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	/**
	 * @param usuarios the usuarios to set
	 */
	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	/**
	 * @return the empresaSelecionadaId
	 */
	public int getEmpresaSelecionadaId() {
		return empresaSelecionadaId;
	}

	/**
	 * @param empresaSelecionadaId the empresaSelecionadaId to set
	 */
	public void setEmpresaSelecionadaId(int empresaSelecionadaId) {
		this.empresaSelecionadaId = empresaSelecionadaId;
	}

	/**
	 * @return the usuarioSelecionado
	 */
	public boolean isUsuarioSelecionado() {
		return usuarioSelecionado;
	}

	/**
	 * @param usuarioSelecionado the usuarioSelecionado to set
	 */
	public void setUsuarioSelecionado(boolean usuarioSelecionado) {
		this.usuarioSelecionado = usuarioSelecionado;
	}

	/**
	 * @return the usuariosSelecionados
	 */
	public List<Usuario> getUsuariosSelecionados() {
		return usuariosSelecionados;
	}

	/**
	 * @param usuariosSelecionados the usuariosSelecionados to set
	 */
	public void setUsuariosSelecionados(List<Usuario> usuariosSelecionados) {
		this.usuariosSelecionados = usuariosSelecionados;
	}
	
	
	
}
