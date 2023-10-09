package br.com.magnasistema.apicoleta.dto.endereco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EnderecoDtoCadastrar(

		@NotNull Long idCidade,

		@Size(max = 255)
		@NotBlank String logradouro,

		@Size(max = 8)
		@NotNull String numero,

		@NotNull @Pattern(regexp = "\\d{8}") String cep) {

}
