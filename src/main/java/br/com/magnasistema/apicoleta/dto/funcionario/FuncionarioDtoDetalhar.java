package br.com.magnasistema.apicoleta.dto.funcionario;

import java.time.LocalDate;

import br.com.magnasistema.apicoleta.entity.Empresa;
import br.com.magnasistema.apicoleta.entity.Funcionario;
import br.com.magnasistema.apicoleta.enums.TipoFuncao;

public record FuncionarioDtoDetalhar(

		Long id,

		String nomeCompleto,

		String cpf,

		LocalDate dataNascimento,

		String email,

		TipoFuncao funcao,

		Empresa empresa) {

	public FuncionarioDtoDetalhar(Funcionario funcionario) {
		this(funcionario.getId(), funcionario.getNomeCompleto(), funcionario.getCpf(), funcionario.getDataNascimento(),
				funcionario.getEmail(), funcionario.getFuncao(), funcionario.getEmpresa());
	}

}
