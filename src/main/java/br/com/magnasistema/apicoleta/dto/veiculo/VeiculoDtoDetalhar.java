package br.com.magnasistema.apicoleta.dto.veiculo;

import br.com.magnasistema.apicoleta.entity.Empresa;
import br.com.magnasistema.apicoleta.entity.Veiculo;
import br.com.magnasistema.apicoleta.enums.TipoVeiculo;

public record VeiculoDtoDetalhar (
		Long id,
		
		TipoVeiculo tipo,
	
		double capacidade,
	
		String placa,
		
		int anoFabricacao,
		
		Empresa empresa
		)

{

	public VeiculoDtoDetalhar(Veiculo veiculo) {
		this(veiculo.getId(), veiculo.getTipo(),veiculo.getCapacidade(),veiculo.getPlaca(),veiculo.getAnoFabricacao(), veiculo.getEmpresa());

	}

}
