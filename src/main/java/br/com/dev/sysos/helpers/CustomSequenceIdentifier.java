/**
 * 
 */
package br.com.dev.sysos.helpers;

import java.io.Serializable;
import java.util.Properties;

import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.SequenceGenerator;
import org.hibernate.type.Type;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.dev.sysos.domain.administrativo.Usuario;

/**
 * @author Alan Jhone
 *
 */
public class CustomSequenceIdentifier extends SequenceGenerator {
	
    @Override
    public void configure(Type type, Properties params, Dialect dialect) throws MappingException {

    	params.setProperty("schema", getSchemaAcessoUsuarioLogado());
    	params.setProperty("sequence", getSchemaAcessoUsuarioLogado()+"."+params.getProperty("sequence_name"));
    	
    	super.configure(type, params, dialect);
    }
	
    @Override
    public Serializable generate(SessionImplementor session, Object obj) {
		return super.generate(session, obj);
    }
    
    public String getSchemaAcessoUsuarioLogado() {
    	String schema = "public";
    	
    	try {
        	Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        	if(usuarioLogado.getSchemaAcesso() != "")
        		schema = usuarioLogado.getSchemaAcesso();
		} catch (NullPointerException e) {
			
		}
    	
        return schema;
    }

}