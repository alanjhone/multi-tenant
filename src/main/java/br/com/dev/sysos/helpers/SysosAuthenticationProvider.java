package br.com.dev.sysos.helpers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import br.com.dev.sysos.domain.administrativo.Empresa;
import br.com.dev.sysos.domain.administrativo.Papel;
import br.com.dev.sysos.domain.administrativo.Usuario;
import br.com.dev.sysos.services.UsuarioService;

/**
 * Autenticação customizada
 * 
 * @author Alan Jhone
 */
@Component
public class SysosAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UsuarioService usuarioService;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String login = authentication.getName();
		String senha = authentication.getCredentials().toString();

		Usuario usuario = usuarioService.getByLogin(login);
		
		if (authorizedUser(usuario, login, senha)) {

			List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
			
			try{
				usuario = verificarVinculo(usuario);
				
				grantedAuths.addAll(usuario.getPermissoes());
				
				Authentication auth = new UsernamePasswordAuthenticationToken(usuario, senha, grantedAuths);
				
				return auth;
				
			} catch (NullPointerException e) {
				throw new BadCredentialsException("Prezado usuário, o seu login está diferente do seu usuário do AD ou você não tem acesso a esse sistema."
						+ " Por favor, entre em contato com o nosso suporte.");
			}
			
		} else
			throw new BadCredentialsException("Usuário ou senha inválida.");
	}

	private boolean authorizedUser(Usuario usuario, String userName, String password){
		 if(usuario != null && usuario.getLogin().equals(userName) && usuario.getSenha().equals(password))
			return true;
		 
		 return false;
	}
	
	private Usuario verificarVinculo(Usuario usuario) {
		List<Empresa> empresas = usuario.getEmpresas();
		
		String schemaAcesso;
		
		if(!empresas.isEmpty() && empresas.size() == 1) {
			
			usuario.setSchemaAcesso(empresas.get(0).getNome());
        	
        	if(empresas.get(0).getNome().equalsIgnoreCase(Administrativo.SCHEMA_ACESSO_ADMIN))
        		usuario.setSchemaAcesso("public");
			
		}else {
			
			usuario.getPermissoes().forEach((p -> { 
				if(p.getId() == Papel.ROLE_ADMIN_GERAL) {
					usuario.setSchemaAcesso("public");
				}
			}));
		}
		
		return usuario;
	}
	
	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}