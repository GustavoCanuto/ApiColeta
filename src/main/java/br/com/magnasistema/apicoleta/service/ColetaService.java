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
import br.com.magnasistema.apicoleta.entity.Coleta;
import br.com.magnasistema.apicoleta.entity.Destino;
import br.com.magnasistema.apicoleta.repository.BairroRepository;
import br.com.magnasistema.apicoleta.repository.ColetaRepository;
import br.com.magnasistema.apicoleta.repository.DestinoRepository;
import br.com.magnasistema.apicoleta.repository.EquipeRepository;
import br.com.magnasistema.apicoleta.validacoes.ValidacaoException;
import br.com.magnasistema.apicoleta.validacoes.coleta.atualizar.ValidadorAtualizacaoColeta;
import br.com.magnasistema.apicoleta.validacoes.coleta.cadastro.ValidadorCadastroColeta;

@Service
public class ColetaService {

	@Autowired
	private ColetaRepository coletaRepository;

	@Autowired
	private EquipeRepository equipeRepository;

	@Autowired
	private BairroRepository bairroRepository;

	@Autowired
	private DestinoRepository destinoRepository;

	@Autowired
	private List<ValidadorCadastroColeta> validadoresCadastro;

	@Autowired
	private List<ValidadorAtualizacaoColeta> validadoresAtualizacao;

	public ColetaDtoDetalhar cadastrarColeta(ColetaDtoCadastro dados) {

		validarDados(dados);

		var equipe = equipeRepository.findById(dados.idEquipe())
				.orElseThrow(() -> new ValidacaoException("Id da Equipe não encontrado"));

		var bairro = bairroRepository.findById(dados.idBairro())
				.orElseThrow(() -> new ValidacaoException("Id do Bairro não encontrado"));

		var destino = destinoRepository.findById(dados.idDestino())
				.orElseThrow(() -> new ValidacaoException("Id do Destino não encontrado"));

		validadoresCadastro.forEach(v -> v.validar(dados));

		var coleta = new Coleta(dados, equipe, bairro, destino);

		coletaRepository.save(coleta);
 
		return new ColetaDtoDetalhar(coleta);
	}

	public ColetaDtoDetalhar atualizarCadastro(ColetaDtoAtualizar dados, Long id) {

		validadoresAtualizacao.forEach(v -> v.validar(dados, id));

		var coleta = coletaRepository.getReferenceById(id);

		coleta.atualizarInformacoes(dados);

		coletaRepository.save(coleta);

		return new ColetaDtoDetalhar(coleta);
	}

	public Page<ColetaDtoDetalhar> listarColetas(Pageable paginacao) {

		return coletaRepository.findAll(paginacao).map(ColetaDtoDetalhar::new);

	}

	public ColetaDtoDetalhar detalharColeta(Long id) {

		return new ColetaDtoDetalhar(coletaRepository.getReferenceById(id));
	}

	public void deletaCadastro(Long id) {

		coletaRepository.deleteById(id);

	}

	public Page<ColetaDtoDetalhar> listarColetaPorEquipe(Pageable paginacao, Long id) {

		return coletaRepository.findByEquipeIdOrderByIdDesc(id, paginacao).map(ColetaDtoDetalhar::new);

	}

	public ColetaDtoDetalhar modificarColeta(ColetaDtoModificar dados, Long id) {

		Destino destino = null;

		if (dados.idDestino() != null) {

			destino = destinoRepository.findById(dados.idDestino())
					.orElseThrow(() -> new ValidacaoException("Id do Destino não encontrado"));

		}
		var coleta = coletaRepository.getReferenceById(id);

		coleta.modificarInformacoes(dados, destino);

		coletaRepository.save(coleta);

		return new ColetaDtoDetalhar(coleta);
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