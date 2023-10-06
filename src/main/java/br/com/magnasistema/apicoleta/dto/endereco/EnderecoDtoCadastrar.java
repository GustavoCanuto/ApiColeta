package br.com.magnasistema.apicoleta.dto.endereco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record EnderecoDtoCadastrar(

		@NotNull Long idCidade,

		@NotBlank String logradouro,

		@NotNull String numero,

		@NotNull @Pattern(regexp = "\\d{8}") String cep) {

}
