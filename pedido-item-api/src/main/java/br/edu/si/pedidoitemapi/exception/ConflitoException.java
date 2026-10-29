package br.edu.si.pedidoitemapi.exception;

public class ConflitoException extends RuntimeException {

    public ConflitoException(String mensagem) {
        super(mensagem);
    }
}
