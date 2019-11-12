/**
 * 
 */
package br.com.dev.sysos.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import br.com.dev.sysos.domain.administrativo.Empresa;
import br.com.dev.sysos.domain.administrativo.Usuario;
import br.com.dev.sysos.helpers.Administrativo;
import br.com.dev.sysos.services.UsuarioService;
import br.com.dev.sysos.utils.FacesUtils;

/**
 * @author Alan Jhone
 *
 */

@Component
@Scope("session")
public class controleAcessoVinculoMBean extends AbstractMBean<Usuario>{

	@Autowired
	private UsuarioService usuarioService;
	
	private List<Empresa> empresas;
	
	public controleAcessoVinculoMBean(){
		empresas = new ArrayList<Empresa>();
	}
	
	public List<Empresa> listaVinculosAtivoUsuario(){		
		empresas = getUsuarioLogado().getEmpresas();
		
		return empresas;
	}
	
	public String selecionarVinculo(Empresa empresa) {
		
		if(empresa != null && empresa.getNome() != "") {
			
			Usuario usuario = getUsuarioLogado();
			
			if(empresa.getNome().equalsIgnoreCase(Administrativo.SCHEMA_ACESSO_ADMIN)) {
				usuario.setSchemaAcesso("public");
			}else {
				usuario.setSchemaAcesso(empresa.getNome());
			}
			
			List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
		
			try{
				grantedAuths.addAll(usuario.getPermissoesCurrentEmpresa(usuario.getSchemaAcesso())); // Corrigir
				
				Authentication auth = new UsernamePasswordAuthenticationToken(usuario, usuario.getSenha(), grantedAuths);
			
				SecurityContextHolder.getContext().setAuthentication(auth);
				
				return "/pages/home.jsf";
				
			}catch (Exception e) {
				FacesUtils.addMensagemErro("Não foi possível selecionar o vínculo");
			}
		}else {
			FacesUtils.addMensagemErro("Por favor, selecione um vínculo válido.");
		}
		
		return "login.jsf";
		
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
	
	

}
