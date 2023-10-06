package br.com.magnasistema.apicoleta.dto.funcionario;

import java.time.LocalDate;
import java.util.List;

import br.com.magnasistema.apicoleta.dto.funcionarioequipe.FuncionarioEquipeDtoApenasEquipes;
import br.com.magnasistema.apicoleta.entity.Empresa;
import br.com.magnasistema.apicoleta.entity.Funcionario;
import br.com.magnasistema.apicoleta.enums.TipoFuncao;

public record FuncionarioDtoListagemEquipes(Long id,

		String nomeCompleto,

		String cpf,

		LocalDate dataNascimento,

		String email,

		TipoFuncao funcao,

		Empresa empresa,

		List<FuncionarioEquipeDtoApenasEquipes> equipes) {

	public FuncionarioDtoListagemEquipes(Funcionario funcionario) {
		this(funcionario.getId(), funcionario.getNomeCompleto(), funcionario.getCpf(), funcionario.getDataNascimento(),
				funcionario.getEmail(), funcionario.getFuncao(), funcionario.getEmpresa(),
				funcionario.getFuncionarioEquipe().stream().map(FuncionarioEquipeDtoApenasEquipes::new).toList());
	}

}
