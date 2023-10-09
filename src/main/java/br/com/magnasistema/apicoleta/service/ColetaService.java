package br.com.magnasistema.apicoleta.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.magnasistema.apicoleta.dto.coleta.ColetaDtoAtualizar;
import br.com.magnasistema.apicoleta.dto.coleta.ColetaDtoCadastro;
import br.com.magnasistema.apicoleta.dto.coleta.ColetaDtoDetalhar;
import br.com.magnasistema.apicoleta.dto.coleta.ColetaDtoModificar;
import br.com.magnasistema.apicoleta.entity.Bairro;
import br.com.magnasistema.apicoleta.entity.Coleta;
import br.com.magnasistema.apicoleta.entity.Destino;
import br.com.magnasistema.apicoleta.entity.Equipe;
import br.com.magnasistema.apicoleta.repository.ColetaRepository;
import br.com.magnasistema.apicoleta.service.buscador.BuscarBairro;
import br.com.magnasistema.apicoleta.service.buscador.BuscarDestino;
import br.com.magnasistema.apicoleta.service.buscador.BuscarEquipe;
import br.com.magnasistema.apicoleta.validacoes.ValidacaoException;
import br.com.magnasistema.apicoleta.validacoes.coleta.atualizar.ValidadorAtualizacaoColeta;
import br.com.magnasistema.apicoleta.validacoes.coleta.cadastro.ValidadorCadastroColeta;

@Service
public class ColetaService {

	@Autowired
	private ColetaRepository coletaRepository;

	@Autowired
	private BuscarEquipe getEquipe;

	@Autowired
	private BuscarBairro getBairro;

	@Autowired
	private BuscarDestino getDestino;

	@Autowired
	private List<ValidadorCadastroColeta> validadoresCadastro;

	@Autowired
	private List<ValidadorAtualizacaoColeta> validadoresAtualizacao;

	public ColetaDtoDetalhar cadastrarColeta(ColetaDtoCadastro dados) {

		validarDados(dados);

		Bairro bairro = getBairro.buscar(dados.idBairro());

		Equipe equipe = getEquipe.buscar(dados.idEquipe());

		Destino destino = getDestino.buscar(dados.idDestino());

		validadoresCadastro.forEach(v -> v.validar(dados));

		var coleta = new Coleta(dados, equipe, bairro, destino);

		coletaRepository.save(coleta);

		return new ColetaDtoDetalhar(coleta);
	}

	public Page<ColetaDtoDetalhar> listarColetas(Pageable paginacao) {

		return coletaRepository.findAll(paginacao).map(ColetaDtoDetalhar::new);

	}

	public ColetaDtoDetalhar detalharColeta(Long id) {

		return new ColetaDtoDetalhar(coletaRepository.getReferenceById(id));
	}

	public Page<ColetaDtoDetalhar> listarColetaPorEquipe(Pageable paginacao, Long id) {

		return coletaRepository.findByEquipeIdOrderByIdDesc(id, paginacao).map(ColetaDtoDetalhar::new);

	}

	public ColetaDtoDetalhar atualizarCadastro(ColetaDtoAtualizar dados, Long id) {

		validadoresAtualizacao.forEach(v -> v.validar(dados, id));

		var coleta = coletaRepository.getReferenceById(id);

		coleta.atualizarInformacoes(dados);

		coletaRepository.save(coleta);

		return new ColetaDtoDetalhar(coleta);
	}

	public ColetaDtoDetalhar modificarColeta(ColetaDtoModificar dados, Long id) {

		Destino destino = getDestino.buscar(dados.idDestino());

		Coleta coleta = coletaRepository.getReferenceById(id);

		coleta.modificarInformacoes(dados, destino);

		coletaRepository.save(coleta);

		return new ColetaDtoDetalhar(coleta);
	}

	public void deletaCadastro(Long id) {

		coletaRepository.deleteById(id);

	}

	private void validarDados(ColetaDtoCadastro dados) {

		if (dados.timestampFinal() != null) {

			if (dados.timestampFinal().isBefore(dados.timestampInicial())
					|| dados.timestampFinal().isEqual(dados.timestampInicial())) {
				throw new ValidacaoException("O timestampFinal deve ser depois do timestampInicial.");
			}

			if (dados.peso() == null) {
				throw new ValidacaoException("Se a coleta foi finalizada é necessario informar o peso");
			}

			return;

		}

		if (dados.peso() != null) {
			throw new ValidacaoException(
					"Para adicionar peso da coleta é necessario informar o timestampFinal da coleta");
		}

	}

}
