package br.com.magnasistema.apicoleta.dto.funcionario;

import java.time.LocalDate;

import br.com.magnasistema.apicoleta.enums.TipoFuncao;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

public record FuncionarioDtoAtualizar(
	
		
		String nomeCompleto,
	
		@Pattern(regexp = "\\d{9,12}", message = "O CNPJ deve conter de 9 a 12 dígitos.")
		String cpf,

		@Past(message = "A data de nascimento deve ser no passado.")
		LocalDate dataNascimento,
	
		@Email(message = "O email deve ser válido.")
		String email,

		TipoFuncao funcao,
	
		Long idEmpresa) {



}
