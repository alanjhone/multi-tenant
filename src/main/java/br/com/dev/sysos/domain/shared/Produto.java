/**
 * 
 */
package br.com.dev.sysos.domain.shared;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.dev.sysos.domain.administrativo.AbstractEntity;

/**
 * @author Alan Jhone
 *
 */
@Entity
@Table(name="produto")
public class Produto extends AbstractEntity{

	private static final long serialVersionUID = 1L;
	
    /**
     * ID da entidade
     */
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_PRODUTO")
    @SequenceGenerator(name="SEQ_PRODUTO", sequenceName="public.id_seq_produto", allocationSize=1)
    @Column(name="id_produto")
    private int id;

    /**
     * Nome da pessoa
     */
    @Column(nullable=false, columnDefinition="varchar(180)")
    private String nome;

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
    
    
    
}
