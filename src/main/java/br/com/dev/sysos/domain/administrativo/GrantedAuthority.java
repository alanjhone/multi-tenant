package br.com.dev.sysos.domain.administrativo;

import java.io.Serializable;

/**
 * Entidade abstrata para modelo de persistência.
 *
 * @author Alan Jhone
 *
 */
public interface GrantedAuthority extends Serializable {
    String getAuthority();
}