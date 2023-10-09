package br.com.magnasistema.apicoleta.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.magnasistema.apicoleta.dto.funcionario.FuncionarioDtoAtualizar;
import br.com.magnasistema.apicoleta.dto.funcionario.FuncionarioDtoCadastro;
import br.com.magnasistema.apicoleta.enums.TipoFuncao;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table(name = "tb_funcionario")
@Entity(name = "Funcionario")
public class Funcionario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nomeCompleto;

	@Column(unique = true)
	private String cpf;

	private LocalDate dataNascimento;

	@Column(unique = true)
	private String email;

	@Enumerated(EnumType.STRING)
	private TipoFuncao funcao;

	@ManyToOne
	@JoinColumn(name = "fk_empresa")
	private Empresa empresa;

	@OneToMany(mappedBy = "funcionario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<FuncionarioEquipe> funcionarioEquipe = new ArrayList<>();

	public Funcionario() {

	}
 
	public Funcionario(FuncionarioDtoCadastro dados, Empresa empresa) {

	
		this.nomeCompleto = dados.nomeCompleto();
		this.cpf = dados.cpf();
		this.dataNascimento = dados.dataNascimento();
		this.email = dados.email();
		this.funcao = dados.funcao();
		this.empresa = empresa;

	}

	public Funcionario(String nomeCompleto, String cpf, LocalDate dataNascimento, String email, TipoFuncao funcao,
			Empresa empresa) {
		
		this.nomeCompleto = nomeCompleto;
		this.cpf = cpf;
		this.dataNascimento = dataNascimento;
		this.email = email;
		this.funcao = funcao;
		this.empresa = empresa;
		
	}

	public void atualizarInformacoes(FuncionarioDtoAtualizar dados, Empresa empresa) {

		if (dados.nomeCompleto() != null && !dados.nomeCompleto().isBlank()) {
			this.nomeCompleto = dados.nomeCompleto();
		}

		if (dados.cpf() != null) {
			this.cpf = dados.cpf();
		}

		if (dados.dataNascimento() != null) {
			this.dataNascimento = dados.dataNascimento();
		}

		if (dados.email() != null) {
			this.email = dados.email();
		}

		if (dados.funcao() != null) {
			this.funcao = dados.funcao();
		}

		if (empresa != null) {
			this.empresa = empresa;
		}
	}



	public Long getId() {
		return id;
	}

	public List<FuncionarioEquipe> getFuncionarioEquipe() {
		return funcionarioEquipe;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public String getCpf() {
		return cpf;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public String getEmail() {
		return email;
	}

	public TipoFuncao getFuncao() {
		return funcao;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

}
