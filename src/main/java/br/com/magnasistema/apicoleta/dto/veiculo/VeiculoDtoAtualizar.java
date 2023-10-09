package br.com.magnasistema.apicoleta.dto.veiculo;

import br.com.magnasistema.apicoleta.enums.TipoVeiculo;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

public record VeiculoDtoAtualizar(

		TipoVeiculo tipo,

		Double capacidade,

		@Pattern(regexp = "[A-Za-z0-9]{7,8}", message = "A placa deve conter de 7 a 8 caracteres alfanuméricos.") 
		String placa,

		@Min(value = 1900, message = "O ano de fabricação deve ser maior ou igual a 1900.") 
		@Max(value = 2099, message = "O ano de fabricação deve ser menor ou igual a 2099.") 
		Integer anoFabricacao,

		Long idEmpresa

)

{



}
