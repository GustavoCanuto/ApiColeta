package br.com.magnasistema.apicoleta.infra.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.magnasistema.apicoleta.validacoes.ValidacaoException;
import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class TratadorDeErros {

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<String> tratarErro404() {

		return ResponseEntity.notFound().build();

	}

	@ExceptionHandler(ValidacaoException.class)
	public ResponseEntity<String> tratarErroRegraDeNegocio(ValidacaoException ex) {

		return ResponseEntity.badRequest().body(ex.getMessage());

	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> tratarErro500(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro: " + ex.getLocalizedMessage());
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<String> tratarErroForeignKeyConstraint(DataIntegrityViolationException ex) {

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body("Não é possível excluir devido existir coleta registrada com essa entidade.");

	}


}
