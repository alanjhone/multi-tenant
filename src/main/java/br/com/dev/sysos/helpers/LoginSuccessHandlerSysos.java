package br.com.dev.sysos.helpers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import br.com.dev.sysos.domain.administrativo.Empresa;
import br.com.dev.sysos.domain.administrativo.Papel;
import br.com.dev.sysos.domain.administrativo.Usuario;
import br.com.dev.sysos.services.UsuarioService;

/**
 * LoginSuccess 
 * 
 * @author Alan Jhone
 *
 */
public class LoginSuccessHandlerSysos implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		Usuario usuario = (Usuario) authentication.getPrincipal();
		
		if(verificarAcessoAdministradorGeral(usuario) || (!usuario.getEmpresas().isEmpty() && usuario.getEmpresas().size() == 1)){
			response.sendRedirect("pages/home.jsf");
		}else {
			
			response.sendRedirect("pages/selecao_vinculo.jsf");
			
		}
		
	}
	
    public boolean verificarAcessoAdministradorGeral(Usuario usuario) {      
    	
	    if(usuario != null && !usuario.getPermissoes().isEmpty()) {
	        Papel permissao = usuario.getPermissoesCurrentEmpresa(usuario.getSchemaAcesso()).stream()
	        .filter( papel -> Papel.ROLE_ADMIN_GERAL == papel.getId())
	        .findAny()
	        .orElse(null);
	
	        if(permissao != null)
	            return true;
    	}
        
        return false;
    }

}
