package br.com.magnasistema.apicoleta.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.magnasistema.apicoleta.dto.bairro.BairroDtoCadastro;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;

@Table(name = "tb_bairro")
@Entity(name = "Bairro")
@EqualsAndHashCode(of = "id")
public class Bairro {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;
	private String logradouros;

	@ManyToOne
	@JoinColumn(name = "fk_cidade")
	private Cidade cidade;

	@ManyToMany(mappedBy = "bairro")
	@JsonIgnore
	private List<Coleta> coleta = new ArrayList<>();

	public Bairro() {

	}

	public Bairro(String nome, String logradouros, Cidade cidade) {

		this.nome = nome;
		this.logradouros = logradouros;
		this.cidade = cidade;
	}

	public Bairro(BairroDtoCadastro dados, Cidade cidade) {

		this.nome = dados.nome();
		this.logradouros = dados.logradouros();
		this.cidade = cidade;

	}

	public void atualizarInformacoes(BairroDtoCadastro dados, Cidade cidade) {

		if (dados.nome() != null && !dados.nome().isBlank()) {
			this.nome = dados.nome();
		}

		if (dados.logradouros() != null) {
			this.logradouros = dados.logradouros();
		}

		if (cidade != null) {
			this.cidade = cidade;
		}

	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getLogradouros() {
		return logradouros;
	}

	public Cidade getCidade() {
		return cidade;
	}

}
