package br.com.magnasistema.apicoleta.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import br.com.magnasistema.apicoleta.dto.empresa.EmpresaDtoCadastrar;
import br.com.magnasistema.apicoleta.dto.funcionario.FuncionarioDtoAtualizar;
import br.com.magnasistema.apicoleta.dto.funcionario.FuncionarioDtoCadastro;
import br.com.magnasistema.apicoleta.dto.funcionario.FuncionarioDtoDetalhar;
import br.com.magnasistema.apicoleta.entity.Empresa;
import br.com.magnasistema.apicoleta.entity.Funcionario;
import br.com.magnasistema.apicoleta.enums.TipoEmpresa;
import br.com.magnasistema.apicoleta.enums.TipoFuncao;
import br.com.magnasistema.apicoleta.infra.PageResponse;
import br.com.magnasistema.apicoleta.repository.EmpresaRepository;
import br.com.magnasistema.apicoleta.repository.FuncionarioRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class FuncionarioControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private EmpresaRepository empresaRepository;

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	private final String uriPrincipal = "/funcionario";
	private final String uriPesquisaEmpresa = "/funcionario/empresa";

	@BeforeEach
	void inicializar() {

		Empresa empresaTeste = new Empresa(new EmpresaDtoCadastrar("empresa teste", "012345678912345", "400289222",
				"g@hotmail.com", TipoEmpresa.PUBLICA));

		Funcionario funcionarioTeste = new Funcionario("funcionario teste", "11133366622", LocalDate.of(1990, 5, 15),
				"f@gmail.com", TipoFuncao.COLETOR, empresaTeste);

		empresaRepository.save(empresaTeste);
		funcionarioRepository.save(funcionarioTeste);

	}

	@AfterEach
	void finalizar() {

		funcionarioRepository.deleteAllAndResetSequence();
		empresaRepository.deleteAllAndResetSequence();

	}

	@Test
	@DisplayName("Deveria cadastrar um funcionario com informações válidas")
	void cadastrarEmpresaCenario1() {

		FuncionarioDtoCadastro requestBody = new FuncionarioDtoCadastro("func", "11133366623",
				LocalDate.of(1990, 5, 15), "f2@gmail.com", TipoFuncao.COLETOR, 1L);

		ResponseEntity<FuncionarioDtoDetalhar> responseEntity = restTemplate.postForEntity(uriPrincipal, requestBody,
				FuncionarioDtoDetalhar.class);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody().nomeCompleto()).isEqualTo("func");

	}

	@ParameterizedTest
	@MethodSource("parametrosCadastroInvalido")
	@DisplayName("Não Deveria cadastrar um funcionario com informações inválidas")
	void cadastrarFuncionario(String nome, String cpf, LocalDate dataNascimento, String email, TipoFuncao funcao,
			Long idEmpresa, String mensagemDeErro) {

		FuncionarioDtoCadastro requestBody = new FuncionarioDtoCadastro(nome, cpf, dataNascimento, email, funcao,
				idEmpresa);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity(uriPrincipal, requestBody, String.class);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(responseEntity.getBody()).contains(mensagemDeErro);

	}

	@Test
	@DisplayName("Deveria listar funcionarios ")
	void listarFuncionariosCenario1() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<PageResponse<FuncionarioDtoDetalhar>> responseEntity = restTemplate.exchange(uriPrincipal,
				HttpMethod.GET, null, new ParameterizedTypeReference<PageResponse<FuncionarioDtoDetalhar>>() {
				});

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isNotNull();

		PageResponse<FuncionarioDtoDetalhar> pageResponse = responseEntity.getBody();

		assertThat(pageResponse.isEmpty()).isFalse();
		assertThat(pageResponse.getContent()).isNotEmpty();
		assertThat(pageResponse.getContent().get(0).id()).isEqualTo(1L);
	}

	@ValueSource(strings = { "", "COLETOR" })
	@ParameterizedTest
	@DisplayName("Deveria listar funcionarios com diferentes valores de funcao")
	void listarFuncionariosCenario2(String nome) {

		ResponseEntity<PageResponse<FuncionarioDtoDetalhar>> responseEntity = restTemplate.exchange(
				uriPrincipal + "?funcao=" + nome, HttpMethod.GET, null,
				new ParameterizedTypeReference<PageResponse<FuncionarioDtoDetalhar>>() {
				});

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isNotNull();

		PageResponse<FuncionarioDtoDetalhar> pageResponse = responseEntity.getBody();

		assertThat(pageResponse.isEmpty()).isFalse();
		assertThat(pageResponse.getContent()).isNotEmpty();
		assertThat(pageResponse.getContent().get(0).id()).isEqualTo(1L);
	}

	@Test
	@DisplayName("Deveria listar funcionarios com diferentes valores de nome pelo id da empresa")
	void listarFuncionariosPorEmpresaCenario1() {
		Long idEmpresaExistente = 1L;

		ResponseEntity<PageResponse<FuncionarioDtoDetalhar>> responseEntity = restTemplate.exchange(uriPrincipal,
				HttpMethod.GET, null, new ParameterizedTypeReference<PageResponse<FuncionarioDtoDetalhar>>() {
				}, idEmpresaExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isNotNull();

		PageResponse<FuncionarioDtoDetalhar> pageResponse = responseEntity.getBody();

		assertThat(pageResponse.isEmpty()).isFalse();
		assertThat(pageResponse.getContent()).isNotEmpty();
		assertThat(pageResponse.getContent().get(0).id()).isEqualTo(1L);
	}

	@ValueSource(strings = { "", "COLETOR" })
	@ParameterizedTest
	@DisplayName("Deveria listar funcionarios com diferentes valores de nome pelo id da empresa")
	void listarFuncionariosPorEmpresaCenario2(String nome) {
		Long idEmpresaExistente = 1L;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<PageResponse<FuncionarioDtoDetalhar>> responseEntity = restTemplate.exchange(
				uriPesquisaEmpresa+"/{id}?funcao=" + nome, HttpMethod.GET, null,
				new ParameterizedTypeReference<PageResponse<FuncionarioDtoDetalhar>>() {
				}, idEmpresaExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isNotNull();

		PageResponse<FuncionarioDtoDetalhar> pageResponse = responseEntity.getBody();

		assertThat(pageResponse.isEmpty()).isFalse();
		assertThat(pageResponse.getContent()).isNotEmpty();
		assertThat(pageResponse.getContent().get(0).id()).isEqualTo(1L);
	}

	@Test
	@DisplayName("Deveria detalhar um funcionario por ID")
	void detalharFuncionarioPorId() {
		Long idFuncionarioExistente = 1L;

		ResponseEntity<FuncionarioDtoDetalhar> responseEntity = restTemplate.exchange(uriPrincipal+"/{id}",
				HttpMethod.GET, null, FuncionarioDtoDetalhar.class, idFuncionarioExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody().id()).isEqualTo(idFuncionarioExistente);

	}

	@ParameterizedTest
	@MethodSource("parametrosAtualizar")
	@DisplayName("Deveria atualizar dados do funcionario")
	void atualizarFuncionarioPorIdCenario1(String nome, String cpf, LocalDate dataNascimento, String email,
			TipoFuncao funcao, Long idEmpresa) {
		Long idFuncionarioExistente = 1L;

		FuncionarioDtoAtualizar requestBody = new FuncionarioDtoAtualizar(nome, cpf, dataNascimento, email, funcao,
				idEmpresa);

		ResponseEntity<FuncionarioDtoDetalhar> responseEntity = restTemplate.exchange("/funcionario/{id}",
				HttpMethod.PUT, new HttpEntity<>(requestBody), FuncionarioDtoDetalhar.class, idFuncionarioExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody().id()).isEqualTo(idFuncionarioExistente);

	}

	@ParameterizedTest
	@MethodSource("parametrosCadastroInvalido")
	@DisplayName("Não Deveria atualizar com dados invalida")
	void atualizarFuncionarioPorIdCenario2(String nome, String cpf, LocalDate dataNascimento, String email,
			TipoFuncao funcao, Long idEmpresa, String mensagemDeErro) {
		Long idFuncionarioExistente = 1L;

		FuncionarioDtoAtualizar requestBody = new FuncionarioDtoAtualizar(nome, cpf, dataNascimento, email, funcao,
				idEmpresa);

		ResponseEntity<String> responseEntity = restTemplate.exchange(uriPrincipal +"/{id}", HttpMethod.PUT,
				new HttpEntity<>(requestBody), String.class, idFuncionarioExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(responseEntity.getBody()).contains(mensagemDeErro);

	}

	@Test
	@DisplayName("Deveria excluir um Funcionario por ID")
	void excluirFuncionarioPorId() {
		Long idFuncionarioExistente = 1L;

		ResponseEntity<Void> responseEntity = restTemplate.exchange(uriPrincipal+ "/{id}", HttpMethod.DELETE, null,
				Void.class, idFuncionarioExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}

	static Stream<Arguments> parametrosCadastroInvalido() {
		return Stream.of(
				Arguments.of("funcionario teste", "11133366622", LocalDate.of(1990, 5, 15), "f8@gmail.com",
						TipoFuncao.COLETOR, 1L, "Cpf já registrado!"),

				Arguments.of("funcionario teste", "11133366628", LocalDate.of(1990, 5, 15), "f@gmail.com",
						TipoFuncao.COLETOR, 1L, "Email já registrado!"),

				Arguments.of("funcionario teste", "11133366628", LocalDate.of(1990, 5, 15), "f8@gmail.com",
						TipoFuncao.COLETOR, 15L, "Id da empresa informada não existe!")

		);
	}

	static Stream<Arguments> parametrosAtualizar() {
		return Stream.of(Arguments.of("funcionario teste", null, null, null, null, null),

				Arguments.of("", "11133366611", LocalDate.of(1990, 5, 15), "f11@gmail.com", TipoFuncao.COLETOR, 1L),

				Arguments.of(null, "11133366611", LocalDate.of(1990, 5, 15), "f11@gmail.com", TipoFuncao.COLETOR, 1L)

		);
	}
}
