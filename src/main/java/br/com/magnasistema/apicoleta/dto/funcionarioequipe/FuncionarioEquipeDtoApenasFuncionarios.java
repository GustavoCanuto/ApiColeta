package br.com.magnasistema.apicoleta.dto.funcionarioequipe;

import br.com.magnasistema.apicoleta.dto.funcionario.FuncionarioDtoDetalhar;
import br.com.magnasistema.apicoleta.entity.FuncionarioEquipe;


public record FuncionarioEquipeDtoApenasFuncionarios (
		long idFuncionarioEquipe,
		
		FuncionarioDtoDetalhar funcionario
		
		)

{

	public FuncionarioEquipeDtoApenasFuncionarios(FuncionarioEquipe funcionarioEquipe ) {
		this(funcionarioEquipe.getId(), new FuncionarioDtoDetalhar(funcionarioEquipe.getFuncionario()));
	}

	



}
