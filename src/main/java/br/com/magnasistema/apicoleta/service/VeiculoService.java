package br.com.magnasistema.apicoleta.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.magnasistema.apicoleta.dto.veiculo.VeiculoDtoAtualizar;
import br.com.magnasistema.apicoleta.dto.veiculo.VeiculoDtoCadastro;
import br.com.magnasistema.apicoleta.dto.veiculo.VeiculoDtoDetalhar;
import br.com.magnasistema.apicoleta.entity.Empresa;
import br.com.magnasistema.apicoleta.entity.Veiculo;
import br.com.magnasistema.apicoleta.enums.TipoVeiculo;
import br.com.magnasistema.apicoleta.repository.VeiculoRepository;
import br.com.magnasistema.apicoleta.service.buscador.BuscarEmpresa;
import br.com.magnasistema.apicoleta.validacoes.ValidacaoException;

@Service
public class VeiculoService {

	@Autowired
	private VeiculoRepository veiculoRepository;

	@Autowired
	private BuscarEmpresa getEmpresa;

	public VeiculoDtoDetalhar cadastrarVeiculo(VeiculoDtoCadastro dados) {

		validaDuplicadas(dados);

		Empresa empresa = getEmpresa.buscar(dados.idEmpresa());

		Veiculo veiculo = new Veiculo(dados, empresa);

		veiculoRepository.save(veiculo);

		return new VeiculoDtoDetalhar(veiculo);
	}

	public Page<VeiculoDtoDetalhar> listarVeiculos(Pageable paginacao, Double capacidade, TipoVeiculo tipo) {

		if (capacidade != null && tipo != null) {

			return veiculoRepository.findByCapacidadeGreaterThanAndTipo(capacidade, tipo, paginacao)
					.map(VeiculoDtoDetalhar::new);
		}

		if (capacidade != null) {

			return veiculoRepository.findByCapacidadeGreaterThan(capacidade, paginacao).map(VeiculoDtoDetalhar::new);
		}

		if (tipo != null) {

			return veiculoRepository.findByTipo(tipo, paginacao).map(VeiculoDtoDetalhar::new);
		}

		return veiculoRepository.findAll(paginacao).map(VeiculoDtoDetalhar::new);
	}

	public Page<VeiculoDtoDetalhar> listarVeiculosPorEmpresa(Pageable paginacao, Double capacidade, TipoVeiculo tipo,
			long idEmpresa) {

		if (capacidade != null && tipo != null) {

			return veiculoRepository
					.findByEmpresaIdAndCapacidadeGreaterThanAndTipo(idEmpresa, capacidade, tipo, paginacao)
					.map(VeiculoDtoDetalhar::new);
		}

		if (capacidade != null) {

			return veiculoRepository.findByEmpresaIdAndCapacidadeGreaterThan(idEmpresa, capacidade, paginacao)
					.map(VeiculoDtoDetalhar::new);
		}

		if (tipo != null) {

			return veiculoRepository.findByEmpresaIdAndTipo(idEmpresa, tipo, paginacao).map(VeiculoDtoDetalhar::new);
		}

		return veiculoRepository.findByEmpresaId(idEmpresa, paginacao).map(VeiculoDtoDetalhar::new);
	}

	public VeiculoDtoDetalhar detalharVeiculo(Long id) {

		return new VeiculoDtoDetalhar(veiculoRepository.getReferenceById(id));

	}

	public VeiculoDtoDetalhar atualizarCadastro(VeiculoDtoAtualizar dados, long id) {

		validaDuplicadas(dados);

		Empresa empresa = getEmpresa.buscar(dados.idEmpresa());

		Veiculo veiculo = veiculoRepository.getReferenceById(id);

		veiculo.atualizarInformacoes(dados, empresa);

		veiculoRepository.save(veiculo);

		return new VeiculoDtoDetalhar(veiculo);
	}

	public void deletaCadastro(Long id) {

		veiculoRepository.deleteById(id);

	}

	private void validaDuplicadas(VeiculoDtoCadastro dados) {
		if (veiculoRepository.existsByPlaca(dados.placa())) {
			throw new ValidacaoException("Placa de veiculo já registrado!");
		}
	}

	private void validaDuplicadas(VeiculoDtoAtualizar dados) {
		if (veiculoRepository.existsByPlaca(dados.placa())) {
			throw new ValidacaoException("Placa de veiculo já registrado!");
		}
	}

}
