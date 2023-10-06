package br.com.magnasistema.apicoleta.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;

@Table(name = "tb_estado")
@Entity(name = "Estado")
@EqualsAndHashCode(of = "id")
public class Estado {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String uf;

	@OneToMany(mappedBy = "estado")
	@JsonIgnore
	private List<Cidade> cidade = new ArrayList<>();

	public Estado() {

	}

	public Estado(String nome, String uf) {

		this.nome = nome;
		this.uf = uf;

	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getUf() {
		return uf;
	}

}
