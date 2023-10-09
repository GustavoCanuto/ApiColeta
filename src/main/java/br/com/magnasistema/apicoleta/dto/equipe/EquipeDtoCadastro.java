package br.com.magnasistema.apicoleta.dto.equipe;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EquipeDtoCadastro(
		
		@NotBlank
		@Size(max = 255)
		String nome,

		@Size(max = 255)
		String descricao,

		@NotNull
		Long idVeiculo
)

{


}
