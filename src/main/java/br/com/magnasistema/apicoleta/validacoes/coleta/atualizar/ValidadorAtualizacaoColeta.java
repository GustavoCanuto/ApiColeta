package br.com.magnasistema.apicoleta.validacoes.coleta.atualizar;

import br.com.magnasistema.apicoleta.dto.coleta.ColetaDtoAtualizar;

public interface ValidadorAtualizacaoColeta {

	void validar(ColetaDtoAtualizar dados, Long id);

}
