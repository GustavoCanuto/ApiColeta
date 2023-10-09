package br.com.magnasistema.apicoleta.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.magnasistema.apicoleta.dto.veiculo.VeiculoDtoAtualizar;
import br.com.magnasistema.apicoleta.dto.veiculo.VeiculoDtoCadastro;
import br.com.magnasistema.apicoleta.enums.TipoVeiculo;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table(name = "tb_veiculo")
@Entity(name = "Veiculo")
public class Veiculo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private TipoVeiculo tipo;
	private Double capacidade;

	@Column(unique = true)
	private String placa;

	private Integer anoFabricacao;

	@ManyToOne
	@JoinColumn(name = "fk_empresa")
	private Empresa empresa;

	@OneToMany(mappedBy = "veiculo", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Equipe> equipe = new ArrayList<>();

	public Veiculo() {

	}

	public Veiculo(TipoVeiculo tipo, Double capacidade, String placa, Integer anoFabricacao, Empresa empresa) {

		this.tipo = tipo;
		this.capacidade = capacidade;
		this.placa = placa;
		this.anoFabricacao = anoFabricacao;
		this.empresa = empresa;

	}

	public Veiculo(VeiculoDtoCadastro dados, Empresa empresa) {
		this.tipo = dados.tipo();
		this.capacidade = dados.capacidade();
		this.placa = dados.placa();
		this.anoFabricacao = dados.anoFabricacao();
		this.empresa = empresa;
	}

	public void atualizarInformacoes(VeiculoDtoAtualizar dados, Empresa empresa) {

		if (dados.tipo() != null) {
			this.tipo = dados.tipo();
		}

		if (dados.capacidade() != null) {
			this.capacidade = dados.capacidade();
		}

		if (dados.placa() != null) {
			this.placa = dados.placa();
		}

		if (dados.anoFabricacao() != null) {
			this.anoFabricacao = dados.anoFabricacao();
		}

		if (empresa != null) {
			this.empresa = empresa;
		}

	}

	public Long getId() {
		return id;
	}

	public TipoVeiculo getTipo() {
		return tipo;
	}

	public Double getCapacidade() {
		return capacidade;
	}

	public String getPlaca() {
		return placa;
	}

	public Integer getAnoFabricacao() {
		return anoFabricacao;
	}

	public Empresa getEmpresa() {
		return empresa;
	}



}
