package br.com.magnasistema.apicoleta.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.magnasistema.apicoleta.dto.destino.DestinoDtoAtualizar;
import br.com.magnasistema.apicoleta.dto.destino.DestinoDtoCadastro;
import br.com.magnasistema.apicoleta.dto.destino.DestinoDtoDetalhar;
import br.com.magnasistema.apicoleta.entity.Cidade;
import br.com.magnasistema.apicoleta.entity.Destino;
import br.com.magnasistema.apicoleta.entity.Empresa;
import br.com.magnasistema.apicoleta.repository.DestinoRepository;
import br.com.magnasistema.apicoleta.service.buscador.BuscarCidade;
import br.com.magnasistema.apicoleta.service.buscador.BuscarEmpresa;

@Service
public class DestinoService {

	@Autowired
	private DestinoRepository destinoRepository;

	@Autowired
	private BuscarCidade getCidade;

	@Autowired
	private BuscarEmpresa getEmpresa;

	public DestinoDtoDetalhar cadastrarDestino(DestinoDtoCadastro dados) {

		Cidade cidade = getCidade.buscar(dados.endereco().idCidade());

		Empresa empresa = getEmpresa.buscar(dados.idEmpresa());

		Destino destino = new Destino(dados, cidade, empresa);

		destinoRepository.save(destino);

		return new DestinoDtoDetalhar(destino);
	}

	public Page<DestinoDtoDetalhar> listarDestinos(Pageable paginacao) {

		return destinoRepository.findAll(paginacao).map(DestinoDtoDetalhar::new);

	}

	public DestinoDtoDetalhar detalharDestino(Long id) {

		return new DestinoDtoDetalhar(destinoRepository.getReferenceById(id));
	}

	public DestinoDtoDetalhar atualizarCadastro(DestinoDtoAtualizar dados, Long id) {

		Cidade cidade = null;

		if (dados.endereco() != null) {

			cidade = getCidade.buscar(dados.endereco().idCidade());
		}

		Empresa empresa = getEmpresa.buscar(dados.idEmpresa());

		Destino destino = destinoRepository.getReferenceById(id);

		destino.atualizarInformacoes(dados, cidade, empresa);

		destinoRepository.save(destino);

		return new DestinoDtoDetalhar(destino);

	}

	public void deletaCadastro(Long id) {

		destinoRepository.deleteById(id);

	}

}
