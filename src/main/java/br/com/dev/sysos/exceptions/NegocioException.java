package br.com.dev.sysos.exceptions;

/**
 * Tratamento de regras de negócio.
 *
 * @author Alan
 */
public class NegocioException extends Exception {
    public NegocioException(String mensagem) {
        super(mensagem);
    }
}
