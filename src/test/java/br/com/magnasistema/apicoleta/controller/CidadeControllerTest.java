package br.com.magnasistema.apicoleta.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import br.com.magnasistema.apicoleta.dto.cidade.CidadeDtoDetalhar;
import br.com.magnasistema.apicoleta.entity.Cidade;
import br.com.magnasistema.apicoleta.entity.Estado;
import br.com.magnasistema.apicoleta.infra.PageResponse;
import br.com.magnasistema.apicoleta.repository.CidadeRepository;
import br.com.magnasistema.apicoleta.repository.EstadoRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CidadeControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private EstadoRepository estadoRepository;

	@Autowired
	private CidadeRepository cidadeRepository;

	private final String uriPrincipal = "/cidade";
	private final String uriPesquisaEstado = "/cidade/estado";

	@BeforeEach
	void inicializar() {

		Estado estadoTeste = new Estado("estado teste", "uf");
		Cidade cidadeTeste = new Cidade("cidade teste", estadoTeste);

		estadoRepository.save(estadoTeste);
		cidadeRepository.save(cidadeTeste);

	}

	@AfterEach
	void finalizar() {

		cidadeRepository.deleteAllAndResetSequence();
		estadoRepository.deleteAllAndResetSequence();
	}

	@Test
	@DisplayName("Deveria listar cidades")
	void listarCidadesCenario1() {

		ResponseEntity<PageResponse<CidadeDtoDetalhar>> responseEntity = restTemplate.exchange(uriPrincipal,
				HttpMethod.GET, null, new ParameterizedTypeReference<PageResponse<CidadeDtoDetalhar>>() {
				});

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isNotNull();

		PageResponse<CidadeDtoDetalhar> pageResponse = responseEntity.getBody();

		assertThat(pageResponse.isEmpty()).isFalse();
		assertThat(pageResponse.getContent()).isNotEmpty();
		assertThat(pageResponse.getContent().get(0).id()).isEqualTo(1L);

	}

	@ValueSource(strings = { "", "cidade" })
	@ParameterizedTest
	@DisplayName("Deveria listar cidades com diferentes valores de nome")
	void listarCidadesCenario2(String nome) {

		ResponseEntity<PageResponse<CidadeDtoDetalhar>> responseEntity = restTemplate.exchange(uriPrincipal+ "?nome=" + nome,
				HttpMethod.GET, null, new ParameterizedTypeReference<PageResponse<CidadeDtoDetalhar>>() {
				});

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isNotNull();

		PageResponse<CidadeDtoDetalhar> pageResponse = responseEntity.getBody();

		assertThat(pageResponse.isEmpty()).isFalse();
		assertThat(pageResponse.getContent()).isNotEmpty();
		assertThat(pageResponse.getContent().get(0).id()).isEqualTo(1L);
	}

	@Test
	@DisplayName("Deveria listar cidades pelo id do estado")
	void listarCidadesPorEstadoCenario1() {
		Long idEstadoExistente = 1L;

		ResponseEntity<PageResponse<CidadeDtoDetalhar>> responseEntity = restTemplate.exchange(uriPesquisaEstado +"/{id}",
				HttpMethod.GET, null, new ParameterizedTypeReference<PageResponse<CidadeDtoDetalhar>>() {
				}, idEstadoExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isNotNull();

		PageResponse<CidadeDtoDetalhar> pageResponse = responseEntity.getBody();

		assertThat(pageResponse.isEmpty()).isFalse();
		
		assertThat(pageResponse.getContent()).isNotEmpty();
		assertThat(pageResponse.getContent().get(0).estado().nome()).isEqualTo("estado teste");
	}

	@ValueSource(strings = { "", "cidade" })
	@ParameterizedTest
	@DisplayName("Deveria listar cidades com diferentes valores de nome pelo id do estado")
	void listarCidadesPorEstadoCenario2(String nome) {
		Long idEstadoExistente = 1L;

		ResponseEntity<PageResponse<CidadeDtoDetalhar>> responseEntity = restTemplate.exchange(
				uriPesquisaEstado + "/{id}?nome=" + nome, HttpMethod.GET, null,
				new ParameterizedTypeReference<PageResponse<CidadeDtoDetalhar>>() {
				}, idEstadoExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isNotNull();

		PageResponse<CidadeDtoDetalhar> pageResponse = responseEntity.getBody();

		assertThat(pageResponse.isEmpty()).isFalse();
		assertThat(pageResponse.getContent()).isNotEmpty();
		assertThat(pageResponse.getContent().get(0).estado().nome()).isEqualTo("estado teste");
	}

	@Test
	@DisplayName("Deveria detalhar uma cidade por ID")
	void detalharCidadePorId() {
		Long idDoCidadeExistente = 1L;

		ResponseEntity<CidadeDtoDetalhar> responseEntity = restTemplate.exchange(uriPrincipal+"/{id}", HttpMethod.GET, null,
				CidadeDtoDetalhar.class, idDoCidadeExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody().id()).isEqualTo(idDoCidadeExistente);

	}

}
