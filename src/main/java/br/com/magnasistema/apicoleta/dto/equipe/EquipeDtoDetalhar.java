package br.com.magnasistema.apicoleta.dto.equipe;

import br.com.magnasistema.apicoleta.entity.Equipe;
import br.com.magnasistema.apicoleta.entity.Veiculo;

public record EquipeDtoDetalhar(long id,

		String nome,

		String descricao,

		Veiculo veiculo

)

{

	public EquipeDtoDetalhar(Equipe equipe) {

		this(equipe.getId(), equipe.getNome(), equipe.getDescricao(), equipe.getVeiculo());

	}
}