package br.com.magnasistema.apicoleta.entity;

import br.com.magnasistema.apicoleta.dto.destino.DestinoDtoAtualizar;
import br.com.magnasistema.apicoleta.dto.destino.DestinoDtoCadastro;
import br.com.magnasistema.apicoleta.enums.TipoDestino;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Table(name = "tb_destino")
@Entity(name = "Destino")
public class Destino {

	public Destino(String nome, TipoDestino tipoLocal, Double capacidadeSuportada, Endereco endereco, Empresa empresa) {

		this.nome = nome;
		this.tipoLocal = tipoLocal;
		this.capacidadeSuportada = capacidadeSuportada;
		this.endereco = endereco;
		this.empresa = empresa;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;

	@Enumerated(EnumType.STRING)
	private TipoDestino tipoLocal;

	private Double capacidadeSuportada;

	@Embedded
	private Endereco endereco;

	@ManyToOne
	@JoinColumn(name = "fk_empresa")
	private Empresa empresa;

	public Destino() {

	}

	public Destino(DestinoDtoCadastro dados, Cidade cidade, Empresa empresa) {

		this.nome = dados.nome();
		this.tipoLocal = dados.tipoLocal();
		this.capacidadeSuportada = dados.capacidadeSuportada();
		this.endereco = new Endereco(dados.endereco(), cidade);
		this.empresa = empresa;

	}

	public void atualizarInformacoes(DestinoDtoAtualizar dados, Cidade cidade, Empresa empresa) {

		if (dados.nome() != null && !dados.nome().isBlank()) {
			this.nome = dados.nome();
		}
		if (dados.tipoLocal() != null) {
			this.tipoLocal = dados.tipoLocal();
		}
		if (dados.capacidadeSuportada() != null) {
			this.capacidadeSuportada = dados.capacidadeSuportada();
		}
		if (dados.endereco() != null) {
			this.endereco.atualizarInformacoes(dados.endereco(), cidade);
		}

		if (empresa != null) {
			this.empresa = empresa;
		}

	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public TipoDestino getTipoLocal() {
		return tipoLocal;
	}

	public Double getCapacidadeSuportada() {
		return capacidadeSuportada;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

}
