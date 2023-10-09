package br.com.magnasistema.apicoleta.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.magnasistema.apicoleta.dto.funcionario.FuncionarioDtoAtualizar;
import br.com.magnasistema.apicoleta.dto.funcionario.FuncionarioDtoCadastro;
import br.com.magnasistema.apicoleta.dto.funcionario.FuncionarioDtoDetalhar;
import br.com.magnasistema.apicoleta.entity.Empresa;
import br.com.magnasistema.apicoleta.entity.Funcionario;
import br.com.magnasistema.apicoleta.enums.TipoFuncao;
import br.com.magnasistema.apicoleta.repository.FuncionarioRepository;
import br.com.magnasistema.apicoleta.service.buscador.BuscarEmpresa;
import br.com.magnasistema.apicoleta.validacoes.ValidacaoException;

@Service
public class FuncionarioService {

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	@Autowired
	private BuscarEmpresa getEmpresa;

	public FuncionarioDtoDetalhar cadastrarFuncionario(FuncionarioDtoCadastro dados) {

		validaDuplicadas(dados);

//		Empresa empresa = empresaRepository.findById(dados.idEmpresa())
//				.orElseThrow(() -> new ValidacaoException("Id da empresa informada não existe!"));
		
		Empresa empresa = getEmpresa.buscar(dados.idEmpresa());

		Funcionario funcionario = new Funcionario(dados, empresa);

		funcionarioRepository.save(funcionario);

		return new FuncionarioDtoDetalhar(funcionario);

	}



	public Page<FuncionarioDtoDetalhar> listarFuncionarios(Pageable paginacao, TipoFuncao funcao) {

		if (funcao != null) {

			return funcionarioRepository.findByFuncao(funcao, paginacao).map(FuncionarioDtoDetalhar::new);

		}

		return funcionarioRepository.findAll(paginacao).map(FuncionarioDtoDetalhar::new);
	}

	public Page<FuncionarioDtoDetalhar> listarFuncionariosPorEmpresa(Pageable paginacao, TipoFuncao funcao,
			long idEmpresa) {

		if (funcao != null) {

			return funcionarioRepository.findByEmpresaIdAndFuncao(idEmpresa, funcao, paginacao)
					.map(FuncionarioDtoDetalhar::new);

		}

		return funcionarioRepository.findByEmpresaId(idEmpresa, paginacao).map(FuncionarioDtoDetalhar::new);
	}

	public FuncionarioDtoDetalhar detalharFuncionario(Long id) {

		return new FuncionarioDtoDetalhar(funcionarioRepository.getReferenceById(id));

	}

	public FuncionarioDtoDetalhar atualizarCadastro(FuncionarioDtoAtualizar dados, Long id) {
		
		
		validaDuplicadas(dados);
		
//		Empresa empresa = null;
//
//		if (dados.idEmpresa() != null) {
// 
//			empresa = empresaRepository.findById(dados.idEmpresa())
//					.orElseThrow(() -> new ValidacaoException("Id da empresa informada não existe!"));
//
//		}
		
		Empresa empresa = getEmpresa.buscar(dados.idEmpresa());

		Funcionario funcionario = funcionarioRepository.getReferenceById(id);

		funcionario.atualizarInformacoes(dados, empresa);

		funcionarioRepository.save(funcionario);

		return new FuncionarioDtoDetalhar(funcionario);
	}



	

	public void deletaCadastro(Long id) {

		funcionarioRepository.deleteById(id);

	}
	
	private void validaDuplicadas(FuncionarioDtoCadastro dados) {
		if (funcionarioRepository.existsByCpf(dados.cpf())) {
			throw new ValidacaoException("Cpf já registrado!");
		}

		if (funcionarioRepository.existsByEmail(dados.email())) {
			throw new ValidacaoException("Email já registrado!");
		}
	}
	
	private void validaDuplicadas(FuncionarioDtoAtualizar dados) {
		
		if (funcionarioRepository.existsByCpf(dados.cpf())) {
			throw new ValidacaoException("Cpf já registrado!");
		}

		if (funcionarioRepository.existsByEmail(dados.email())) {
			throw new ValidacaoException("Email já registrado!");
		}
	}

}
