package br.com.magnasistema.apicoleta.dto.empresa;

import br.com.magnasistema.apicoleta.enums.TipoEmpresa;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EmpresaDtoCadastrar(
		
	
		@NotBlank
		@Size(max = 255)
		String nome,
		
		@NotBlank
		@Pattern(regexp = "\\d{14,20}", message = "O CNPJ deve conter de 14 a 20 dígitos.")
		String cnpj,
		
		
		@NotBlank
		@Pattern(regexp = "\\d{8,14}", message = "O telefone deve conter de 8 a 14 dígitos.")
		String telefone,
	
		@NotBlank
		@Email(message = "O email deve ser válido.")
		String email,
		
		@NotNull
		TipoEmpresa tipo
		) {



}
