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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import br.com.magnasistema.apicoleta.dto.destino.DestinoDtoAtualizar;
import br.com.magnasistema.apicoleta.dto.destino.DestinoDtoCadastro;
import br.com.magnasistema.apicoleta.dto.destino.DestinoDtoDetalhar;
import br.com.magnasistema.apicoleta.dto.empresa.EmpresaDtoCadastrar;
import br.com.magnasistema.apicoleta.dto.endereco.EnderecoDtoAtualizar;
import br.com.magnasistema.apicoleta.dto.endereco.EnderecoDtoCadastrar;
import br.com.magnasistema.apicoleta.entity.Cidade;
import br.com.magnasistema.apicoleta.entity.Destino;
import br.com.magnasistema.apicoleta.entity.Empresa;
import br.com.magnasistema.apicoleta.entity.Endereco;
import br.com.magnasistema.apicoleta.entity.Estado;
import br.com.magnasistema.apicoleta.enums.TipoDestino;
import br.com.magnasistema.apicoleta.enums.TipoEmpresa;
import br.com.magnasistema.apicoleta.infra.PageResponse;
import br.com.magnasistema.apicoleta.repository.CidadeRepository;
import br.com.magnasistema.apicoleta.repository.DestinoRepository;
import br.com.magnasistema.apicoleta.repository.EmpresaRepository;
import br.com.magnasistema.apicoleta.repository.EstadoRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class DestinoControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private EstadoRepository estadoRepository;

	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private EmpresaRepository empresaRepository;

	@Autowired
	private DestinoRepository destinoRepository;

	private final String uriPrincipal = "/destino";

	@BeforeEach
	void inicializar() {

		Destino destinoTeste = new Destino("destino teste", TipoDestino.ATERRO_CONTROLADO, 60D, criarEndereco(),
				criarEmpresa());

		destinoRepository.save(destinoTeste);

	}

	@AfterEach
	void finalizar() {

		destinoRepository.deleteAllAndResetSequence();
		cidadeRepository.deleteAllAndResetSequence();
		estadoRepository.deleteAllAndResetSequence();
		empresaRepository.deleteAllAndResetSequence();

	}

	@Test
	@DisplayName("Deveria cadastrar um destino com informações válidas")
	void cadastrarDestinoCenario1() {

		DestinoDtoCadastro requestBody = new DestinoDtoCadastro("destino teste2", TipoDestino.ATERRO_CONTROLADO, 60D,
				new EnderecoDtoCadastrar(1L, "xx", "22", "02211010"), 1L);

		ResponseEntity<DestinoDtoDetalhar> responseEntity = restTemplate.postForEntity(uriPrincipal, requestBody,
				DestinoDtoDetalhar.class);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody().id()).isEqualTo(2L);

	}

	@ParameterizedTest
	@MethodSource("parametrosCadastroInvalido")
	@DisplayName("Não Deveria cadastrar um destino com informações inválidas")
	void cadastrarDestinoCenaro2(String nome, TipoDestino tipo, Double capacidade, EnderecoDtoCadastrar endereco,
			Long idEmpresa, String mensagemDeErro) {

		DestinoDtoCadastro requestBody = new DestinoDtoCadastro(nome, tipo, capacidade, endereco, idEmpresa);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity(uriPrincipal, requestBody, String.class);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(responseEntity.getBody()).contains(mensagemDeErro);

	}

	@ParameterizedTest
	@MethodSource("parametrosAtualizar")
	@DisplayName("Deveria atualizar dados do destino")
	void atualizarDestinoPorIdCenario1(String nome, TipoDestino tipo, Double capacidade, EnderecoDtoAtualizar endereco,
			Long idEmpresa) {
		Long idDestinoExistente = 1L;

		DestinoDtoAtualizar requestBody = new DestinoDtoAtualizar(nome, tipo, capacidade, endereco, idEmpresa);

		ResponseEntity<DestinoDtoDetalhar> responseEntity = restTemplate.exchange(uriPrincipal + "/{id}",
				HttpMethod.PUT, new HttpEntity<>(requestBody), DestinoDtoDetalhar.class, idDestinoExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody().id()).isEqualTo(idDestinoExistente);

	}

	@ParameterizedTest
	@MethodSource("parametrosAtualizarInvalido")
	@DisplayName("Não Deveria atualizar com dados invalida")
	void atualizarDestinoPorIdCenario2(String nome, TipoDestino tipo, Double capacidade, EnderecoDtoAtualizar endereco,
			Long idEmpresa, String mensagemDeErro) {

		Long idDestinoExistente = 1L;

		DestinoDtoAtualizar requestBody = new DestinoDtoAtualizar(nome, tipo, capacidade, endereco, idEmpresa);

		ResponseEntity<String> responseEntity = restTemplate.exchange(uriPrincipal + "/{id}", HttpMethod.PUT,
				new HttpEntity<>(requestBody), String.class, idDestinoExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(responseEntity.getBody()).contains(mensagemDeErro);

	}

	@Test
	@DisplayName("Deveria listar destinos ")
	void listarDestinos() {

		ResponseEntity<PageResponse<DestinoDtoDetalhar>> responseEntity = restTemplate.exchange(uriPrincipal,
				HttpMethod.GET, null, new ParameterizedTypeReference<PageResponse<DestinoDtoDetalhar>>() {
				});

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

		PageResponse<DestinoDtoDetalhar> pageResponse = responseEntity.getBody();

		assertThat(pageResponse.isEmpty()).isFalse();
		assertThat(pageResponse.getContent()).isNotEmpty();
		assertThat(pageResponse.getContent().get(0).id()).isEqualTo(1L);
	}

	@Test
	@DisplayName("Deveria detalhar um destino por ID")
	void detalharDestinoPorId() {
		Long idDestinoExistente = 1L;

		ResponseEntity<DestinoDtoDetalhar> responseEntity = restTemplate.exchange(uriPrincipal + "/{id}",
				HttpMethod.GET, null, DestinoDtoDetalhar.class, idDestinoExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody().id()).isEqualTo(idDestinoExistente);

	}

	@Test
	@DisplayName("Deveria excluir um destino por ID")
	void excluirDestinoPorId() {
		Long idDestinoExistente = 1L;

		ResponseEntity<Void> responseEntity = restTemplate.exchange(uriPrincipal + "/{id}", HttpMethod.DELETE, null,
				Void.class, idDestinoExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}

	static Stream<Arguments> parametrosCadastroInvalido() {
		return Stream.of(

				Arguments.of("destino teste2", TipoDestino.ATERRO_CONTROLADO, 60D,
						new EnderecoDtoCadastrar(1L, "xx", "22", "02211010"), 15L,
						"Id da empresa informada não existe!"),

				Arguments.of("destino teste2", TipoDestino.ATERRO_CONTROLADO, 60D,
						new EnderecoDtoCadastrar(15L, "xx", "22", "02211010"), 1L, "Id da cidade informada não existe!")

		);
	}

	static Stream<Arguments> parametrosAtualizarInvalido() {
		return Stream.of(Arguments.of("destino teste2", TipoDestino.ATERRO_CONTROLADO, 60D,
				new EnderecoDtoAtualizar(1L, "xx", "22", "02211010"), 15L, "Id da empresa informada não existe!"),

				Arguments.of("destino teste2", TipoDestino.ATERRO_CONTROLADO, 60D,
						new EnderecoDtoAtualizar(15L, "xx", "22", "02211010"), 1L, "Id da cidade informada não existe!")

		);
	}

	static Stream<Arguments> parametrosAtualizar() {
		return Stream.of(
				Arguments.of("destino teste2", null, null, new EnderecoDtoAtualizar(null, null, null, null), null),

				Arguments.of("", TipoDestino.ATERRO_CONTROLADO, 60D, new EnderecoDtoAtualizar(1L, "", "", "02211010"),
						1L),

				Arguments.of(null, TipoDestino.ATERRO_CONTROLADO, 60D,
						new EnderecoDtoAtualizar(1L, "xx", "22", "02211010"), 1L),

				Arguments.of("destino", TipoDestino.ATERRO_CONTROLADO, 60D,
						new EnderecoDtoAtualizar(1L, "xx", "22", "02211010"), 1L),

				Arguments.of("destino", TipoDestino.ATERRO_CONTROLADO, 60D, null, 1L)

		);
	}

	private Empresa criarEmpresa() {

		Empresa empresaTeste = new Empresa(new EmpresaDtoCadastrar("empresa teste", "012345678912345", "400289222",
				"g@hotmail.com", TipoEmpresa.PUBLICA));

		empresaRepository.save(empresaTeste);

		return empresaTeste;
	}

	private Endereco criarEndereco() {
		
		Estado estadoTeste = new Estado("estado teste ", "uf");
		Cidade cidadeTeste = new Cidade("cidade teste", estadoTeste);

		estadoRepository.save(estadoTeste);
		cidadeRepository.save(cidadeTeste);

		Endereco enderecoTeste = new Endereco(new EnderecoDtoCadastrar(1L, "xx", "22", "02211010"), cidadeTeste);

		return enderecoTeste;
	}



}
