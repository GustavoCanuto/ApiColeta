package br.com.magnasistema.apicoleta.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.magnasistema.apicoleta.dto.empresa.EmpresaDtoAtualizar;
import br.com.magnasistema.apicoleta.dto.empresa.EmpresaDtoCadastrar;
import br.com.magnasistema.apicoleta.dto.empresa.EmpresaDtoDetalhar;
import br.com.magnasistema.apicoleta.entity.Empresa;
import br.com.magnasistema.apicoleta.repository.EmpresaRepository;
import br.com.magnasistema.apicoleta.validacoes.ValidacaoException;

@Service
public class EmpresaService {

	@Autowired
	private EmpresaRepository empresaRepository;

	public EmpresaDtoDetalhar cadastrarEmpresa(EmpresaDtoCadastrar dados) {

		validaDuplicadas(dados); 

		Empresa empresa = new Empresa(dados);

		empresaRepository.save(empresa);

		return new EmpresaDtoDetalhar(empresa);

	}



	public Page<EmpresaDtoDetalhar> listarEmpresas(Pageable paginacao, String nome) {

		if (nome != null && !nome.isBlank()) {
			return empresaRepository.findByNomeContainingIgnoreCase(nome, paginacao).map(EmpresaDtoDetalhar::new);

		}
		return empresaRepository.findAll(paginacao).map(EmpresaDtoDetalhar::new);

	}

	public EmpresaDtoDetalhar detalharEmpresa(Long id) {

		return new EmpresaDtoDetalhar(empresaRepository.getReferenceById(id)) ;

	}

	public EmpresaDtoDetalhar atualizarCadastro(EmpresaDtoAtualizar dados, long id) {

		
		
		validaDuplicadas(dados);

		var empresa = empresaRepository.getReferenceById(id);

		empresa.atualizarInformacoes(dados);

		empresaRepository.save(empresa);

		return new EmpresaDtoDetalhar(empresa);

	}


	public void deletaCadastro(Long id) {

		empresaRepository.deleteById(id);

	}
	
	private void validaDuplicadas(EmpresaDtoAtualizar dados) {
		if (empresaRepository.existsByCnpj(dados.cnpj())) {
			throw new ValidacaoException("CNPJ já registrado!");
		}

		if (empresaRepository.existsByEmail(dados.email())) {
			throw new ValidacaoException("Email já registrado!");
		}
	}


	
	private void validaDuplicadas(EmpresaDtoCadastrar dados) {
		if (empresaRepository.existsByCnpj(dados.cnpj())) {
			throw new ValidacaoException("CNPJ já registrado!");
		}

		if (empresaRepository.existsByEmail(dados.email())) {
			throw new ValidacaoException("Email já registrado!");
		}
	}

}
