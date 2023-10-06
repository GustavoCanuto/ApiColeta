package br.com.magnasistema.apicoleta.dto.equipe;

import java.util.List;

import br.com.magnasistema.apicoleta.dto.funcionarioequipe.FuncionarioEquipeDtoApenasFuncionarios;
import br.com.magnasistema.apicoleta.entity.Equipe;
import br.com.magnasistema.apicoleta.entity.Veiculo;

public record EquipeDtoDetalharComFuncionarios(long id,

		String nome,

		String descricao,

		Veiculo veiculo,

		List<FuncionarioEquipeDtoApenasFuncionarios> funcionarios

)

{

	public EquipeDtoDetalharComFuncionarios(Equipe equipe) {
		
	
		this(equipe.getId(), equipe.getNome(), equipe.getDescricao(), equipe.getVeiculo(), 
				equipe.getFuncionarioEquipe().stream()
	            .map(FuncionarioEquipeDtoApenasFuncionarios::new)
	            .toList());

}
}
