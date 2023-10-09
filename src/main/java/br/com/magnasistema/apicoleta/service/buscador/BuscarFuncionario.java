package br.com.magnasistema.apicoleta.service.buscador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.magnasistema.apicoleta.entity.Funcionario;
import br.com.magnasistema.apicoleta.repository.FuncionarioRepository;
import br.com.magnasistema.apicoleta.validacoes.ValidacaoException;

@Component
public class BuscarFuncionario implements BuscarEntidade<Funcionario> {

	@Autowired
	FuncionarioRepository funcionarioRepository;

	Funcionario funcionario;

	public Funcionario buscar(Long id) {

		funcionario = funcionarioRepository.findById(id)
				.orElseThrow(() -> new ValidacaoException("Id do Funcionario não encontrado"));

		return funcionario;
	}

}
