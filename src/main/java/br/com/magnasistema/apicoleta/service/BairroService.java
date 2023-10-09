package br.com.magnasistema.apicoleta.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.magnasistema.apicoleta.dto.bairro.BairroDtoCadastro;
import br.com.magnasistema.apicoleta.dto.bairro.BairroDtoDetalhar;
import br.com.magnasistema.apicoleta.entity.Bairro;
import br.com.magnasistema.apicoleta.entity.Cidade;
import br.com.magnasistema.apicoleta.repository.BairroRepository;
import br.com.magnasistema.apicoleta.service.buscador.BuscarCidade;

@Service
public class BairroService {

	@Autowired
	private BairroRepository bairroRepository;

	@Autowired
	private BuscarCidade getCidade;

	public BairroDtoDetalhar cadastrarBairro(BairroDtoCadastro dados) {

		 Cidade cidade = getCidade.buscar(dados.idCidade());

		Bairro bairro = new Bairro(dados, cidade); 

		bairroRepository.save(bairro);

		return new BairroDtoDetalhar(bairro);

	}

	public Page<BairroDtoDetalhar> listarBairros(Pageable paginacao, String nome) {

		if ((nome != null && !nome.isBlank())) {

			return bairroRepository.findByNomeContainingIgnoreCase(nome, paginacao).map(BairroDtoDetalhar::new);

		}

		return bairroRepository.findAll(paginacao).map(BairroDtoDetalhar::new);

	}

	public Page<BairroDtoDetalhar> listarBairrosPorCidade(Long id, String nome, Pageable paginacao) {

		if ((nome != null && !nome.isBlank())) {

			return bairroRepository.findByCidadeIdAndNomeContainingIgnoreCase(id, nome, paginacao)
					.map(BairroDtoDetalhar::new);

		}

		return bairroRepository.findByCidadeId(id, paginacao).map(BairroDtoDetalhar::new);

	}

	public BairroDtoDetalhar detalharBairro(Long id) {

		return new BairroDtoDetalhar(bairroRepository.getReferenceById(id));

	}

	public BairroDtoDetalhar atualizarCadastro(BairroDtoCadastro dados, long id) {

		Cidade cidade = getCidade.buscar(dados.idCidade());

		Bairro bairro = bairroRepository.getReferenceById(id);

		bairro.atualizarInformacoes(dados, cidade);

		bairroRepository.save(bairro);

		return new BairroDtoDetalhar(bairro);

	}

	public void deletaCadastro(Long id) {

		bairroRepository.deleteById(id);

	}

}
