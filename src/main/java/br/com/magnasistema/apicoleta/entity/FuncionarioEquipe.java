package br.com.magnasistema.apicoleta.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Table(name = "tb_funcionario_equipe")
@Entity(name = "FuncionarioEquipe")
public class FuncionarioEquipe {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "fk_funcionario")
	private Funcionario funcionario;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = " fk_equipe")
	private Equipe equipe;

	public FuncionarioEquipe() {

	}

	public FuncionarioEquipe(Funcionario funcionario, Equipe equipe) {

		this.funcionario = funcionario;
		this.equipe = equipe;
	}


	public Long getId() {
		return id;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public Equipe getEquipe() {
		return equipe;
	}

}
