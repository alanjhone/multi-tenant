package br.com.dev.sysos.exceptions;

/**
 * 
 * Class para tratamento de exceções
 * 
 * @author Alan Jhone
 *
 */
public class SegurancaException extends Exception{
    public SegurancaException(String mensagem) {
        super(mensagem);
    }
}
