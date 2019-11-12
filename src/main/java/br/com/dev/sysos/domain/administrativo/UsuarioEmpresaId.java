/**
 * 
 */
package br.com.dev.sysos.domain.administrativo;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author Alan Jhone
 *
 */

@Embeddable
public class UsuarioEmpresaId extends AbstractEntity {
 
    @Column(name = "id_usuario")
    private int usuarioId;
 
    @Column(name = "id_empresa")
    private int empresaId;
 
    public UsuarioEmpresaId() {}
    
    public UsuarioEmpresaId(
        int usuarioId,
        int empresaId) {
        this.usuarioId = usuarioId;
        this.empresaId = empresaId;
    }

}