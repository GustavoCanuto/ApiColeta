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
import br.com.magnasistema.apicoleta.dto.veiculo.VeiculoDtoAtualizar;
import br.com.magnasistema.apicoleta.dto.veiculo.VeiculoDtoCadastro;
import br.com.magnasistema.apicoleta.dto.veiculo.VeiculoDtoDetalhar;
import br.com.magnasistema.apicoleta.entity.Empresa;
import br.com.magnasistema.apicoleta.entity.Veiculo;
import br.com.magnasistema.apicoleta.enums.TipoEmpresa;
import br.com.magnasistema.apicoleta.enums.TipoVeiculo;
import br.com.magnasistema.apicoleta.infra.PageResponse;
import br.com.magnasistema.apicoleta.repository.EmpresaRepository;
import br.com.magnasistema.apicoleta.repository.VeiculoRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class VeiculoControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private EmpresaRepository empresaRepository;

	@Autowired
	private VeiculoRepository veiculoRepository;

	private final String URI_PRINCIPAL = "/veiculo";
	private final String URI_PESQUISA_EMPRESA = "/veiculo/empresa";

	@BeforeEach
	void inicializar() {

		Empresa empresaTeste = new Empresa(new EmpresaDtoCadastrar("empresa teste", "012345678912345", "400289222",
				"g@hotmail.com", TipoEmpresa.PUBLICA));

		Veiculo veiculoTeste = new Veiculo(TipoVeiculo.CAMINHAO_COMPACTADOR, 50D, "1234xx8", 1950, empresaTeste);

		empresaRepository.save(empresaTeste);
		veiculoRepository.save(veiculoTeste);

	}

	@AfterEach
	void finalizar() {

		veiculoRepository.deleteAllAndResetSequence();
		empresaRepository.deleteAllAndResetSequence();

	}

	@Test
	@DisplayName("Deveria cadastrar um veiculo com informações válidas")
	void cadastrarVeiculoCenario1() {

		VeiculoDtoCadastro requestBody = new VeiculoDtoCadastro(TipoVeiculo.CAMINHAO_COMPACTADOR, 50D, "1234xx9", 1950,
				1L);

		ResponseEntity<VeiculoDtoDetalhar> responseEntity = restTemplate.postForEntity(URI_PRINCIPAL, requestBody,
				VeiculoDtoDetalhar.class);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody().id()).isEqualTo(2L);

	}

	@ParameterizedTest
	@MethodSource("parametrosCadastroInvalido")
	@DisplayName("Não Deveria cadastrar um veiculo com informações inválidas")
	void cadastrarVeiculoCenario2(TipoVeiculo tipo, Double capacidade, String placa, Integer anoFabricacao,
			Long idEmpresa, String mensagemDeErro) {

		VeiculoDtoCadastro requestBody = new VeiculoDtoCadastro(tipo, capacidade, placa, anoFabricacao, idEmpresa);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity(URI_PRINCIPAL, requestBody, String.class);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(responseEntity.getBody()).contains(mensagemDeErro);

	}

	@Test
	@DisplayName("Deveria listar veiculo ")
	void listarVeiculosCenario1() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<PageResponse<VeiculoDtoDetalhar>> responseEntity = restTemplate.exchange(URI_PRINCIPAL,
				HttpMethod.GET, null, new ParameterizedTypeReference<PageResponse<VeiculoDtoDetalhar>>() {
				});

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

		PageResponse<VeiculoDtoDetalhar> pageResponse = responseEntity.getBody();

		assertThat(pageResponse.isEmpty()).isFalse();
		assertThat(pageResponse.getContent()).isNotEmpty();
		assertThat(pageResponse.getContent().get(0).id()).isEqualTo(1L);
	}

	@ParameterizedTest
	@MethodSource("parametroListar")
	@DisplayName("Deveria listar veiculo com diferentes valores")
	void listarVeiculosCenario2(String capacidade, TipoVeiculo tipo) {

		ResponseEntity<PageResponse<VeiculoDtoDetalhar>> responseEntity = restTemplate.exchange(
				URI_PRINCIPAL + "?capacidade=" + capacidade + "&tipo=" + tipo, HttpMethod.GET, null,
				new ParameterizedTypeReference<PageResponse<VeiculoDtoDetalhar>>() {
				});

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

		PageResponse<VeiculoDtoDetalhar> pageResponse = responseEntity.getBody();

		assertThat(pageResponse.isEmpty()).isFalse();
		assertThat(pageResponse.getContent()).isNotEmpty();
		assertThat(pageResponse.getContent().get(0).id()).isEqualTo(1L);
	}

	@ParameterizedTest
	@MethodSource("parametroListar")
	@DisplayName("Deveria listar veiculo capacidade com diferentes valores")
	void listarVeiculosCenario3(String capacidade, TipoVeiculo tipo) {

		ResponseEntity<PageResponse<VeiculoDtoDetalhar>> responseEntity = restTemplate.exchange(
				URI_PRINCIPAL + "?capacidade=10", HttpMethod.GET, null,
				new ParameterizedTypeReference<PageResponse<VeiculoDtoDetalhar>>() {
				});

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

		PageResponse<VeiculoDtoDetalhar> pageResponse = responseEntity.getBody();

		assertThat(pageResponse.isEmpty()).isFalse();
		assertThat(pageResponse.getContent()).isNotEmpty();
		assertThat(pageResponse.getContent().get(0).id()).isEqualTo(1L);
	}

	@Test
	@DisplayName("Deveria listar veiculo  pelo id da empresa")
	void listarVeiculoPorEmpresaCenario1() {
		Long idEmpresaExistente = 1L;

		ResponseEntity<PageResponse<VeiculoDtoDetalhar>> responseEntity = restTemplate.exchange(
				URI_PESQUISA_EMPRESA + "/{id}", HttpMethod.GET, null,
				new ParameterizedTypeReference<PageResponse<VeiculoDtoDetalhar>>() {
				}, idEmpresaExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

		PageResponse<VeiculoDtoDetalhar> pageResponse = responseEntity.getBody();

		assertThat(pageResponse.isEmpty()).isFalse();
		assertThat(pageResponse.getContent()).isNotEmpty();
		assertThat(pageResponse.getContent().get(0).id()).isEqualTo(1L);
	}

	@ParameterizedTest
	@MethodSource("parametroListar")
	@DisplayName("Deveria listar veiculo com diferentes valores pelo id da empresa")
	void listarVeiculoPorEmpresaCenario2(String capacidade, TipoVeiculo tipo) {
		Long idEmpresaExistente = 1L;

		ResponseEntity<PageResponse<VeiculoDtoDetalhar>> responseEntity = restTemplate.exchange(
				URI_PESQUISA_EMPRESA + "/{id}?capacidade=" + capacidade + "&tipo=" + tipo, HttpMethod.GET, null,
				new ParameterizedTypeReference<PageResponse<VeiculoDtoDetalhar>>() {
				}, idEmpresaExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

		PageResponse<VeiculoDtoDetalhar> pageResponse = responseEntity.getBody();

		assertThat(pageResponse.isEmpty()).isFalse();
		assertThat(pageResponse.getContent()).isNotEmpty();
		assertThat(pageResponse.getContent().get(0).id()).isEqualTo(1L);
	}

	@Test
	@DisplayName("Deveria listar veiculo capacidade  pelo id da empresa")
	void listarVeiculoPorEmpresaCenario3() {
		Long idEmpresaExistente = 1L;

		ResponseEntity<PageResponse<VeiculoDtoDetalhar>> responseEntity = restTemplate.exchange(
				URI_PESQUISA_EMPRESA + "/{id}?capacidade=10", HttpMethod.GET, null,
				new ParameterizedTypeReference<PageResponse<VeiculoDtoDetalhar>>() {
				}, idEmpresaExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

		PageResponse<VeiculoDtoDetalhar> pageResponse = responseEntity.getBody();

		assertThat(pageResponse.isEmpty()).isFalse();
		assertThat(pageResponse.getContent()).isNotEmpty();
		assertThat(pageResponse.getContent().get(0).id()).isEqualTo(1L);
	}

	@Test
	@DisplayName("Deveria detalhar um  veiculo por ID")
	void detalharVeiculoPorId() {
		Long idVeiculoExistente = 1L;

		ResponseEntity<VeiculoDtoDetalhar> responseEntity = restTemplate.exchange(URI_PRINCIPAL + "/{id}",
				HttpMethod.GET, null, VeiculoDtoDetalhar.class, idVeiculoExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody().id()).isEqualTo(idVeiculoExistente);

	}

	@ParameterizedTest
	@MethodSource("parametrosAtualizar")
	@DisplayName("Deveria atualizar dados do funcionario")
	void atualizarFuncionarioPorIdCenario1(TipoVeiculo tipo, Double capacidade, String placa, Integer anoFabricacao,
			Long idEmpresa) {
		Long idVeiculoExistente = 1L;

		VeiculoDtoAtualizar requestBody = new VeiculoDtoAtualizar(tipo, capacidade, placa, anoFabricacao, idEmpresa);

		ResponseEntity<VeiculoDtoDetalhar> responseEntity = restTemplate.exchange(URI_PRINCIPAL + "/{id}",
				HttpMethod.PUT, new HttpEntity<>(requestBody), VeiculoDtoDetalhar.class, idVeiculoExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody().id()).isEqualTo(idVeiculoExistente);

	}

	@ParameterizedTest
	@MethodSource("parametrosCadastroInvalido")
	@DisplayName("Não Deveria atualizar veiculo com dados invalida")
	void atualizarFuncionarioPorIdCenario2(TipoVeiculo tipo, Double capacidade, String placa, Integer anoFabricacao,
			Long idEmpresa, String mensagemDeErro) {
		Long idVeiculoExistente = 1L;

		VeiculoDtoAtualizar requestBody = new VeiculoDtoAtualizar(tipo, capacidade, placa, anoFabricacao, idEmpresa);

		ResponseEntity<String> responseEntity = restTemplate.exchange(URI_PRINCIPAL + "/{id}", HttpMethod.PUT,
				new HttpEntity<>(requestBody), String.class, idVeiculoExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(responseEntity.getBody()).contains(mensagemDeErro);

	}

	@Test
	@DisplayName("Deveria excluir um veiculo por ID")
	void excluirVeiculoPorId() {
		Long idVeiculoExistente = 1L;

		ResponseEntity<Void> responseEntity = restTemplate.exchange(URI_PRINCIPAL + "/{id}", HttpMethod.DELETE, null,
				Void.class, idVeiculoExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}

	static Stream<Arguments> parametrosCadastroInvalido() {
		return Stream.of(
				Arguments.of(TipoVeiculo.CAMINHAO_COMPACTADOR, 50D, "1234xx8", 1950, 1L,
						"Placa de veiculo já registrado!"),

				Arguments.of(TipoVeiculo.CAMINHAO_COMPACTADOR, 50D, "123f458", 1950, 15L,
						"Id da empresa informada não existe!")

		);
	}

	static Stream<Arguments> parametroListar() {
		return Stream.of(

				Arguments.of("", "CAMINHAO_COMPACTADOR"),

				Arguments.of("20", "CAMINHAO_COMPACTADOR")

		);
	}

	static Stream<Arguments> parametrosAtualizar() {
		return Stream.of(
				Arguments.of(TipoVeiculo.CAMINHAO_COMPACTADOR, null, null, null, null),

				Arguments.of(null, 50D, "123f458", 1950, 1L),

				Arguments.of(TipoVeiculo.CAMINHAO_COMPACTADOR, 50D, "123f459", 1950, 1L)

		);
	}

}
