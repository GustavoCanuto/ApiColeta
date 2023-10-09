package br.com.magnasistema.apicoleta.dto.funcionario;

import java.time.LocalDate;

import br.com.magnasistema.apicoleta.enums.TipoFuncao;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record FuncionarioDtoCadastro(
		
		@NotBlank
		@Size(max = 255)
		String nomeCompleto,
		
		@NotNull
		@Pattern(regexp = "\\d{11}", message = "O CPF deve conter 11 numeros.")
		String cpf,
		
		@NotNull
		@Past(message = "A data de nascimento deve ser no passado.")
		LocalDate dataNascimento,
		
		@NotNull
		@Email(message = "O email deve ser válido.")
		String email,
		
		@NotNull
		TipoFuncao funcao,
		
		@NotNull
		Long idEmpresa
		) {

}
