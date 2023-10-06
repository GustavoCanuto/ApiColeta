package br.com.magnasistema.apicoleta.dto.funcionarioequipe;

import br.com.magnasistema.apicoleta.dto.equipe.EquipeDtoDetalhar;
import br.com.magnasistema.apicoleta.entity.FuncionarioEquipe;



public record FuncionarioEquipeDtoApenasEquipes (

		long idFuncionarioEquipe,
		
		EquipeDtoDetalhar equipe 
		
	
		)

{
	public FuncionarioEquipeDtoApenasEquipes(FuncionarioEquipe funcionarioEquipe) {
		this(funcionarioEquipe.getId(), new EquipeDtoDetalhar(funcionarioEquipe.getEquipe()));
	}



}
