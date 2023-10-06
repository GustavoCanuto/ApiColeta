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

import br.com.magnasistema.apicoleta.dto.empresa.EmpresaDtoAtualizar;
import br.com.magnasistema.apicoleta.dto.empresa.EmpresaDtoCadastrar;
import br.com.magnasistema.apicoleta.dto.empresa.EmpresaDtoDetalhar;
import br.com.magnasistema.apicoleta.entity.Empresa;
import br.com.magnasistema.apicoleta.enums.TipoEmpresa;
import br.com.magnasistema.apicoleta.infra.PageResponse;
import br.com.magnasistema.apicoleta.repository.EmpresaRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class EmpresaControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private EmpresaRepository empresaRepository;

	private final String uriPrincipal = "/empresa";

	@BeforeEach
	void inicializar() {

		Empresa empresaTeste = new Empresa(new EmpresaDtoCadastrar("empresa teste", "012345678912345", "400289222",
				"g@hotmail.com", TipoEmpresa.PUBLICA));

		empresaRepository.save(empresaTeste);

	}

	@AfterEach
	void finalizar() {

		empresaRepository.deleteAllAndResetSequence();

	}

	@Test
	@DisplayName("Deveria cadastrar uma empresa com informações válidas")
	void cadastrarEmpresaCenario1() {

		EmpresaDtoCadastrar requestBody = new EmpresaDtoCadastrar("empresa teste2", "012345678912346", "400289227",
				"g7@hotmail.com", TipoEmpresa.PUBLICA);

		ResponseEntity<EmpresaDtoDetalhar> responseEntity = restTemplate.postForEntity(uriPrincipal, requestBody,
				EmpresaDtoDetalhar.class);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody().nome()).isEqualTo("empresa teste2");

	}

	@ParameterizedTest
	@MethodSource("parametrosCadastroInvalido")
	@DisplayName("Não Deveria cadastrar uma empresa com informações inválidas")
	void cadastrarEmpresaCenario2(String nome, String cnpj, String telefone, String email, TipoEmpresa tipo,
			String mensagemDeErro) {

		EmpresaDtoCadastrar requestBody = new EmpresaDtoCadastrar(nome, cnpj, telefone, email, tipo);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity("/empresa", requestBody, String.class);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(responseEntity.getBody()).contains(mensagemDeErro);

	}

	@Test
	@DisplayName("Deveria listar empresas ")
	void listarEmpresasCenario1() {

		ResponseEntity<PageResponse<EmpresaDtoDetalhar>> responseEntity = restTemplate.exchange(uriPrincipal,
				HttpMethod.GET, null, new ParameterizedTypeReference<PageResponse<EmpresaDtoDetalhar>>() {
				});

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isNotNull();

		PageResponse<EmpresaDtoDetalhar> pageResponse = responseEntity.getBody();

		assertThat(pageResponse.isEmpty()).isFalse();
		assertThat(pageResponse.getContent()).isNotEmpty();
		assertThat(pageResponse.getContent().get(0).id()).isEqualTo(1L);
	}

	@ValueSource(strings = { "", "empresa" })
	@ParameterizedTest
	@DisplayName("Deveria listar empresas com diferentes valores de nome")
	void listarEmpresasCenario2(String nome) {

		ResponseEntity<PageResponse<EmpresaDtoDetalhar>> responseEntity = restTemplate.exchange(
				uriPrincipal + "?nome=" + nome, HttpMethod.GET, null,
				new ParameterizedTypeReference<PageResponse<EmpresaDtoDetalhar>>() {
				});

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isNotNull();

		PageResponse<EmpresaDtoDetalhar> pageResponse = responseEntity.getBody();

		assertThat(pageResponse.isEmpty()).isFalse();
		assertThat(pageResponse.getContent()).isNotEmpty();
		assertThat(pageResponse.getContent().get(0).id()).isEqualTo(1L);
	}

	@Test
	@DisplayName("Deveria detalhar uma cidade por ID")
	void detalharEmpresaPorId() {
		Long idDoEmpresaExistente = 1L;

		ResponseEntity<EmpresaDtoDetalhar> responseEntity = restTemplate.exchange(uriPrincipal+"/{id}", HttpMethod.GET, null,
				EmpresaDtoDetalhar.class, idDoEmpresaExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody().id()).isEqualTo(idDoEmpresaExistente);

	}

	@ParameterizedTest
	@MethodSource("parametrosAtualizar")
	@DisplayName("Deveria atualizar dados")
	void atualizarEmpresaPorIdCenario1(String nome, String cnpj, String telefone, String email, TipoEmpresa tipo) {
		Long idDoEmpresaExistente = 1L;

		EmpresaDtoAtualizar requestBody = new EmpresaDtoAtualizar(nome, cnpj, telefone, email, tipo);

		ResponseEntity<EmpresaDtoDetalhar> responseEntity = restTemplate.exchange(uriPrincipal+"/{id}", HttpMethod.PUT,
				new HttpEntity<>(requestBody), EmpresaDtoDetalhar.class, idDoEmpresaExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody().id()).isEqualTo(idDoEmpresaExistente);
		

	}

	@ParameterizedTest
	@MethodSource("parametrosCadastroInvalido")
	@DisplayName("Não Deveria atualizar dados")
	void atualizarEmpresaPorIdCenario2(String nome, String cnpj, String telefone, String email, TipoEmpresa tipo, String mensagemDeErro) {
		Long idDoEmpresaExistente = 1L;

		EmpresaDtoAtualizar requestBody = new EmpresaDtoAtualizar(nome, cnpj, telefone, email, tipo);

		ResponseEntity<String> responseEntity = restTemplate.exchange(uriPrincipal + "/{id}", HttpMethod.PUT,
				new HttpEntity<>(requestBody), String.class, idDoEmpresaExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody()).contains(mensagemDeErro);

	}

	@Test
	@DisplayName("Deveria excluir uma Empresa por ID")
	void excluirEmpresaPorId() {
		Long idEmpresaExistente = 1L;

		ResponseEntity<Void> responseEntity = restTemplate.exchange(uriPrincipal+"/{id}", HttpMethod.DELETE, null,
				Void.class, idEmpresaExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}

	static Stream<Arguments> parametrosCadastroInvalido() {
		return Stream.of(
				Arguments.of("empresa teste", "012345678912345", "400289222", "g2@hotmail.com", TipoEmpresa.PUBLICA,
						"CNPJ já registrado!"),
				Arguments.of("empresa teste", "012345678912346", "400289228", "g@hotmail.com", TipoEmpresa.PUBLICA,
						"Email já registrado!"));
	}

	static Stream<Arguments> parametrosAtualizar() {
		return Stream.of(Arguments.of("nome", null, null, null, null),
				Arguments.of("", "012345678912343", "400289222", "g3@hotmail.com", TipoEmpresa.PUBLICA),
				Arguments.of(null, "012345678912343", "400289222", "g3@hotmail.com", TipoEmpresa.PUBLICA));
	}
}
