package br.com.magnasistema.apicoleta.entity;

import java.time.LocalDateTime;

import br.com.magnasistema.apicoleta.dto.coleta.ColetaDtoAtualizar;
import br.com.magnasistema.apicoleta.dto.coleta.ColetaDtoCadastro;
import br.com.magnasistema.apicoleta.dto.coleta.ColetaDtoModificar;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;

@Table(name = "tb_coleta")
@Entity(name = "Coleta")
@EqualsAndHashCode(of = "id")
public class Coleta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDateTime timestampInicial;
	private LocalDateTime timestampFinal;
	private Double peso;

	@ManyToOne
	@JoinColumn(name = "fk_equipe")
	private Equipe equipe;

	@ManyToOne
	@JoinColumn(name = "fk_bairro")
	private Bairro bairro;

	@ManyToOne
	@JoinColumn(name = "fk_destino")
	private Destino destino;

	public Long getId() {
		return id;
	}

	public LocalDateTime getTimestampInicial() {
		return timestampInicial;
	}

	public LocalDateTime getTimestampFinal() {
		return timestampFinal;
	}

	public Double getPeso() {
		return peso;
	}

	public Equipe getEquipe() {
		return equipe;
	}

	public Bairro getBairro() {
		return bairro;
	}

	public Destino getDestino() {
		return destino;
	}

	public Coleta() {

	}

	public Coleta(ColetaDtoCadastro dados, Equipe equipe, Bairro bairro, Destino destino) {

		this.timestampInicial = dados.timestampInicial();
		this.timestampFinal = dados.timestampFinal();
		this.peso = dados.peso();
		this.equipe = equipe;
		this.bairro = bairro;
		this.destino = destino;
	}

	public void modificarInformacoes(ColetaDtoModificar dados, Destino destino) {
		
		if (dados.peso() != null) {
			this.peso = dados.peso();
		}

		if (destino != null) {
			this.destino = destino;
		}

	}
		
		public void atualizarInformacoes(ColetaDtoAtualizar dados) {

			this.timestampFinal = dados.timestampFinal();
			this.peso = dados.peso();

		}

}
