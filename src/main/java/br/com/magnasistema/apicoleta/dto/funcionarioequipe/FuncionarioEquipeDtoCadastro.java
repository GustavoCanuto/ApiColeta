package br.com.magnasistema.apicoleta.dto.funcionarioequipe;

import jakarta.validation.constraints.NotNull;

public record FuncionarioEquipeDtoCadastro(
		
		@NotNull
		Long idFuncionario,
		
		@NotNull
		Long idEquipe
		
		) {

}
