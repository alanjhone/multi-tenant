/**
 * 
 */
package br.com.dev.sysos.domain.administrativo;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author Alan Jhone
 *
 */

@Entity
@Table(name="usuario_empresa", schema = "public")
public class UsuarioEmpresa extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USUARIO_EMPRESA")
    @SequenceGenerator(name = "SEQ_USUARIO_EMPRESA", sequenceName = "public.id_seq_usuario_empresa", allocationSize = 1)
    @Column(name = "id_usuario_empresa")
    private int id;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_usuario")  
    private Usuario usuario;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_empresa")
    private Empresa empresa;
    
    /**
     * Permissões
     */
    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="permissao", schema = "public", joinColumns = @JoinColumn(name="id_usuario_empresa"), inverseJoinColumns = @JoinColumn(name="id_papel"))
    private List<Papel> permissoes;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the usuario
	 */
	public Usuario getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the empresa
	 */
	public Empresa getEmpresa() {
		return empresa;
	}

	/**
	 * @param empresa the empresa to set
	 */
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	/**
	 * @return the permissoes
	 */
	public List<Papel> getPermissoes() {
		return permissoes;
	}

	/**
	 * @param permissoes the permissoes to set
	 */
	public void setPermissoes(List<Papel> permissoes) {
		this.permissoes = permissoes;
	}

    
	
}
