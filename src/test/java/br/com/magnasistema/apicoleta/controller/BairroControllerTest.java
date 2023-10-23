package br.com.magnasistema.apicoleta.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import br.com.magnasistema.apicoleta.dto.bairro.BairroDtoCadastro;
import br.com.magnasistema.apicoleta.dto.bairro.BairroDtoDetalhar;
import br.com.magnasistema.apicoleta.entity.Bairro;
import br.com.magnasistema.apicoleta.entity.Cidade;
import br.com.magnasistema.apicoleta.entity.Estado;
import br.com.magnasistema.apicoleta.infra.PageResponse;
import br.com.magnasistema.apicoleta.repository.BairroRepository;
import br.com.magnasistema.apicoleta.repository.CidadeRepository;
import br.com.magnasistema.apicoleta.repository.EstadoRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class BairroControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private EstadoRepository estadoRepository;

	@Autowired
	private CidadeRepository cidadeRepository;
 
	@Autowired
	private BairroRepository bairroRepository;

	private final String URI_PRINCIPAL = "/bairro";
	private final String URI_PESQUISA_CIDADE = "/bairro/cidade";

	@BeforeEach
	void inicializar() {

		Bairro bairroTeste = new Bairro("bairro teste", "logradouros", criarCidade());

		bairroRepository.save(bairroTeste);

	}

	@AfterEach
	void finalizar() {

		bairroRepository.deleteAllAndResetSequence();
		cidadeRepository.deleteAllAndResetSequence();
		estadoRepository.deleteAllAndResetSequence();
	}

	@Test
	@DisplayName("Deveria cadastrar um bairro com informações válidas")
	void cadastrarBairroCenario1() {

		BairroDtoCadastro requestBody = new BairroDtoCadastro(1L, "Nome do Bairro", "Logradouro válido");

		ResponseEntity<BairroDtoDetalhar> responseEntity = restTemplate.postForEntity(URI_PRINCIPAL, requestBody,
				BairroDtoDetalhar.class);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody().nome()).isEqualTo("Nome do Bairro");
		assertThat(responseEntity.getBody().logradouros()).isEqualTo("Logradouro válido");

	}

	@Test
	@DisplayName("Deveria manda exception ao usar cidade invalida")
	void cadastrarBairroCenario2() {

		BairroDtoCadastro requestBody = new BairroDtoCadastro(15L, "Nome do Bairro", "Logradouro válido");

		ResponseEntity<String> responseEntity = restTemplate.postForEntity(URI_PRINCIPAL, requestBody, String.class);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(responseEntity.getBody()).isEqualTo("Id da cidade informada não existe!");

	}

	@Test
	@DisplayName("Deveria detalhar um bairro por ID")
	void detalharBairroPorId() {
		Long idDoBairroExistente = 1L;

		ResponseEntity<BairroDtoDetalhar> responseEntity = restTemplate.exchange(URI_PRINCIPAL + "/{id}", HttpMethod.GET,
				null, BairroDtoDetalhar.class, idDoBairroExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody().nome()).isEqualTo("bairro teste");

	}

	@ParameterizedTest
	@MethodSource("parametrosAtualizar")
	@DisplayName("Deveria atualizar dados")
	void atualizarBairroPorIdCenario1(Long id, String nome, String logradouros) {
		Long idDoBairroExistente = 1L;

		BairroDtoCadastro requestBody = new BairroDtoCadastro(id, nome, logradouros);

		ResponseEntity<BairroDtoDetalhar> responseEntity = restTemplate.exchange(URI_PRINCIPAL + "/{id}", HttpMethod.PUT,
				new HttpEntity<>(requestBody), BairroDtoDetalhar.class, idDoBairroExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody().id()).isEqualTo(idDoBairroExistente);

	}

	@Test
	@DisplayName("Deveria mandar erro ao tentar atualizar com cidade invalida")
	void atualizarBairroPorIdCenario2() {
		Long idDoBairroExistente = 1L;

		BairroDtoCadastro requestBody = new BairroDtoCadastro(15L, "", "Novo Logradouro 4");

		ResponseEntity<String> responseEntity = restTemplate.exchange(URI_PRINCIPAL + "/{id}", HttpMethod.PUT,
				new HttpEntity<>(requestBody), String.class, idDoBairroExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(responseEntity.getBody()).isEqualTo("Id da cidade informada não existe!");

	}

	@Test
	@DisplayName("Deveria listar bairros ")
	void listarBairrosCenario1() {

		ResponseEntity<PageResponse<BairroDtoDetalhar>> responseEntity = restTemplate.exchange(URI_PRINCIPAL,
				HttpMethod.GET, null, new ParameterizedTypeReference<PageResponse<BairroDtoDetalhar>>() {
				});

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isNotNull();

		PageResponse<BairroDtoDetalhar> pageResponse = responseEntity.getBody();

		assertThat(pageResponse.isEmpty()).isFalse();
		assertThat(pageResponse.getContent()).isNotEmpty();
		assertThat(pageResponse.getContent().get(0).id()).isEqualTo(1L);
	}

	@ValueSource(strings = { "", "bairro" })
	@ParameterizedTest
	@DisplayName("Deveria listar bairros com diferentes valores de nome")
	void listarBairrosCenario2(String nome) {

		ResponseEntity<PageResponse<BairroDtoDetalhar>> responseEntity = restTemplate.exchange(
				URI_PRINCIPAL + "?nome=" + nome, HttpMethod.GET, null,
				new ParameterizedTypeReference<PageResponse<BairroDtoDetalhar>>() {
				});

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isNotNull();

		PageResponse<BairroDtoDetalhar> pageResponse = responseEntity.getBody();

		assertThat(pageResponse.isEmpty()).isFalse();

		assertThat(pageResponse.getContent()).isNotEmpty();
		assertThat(pageResponse.getContent().get(0).id()).isEqualTo(1L);
	}

	@Test
	@DisplayName("Deveria listar bairros  pelo id da cidade")
	void listarBairrosPorCidadeCenario1() {
		Long idCidadeExistente = 1L;

		ResponseEntity<PageResponse<BairroDtoDetalhar>> responseEntity = restTemplate.exchange(
				URI_PESQUISA_CIDADE + "/{id}", HttpMethod.GET, null,
				new ParameterizedTypeReference<PageResponse<BairroDtoDetalhar>>() {
				}, idCidadeExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isNotNull();

		PageResponse<BairroDtoDetalhar> pageResponse = responseEntity.getBody();

		assertThat(pageResponse.isEmpty()).isFalse();
		assertThat(pageResponse.getContent()).isNotEmpty();
		assertThat(pageResponse.getContent().get(0).id()).isEqualTo(1L);
	}

	@ValueSource(strings = { "", "bairro" })
	@ParameterizedTest
	@DisplayName("Deveria listar bairros com diferentes valores de nome pelo id da cidade")
	void listarBairrosPorCidadeCenario2(String nome) {
		Long idCidadeExistente = 1L;

		ResponseEntity<PageResponse<BairroDtoDetalhar>> responseEntity = restTemplate.exchange(
				URI_PESQUISA_CIDADE + "/{id}?nome=" + nome, HttpMethod.GET, null,
				new ParameterizedTypeReference<PageResponse<BairroDtoDetalhar>>() {
				}, idCidadeExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isNotNull();

		PageResponse<BairroDtoDetalhar> pageResponse = responseEntity.getBody();

		assertThat(pageResponse.isEmpty()).isFalse();
		assertThat(pageResponse.getContent()).isNotEmpty();
		assertThat(pageResponse.getContent().get(0).id()).isEqualTo(1L);
	}

	@Test
	@DisplayName("Deveria excluir um bairro por ID")
	void excluirBairroPorId() {
		Long idDoBairroExistente = 1L;

		ResponseEntity<Void> responseEntity = restTemplate.exchange(URI_PRINCIPAL + "/{id}", HttpMethod.DELETE, null,
				Void.class, idDoBairroExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}

	static Stream<Arguments> parametrosAtualizar() {
		return Stream.of(Arguments.of(1L, "Novo Nome do Bairro", "Novo Logradouro"),
				Arguments.of(null, "Novo Nome do Bairro 2", null), Arguments.of(null, null, "Novo Logradouro 3"),
				Arguments.of(null, "", "Novo Logradouro 4")

		);
	}

	private Cidade criarCidade() {

		Estado estadoTeste = new Estado("estado teste ", "uf");
		Cidade cidadeTeste = new Cidade("cidade teste", estadoTeste);

		estadoRepository.save(estadoTeste);
		cidadeRepository.save(cidadeTeste);

		return cidadeTeste;
	}

}
