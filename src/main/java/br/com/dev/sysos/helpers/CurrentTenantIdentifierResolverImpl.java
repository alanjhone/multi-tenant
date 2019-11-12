/**
 * 
 */
package br.com.dev.sysos.helpers;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.dev.sysos.domain.administrativo.Usuario;

/**
 * 
 * 
 * @author Alan Jhone
 *
 */
public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {

	private final String DEFAULT_TENANT_ID = "public";
	
	@Override
	public String resolveCurrentTenantIdentifier() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String schema = DEFAULT_TENANT_ID;
		
		if(authentication != null && authentication.getPrincipal() instanceof UserDetails){
			Usuario usuario = (Usuario) authentication.getPrincipal();
			if(usuario.getSchemaAcesso() != null && usuario.getSchemaAcesso() != "")
				schema = usuario.getSchemaAcesso();
		}
		
		return schema;
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		return true;
	}

}