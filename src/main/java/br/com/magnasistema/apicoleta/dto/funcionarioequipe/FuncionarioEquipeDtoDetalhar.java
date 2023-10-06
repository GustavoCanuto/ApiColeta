package br.com.magnasistema.apicoleta.dto.funcionarioequipe;

import java.util.List;

import br.com.magnasistema.apicoleta.entity.Equipe;
import br.com.magnasistema.apicoleta.entity.Funcionario;
import br.com.magnasistema.apicoleta.entity.FuncionarioEquipe;

public record FuncionarioEquipeDtoDetalhar(
		
		long id,
		
		Funcionario funcionario,
		
		Equipe equipe,

		List<FuncionarioEquipeDtoApenasFuncionarios> funcionarios

)

{

	public FuncionarioEquipeDtoDetalhar(FuncionarioEquipe funcionarioEquipe) {
		
	
		this(funcionarioEquipe.getId(), funcionarioEquipe.getFuncionario(), funcionarioEquipe.getEquipe(), 
				funcionarioEquipe.getEquipe().getFuncionarioEquipe().stream()
	            .map(FuncionarioEquipeDtoApenasFuncionarios::new)
	            .toList());

}
}