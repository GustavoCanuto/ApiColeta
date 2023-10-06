package br.com.magnasistema.apicoleta.entity;

import br.com.magnasistema.apicoleta.dto.endereco.EnderecoDtoAtualizar;
import br.com.magnasistema.apicoleta.dto.endereco.EnderecoDtoCadastrar;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Embeddable
public class Endereco {

	@ManyToOne
	@JoinColumn(name = "fk_cidade")
	private Cidade cidade;

	private String logradouro;
	private String numero;
	private String cep;

	public Endereco(EnderecoDtoCadastrar dados, Cidade cidade) {
		this.cidade = cidade;
		this.logradouro = dados.logradouro();
		this.cep = dados.cep();
		this.numero = dados.numero();

	}

	public void atualizarInformacoes(EnderecoDtoAtualizar dados, Cidade cidade) {

		if (dados.logradouro() != null && !dados.logradouro().isBlank()) {
			this.logradouro = dados.logradouro();
		}

		if (dados.numero() != null && !dados.numero().isBlank()) {
			this.numero = dados.numero();
		}

		if (dados.cep() != null) {
			this.cep = dados.cep();
		}

		if (cidade != null) {
			this.cidade = cidade;
		}

	}

	public Cidade getCidade() {
		return cidade;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public String getNumero() {
		return numero;
	}

	public String getCep() {
		return cep;
	}

	public Endereco() {

	}

}
