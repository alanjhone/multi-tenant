/**
 * 
 */
package br.com.dev.sysos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.dev.sysos.dao.UsuarioEmpresaDao;
import br.com.dev.sysos.domain.administrativo.UsuarioEmpresa;

/**
 * @author Alan Jhone
 *
 */

@Component
public class UsuarioEmpresaService  extends CrudService<UsuarioEmpresa>{

	@Autowired
	private UsuarioEmpresaDao usuarioEmpresaDao;
	
}
