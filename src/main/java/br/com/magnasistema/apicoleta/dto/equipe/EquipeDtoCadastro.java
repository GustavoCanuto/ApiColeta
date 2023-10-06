package br.com.magnasistema.apicoleta.dto.equipe;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EquipeDtoCadastro(
		
		@NotBlank
		String nome,

		String descricao,

		@NotNull
		Long idVeiculo
)

{


}
