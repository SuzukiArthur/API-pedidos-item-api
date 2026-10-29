package br.edu.si.pedidoitemapi.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ErroResponse(
        LocalDateTime timestamp,
        int status,
        String erro,
        String mensagem,
        List<String> detalhes) {

    public static ErroResponse de(int status, String erro, String mensagem, List<String> detalhes) {
        return new ErroResponse(LocalDateTime.now(), status, erro, mensagem, detalhes);
    }
}
