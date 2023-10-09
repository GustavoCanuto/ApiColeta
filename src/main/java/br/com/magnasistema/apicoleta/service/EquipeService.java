package br.com.magnasistema.apicoleta.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.magnasistema.apicoleta.dto.equipe.EquipeDtoCadastro;
import br.com.magnasistema.apicoleta.dto.equipe.EquipeDtoDetalhar;
import br.com.magnasistema.apicoleta.dto.equipe.EquipeDtoDetalharComFuncionarios;
import br.com.magnasistema.apicoleta.entity.Equipe;
import br.com.magnasistema.apicoleta.entity.Veiculo;
import br.com.magnasistema.apicoleta.repository.EquipeRepository;
import br.com.magnasistema.apicoleta.service.buscador.BuscarVeiculo;

@Service
public class EquipeService {

	@Autowired
	private EquipeRepository equipeRepository;

	@Autowired
	private BuscarVeiculo getVeiculo;

	public EquipeDtoDetalharComFuncionarios cadastrarEquipe(EquipeDtoCadastro dados) {
		
		Veiculo veiculo = getVeiculo.buscar(dados.idVeiculo());

		Equipe equipe = new Equipe(dados, veiculo);

		equipeRepository.save(equipe);

		return new EquipeDtoDetalharComFuncionarios(equipe);
	}

	public EquipeDtoDetalharComFuncionarios detalharEquipe(Long id) {

		return new EquipeDtoDetalharComFuncionarios(equipeRepository.getReferenceById(id));
		
	}

	public Page<EquipeDtoDetalhar> listarEquipeSimples(Pageable paginacao) {

		return equipeRepository.findAll(paginacao).map(EquipeDtoDetalhar::new);
	}

	public EquipeDtoDetalharComFuncionarios atualizarEquipe(EquipeDtoCadastro dados, Long id) {


		Veiculo veiculo = getVeiculo.buscar(dados.idVeiculo());
		
		Equipe equipe = equipeRepository.getReferenceById(id);

		equipe.atualizarInformacoes(dados, veiculo);

		equipeRepository.save(equipe);

		return new EquipeDtoDetalharComFuncionarios(equipe);
	}

	public void deletaCadastro(Long id) {

		equipeRepository.deleteById(id);

	}

}
