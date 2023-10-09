package br.com.magnasistema.apicoleta.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table(name = "tb_cidade")
@Entity(name = "Cidade")
public class Cidade {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;

	@ManyToOne
	@JoinColumn(name = "fk_estado")
	private Estado estado;

	@OneToMany(mappedBy = "cidade")
	@JsonIgnore
	private List<Bairro> bairro = new ArrayList<>();

	public Cidade() {

	}

	public Cidade(String nome, Estado estado) {

		this.nome = nome;
		this.estado = estado;

	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public Estado getEstado() {
		return estado;
	}

}
