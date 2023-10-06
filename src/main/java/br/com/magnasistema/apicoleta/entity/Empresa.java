package br.com.magnasistema.apicoleta.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.magnasistema.apicoleta.dto.empresa.EmpresaDtoAtualizar;
import br.com.magnasistema.apicoleta.dto.empresa.EmpresaDtoCadastrar;
import br.com.magnasistema.apicoleta.enums.TipoEmpresa;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;

@Table(name = "tb_empresa")
@Entity(name = "Empresa")
@EqualsAndHashCode(of = "id")
public class Empresa {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;

	@Column(unique = true)
	private String cnpj;

	private String telefone;

	@Column(unique = true)
	private String email;

	@Enumerated(EnumType.STRING)
	private TipoEmpresa tipo;

	@OneToMany(mappedBy = "empresa",  cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Veiculo> veiculo = new ArrayList<>();

	@OneToMany(mappedBy = "empresa",  cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Funcionario> funcionario = new ArrayList<>();
	
	@OneToMany(mappedBy = "empresa",  cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Destino> destinos = new ArrayList<>();
	
	public Empresa() {

	}

	public Empresa(EmpresaDtoCadastrar dados) {

		this.nome = dados.nome();
		this.cnpj = dados.cnpj();

		this.telefone = dados.telefone();

		this.email = dados.email();
		this.tipo = dados.tipo();
	}

	public void atualizarInformacoes(EmpresaDtoAtualizar dados) {

		if (dados.nome() != null && !dados.nome().isBlank()) {
			this.nome = dados.nome();
		}

		if (dados.cnpj() != null ) {
			this.cnpj = dados.cnpj();
		}

		if (dados.telefone() != null ) {
			this.telefone = dados.telefone();
		}

		if (dados.email() != null ) {
			this.email = dados.email();
		}
		if (dados.tipo() != null) {
			this.tipo = dados.tipo();
		}

	}
	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getCnpj() {
		return cnpj;
	}

	public String getTelefone() {
		return telefone;
	}

	public String getEmail() {
		return email;
	}

	public TipoEmpresa getTipo() {
		return tipo;
	}


}
