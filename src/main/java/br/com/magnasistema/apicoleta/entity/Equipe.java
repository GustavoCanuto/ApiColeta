package br.com.magnasistema.apicoleta.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.magnasistema.apicoleta.dto.equipe.EquipeDtoCadastro;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table(name = "tb_equipe")
@Entity(name = "Equipe")
public class Equipe {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;

	private String descricao;

	@ManyToOne
	@JoinColumn(name = "fk_veiculo")
	private Veiculo veiculo;

	@OneToMany(mappedBy = "equipe", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<FuncionarioEquipe> funcionarioEquipe = new ArrayList<>();

	public Equipe() {

	}

	public Equipe(String nome, String descricao, Veiculo veiculo) {

		this.nome = nome;
		this.descricao = descricao;
		this.veiculo = veiculo;

	}
	
	public Equipe(EquipeDtoCadastro dados, Veiculo veiculo) {

		this.nome = dados.nome();
		this.descricao = dados.descricao();
		this.veiculo = veiculo;

	}

	public void atualizarInformacoes(EquipeDtoCadastro dados, Veiculo veiculo) {

		if (dados.nome() != null && !dados.nome().isBlank()) {
			this.nome = dados.nome();
		}
		if (dados.descricao() != null && !dados.descricao().isBlank()) {
			this.descricao = dados.descricao();
		}
		if (veiculo != null) {
			this.veiculo = veiculo;
		}

	}

	@JsonIgnore
	public List<Funcionario> getFuncionarios() {

		List<Funcionario> funcionarios = new ArrayList<>();

		for (FuncionarioEquipe fe : funcionarioEquipe) {
			funcionarios.add(fe.getFuncionario());
		}
		return funcionarios;
	}

	public Long getId() {
		return id;
	}

	public List<FuncionarioEquipe> getFuncionarioEquipe() {
		return funcionarioEquipe;
	}

	public String getNome() {
		return nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

}
