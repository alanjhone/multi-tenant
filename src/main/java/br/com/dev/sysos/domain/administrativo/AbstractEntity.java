package br.com.dev.sysos.domain.administrativo;

import java.io.Serializable;

import javax.print.attribute.standard.SheetCollate;

import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Entidade abstrata para modelo de persistência.
 *
 * @author Alan Jhone
 *
 */
public abstract class AbstractEntity implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * ID da entidade
     */
    private int id;

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
     * Método usado para obter o código hash de objetos
     *
     * @return
     */
    public int hashCode() {
        return (int)((getId() >> 32) ^ getId());
    }


    /**
     * Método usado para comparar objetos do tipo AbstractEntity.
     *
     * @return
     */
    public boolean equals(Object objeto) {
        boolean resultado = false;

        if (this == objeto) {
            resultado = true;
        } else if (objeto == null) {
            resultado = false;
        } else if (getClass() != objeto.getClass()) {
            resultado = false;
        } else {
            resultado = (this.getId() == ((AbstractEntity) objeto).getId());
        }

        return resultado;
    }
    
}
