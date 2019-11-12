package br.com.dev.sysos.domain.administrativo;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Entidade que representa os empresas
 *
 * @author Alan Jhone
 *
 */

@Entity
@Table(name="empresa", schema="public")
public class Empresa extends AbstractEntity{

    private static final long serialVersionUID = -1370492530379526895L;

    /**
     * ID da entidade
     */
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_EMPRESA")
    @SequenceGenerator(name="SEQ_EMPRESA", sequenceName="public.id_seq_empresa", allocationSize=1)
    @Column(name="id_empresa")
    private int id;
    
    @Column(name="nome")
    private String nome;

    @Column(name="cnpj")
    private String cnpj;
    
    @OneToMany(mappedBy = "empresa")
    private List<UsuarioEmpresa> usuariosEmpresa;
    
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
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the cnpj
	 */
	public String getCnpj() {
		return cnpj;
	}

	/**
	 * @param cnpj the cnpj to set
	 */
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	/**
	 * @return the usuariosEmpresa
	 */
	public List<UsuarioEmpresa> getUsuariosEmpresa() {
		return usuariosEmpresa;
	}

	/**
	 * @param usuariosEmpresa the usuariosEmpresa to set
	 */
	public void setUsuariosEmpresa(List<UsuarioEmpresa> usuariosEmpresa) {
		this.usuariosEmpresa = usuariosEmpresa;
	}
	
	
	
}
