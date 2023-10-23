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

import br.com.magnasistema.apicoleta.dto.empresa.EmpresaDtoCadastrar;
import br.com.magnasistema.apicoleta.dto.equipe.EquipeDtoCadastro;
import br.com.magnasistema.apicoleta.dto.equipe.EquipeDtoDetalhar;
import br.com.magnasistema.apicoleta.entity.Empresa;
import br.com.magnasistema.apicoleta.entity.Equipe;
import br.com.magnasistema.apicoleta.entity.Veiculo;
import br.com.magnasistema.apicoleta.enums.TipoEmpresa;
import br.com.magnasistema.apicoleta.enums.TipoVeiculo;
import br.com.magnasistema.apicoleta.infra.PageResponse;
import br.com.magnasistema.apicoleta.repository.EmpresaRepository;
import br.com.magnasistema.apicoleta.repository.EquipeRepository;
import br.com.magnasistema.apicoleta.repository.VeiculoRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class EquipeControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private EquipeRepository equipeRepository;

	@Autowired
	private EmpresaRepository empresaRepository;

	@Autowired
	private VeiculoRepository veiculoRepository;

	private final String URI_PRINCIPAL = "/equipe";

	@BeforeEach
	void inicializar() {

		Equipe equipeTeste = new Equipe("equipe teste", "descricao", criarVeiculo());

		equipeRepository.save(equipeTeste);

	}

	@AfterEach
	void finalizar() {

		equipeRepository.deleteAllAndResetSequence();
		veiculoRepository.deleteAllAndResetSequence();
		empresaRepository.deleteAllAndResetSequence();

	}

	@Test
	@DisplayName("Deveria cadastrar uma com informações válidas")
	void cadastrarEquipeCenario1() {

		EquipeDtoCadastro requestBody = new EquipeDtoCadastro("equipe teste2", "coleta", 1L);

		ResponseEntity<EquipeDtoDetalhar> responseEntity = restTemplate.postForEntity(URI_PRINCIPAL,
				new HttpEntity<>(requestBody), EquipeDtoDetalhar.class);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody().nome()).isEqualTo("equipe teste2");

	}

	@Test
	@DisplayName("Deveria manda exception ao usar veiculo invalida")
	void cadastrarEquipeCenario2() {

		EquipeDtoCadastro requestBody = new EquipeDtoCadastro("equipe teste2", "coleta", 15L);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity(URI_PRINCIPAL, new HttpEntity<>(requestBody),
				String.class);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(responseEntity.getBody()).isEqualTo("Id do veiculo informada não existe!");

	}

	@ParameterizedTest
	@MethodSource("parametrosAtualizar")
	@DisplayName("Deveria atualizar uma com informações válidas")
	void atualizarEquipeCenario1(String nome, String descricao, Long idVeiculo) {

		Long idEquipeExistente = 1L;

		EquipeDtoCadastro requestBody = new EquipeDtoCadastro(nome, descricao, idVeiculo);

		ResponseEntity<EquipeDtoDetalhar> responseEntity = restTemplate.exchange(URI_PRINCIPAL + "/{id}", HttpMethod.PUT,
				new HttpEntity<>(requestBody), EquipeDtoDetalhar.class, idEquipeExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody().id()).isEqualTo(idEquipeExistente);

	}

	@Test
	@DisplayName("Não Deveria atualizar e mandar exception ao usar veiculo invalida")
	void atualizarEquipeCenario2() {

		Long idEquipeExistente = 1L;

		EquipeDtoCadastro requestBody = new EquipeDtoCadastro("equipe teste2", "coleta", 15L);

		ResponseEntity<String> responseEntity = restTemplate.exchange(URI_PRINCIPAL + "/{id}", HttpMethod.PUT,
				new HttpEntity<>(requestBody), String.class, idEquipeExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(responseEntity.getBody()).isEqualTo("Id do veiculo informada não existe!");

	}

	@Test
	@DisplayName("Deveria listar equipes ")
	void listarEquipes() {

		ResponseEntity<PageResponse<EquipeDtoDetalhar>> responseEntity = restTemplate.exchange(URI_PRINCIPAL,
				HttpMethod.GET, null, new ParameterizedTypeReference<PageResponse<EquipeDtoDetalhar>>() {
				});

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

		PageResponse<EquipeDtoDetalhar> pageResponse = responseEntity.getBody();

		assertThat(pageResponse.isEmpty()).isFalse();
		assertThat(pageResponse.getContent()).isNotEmpty();
		assertThat(pageResponse.getContent().get(0).id()).isEqualTo(1L);
	}

	@Test
	@DisplayName("Deveria detalhar uma equipe por ID")
	void detalharEquipePorId() {
		Long idEquipeExistente = 1L;

		ResponseEntity<EquipeDtoDetalhar> responseEntity = restTemplate.exchange(URI_PRINCIPAL + "/{id}", HttpMethod.GET,
				null, EquipeDtoDetalhar.class, idEquipeExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody().id()).isEqualTo(idEquipeExistente);

	}

	@Test
	@DisplayName("Deveria excluir uma equipe por ID")
	void excluirEquipePorId() {
		Long idEquipeExistente = 1L;

		ResponseEntity<Void> responseEntity = restTemplate.exchange(URI_PRINCIPAL + "/{id}", HttpMethod.DELETE, null,
				Void.class, idEquipeExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}

	static Stream<Arguments> parametrosAtualizar() {
		return Stream.of(Arguments.of("", "", 1L),

				Arguments.of(null, null, 1L),

				Arguments.of("x", "y", 1L),

				Arguments.of("x", "y", null)

		);
	}

	private Veiculo criarVeiculo() {

		Empresa empresaTeste = new Empresa(new EmpresaDtoCadastrar("empresa teste", "012345678912345", "400289222",
				"g@hotmail.com", TipoEmpresa.PUBLICA));

		Veiculo veiculoTeste = new Veiculo(TipoVeiculo.CAMINHAO_COMPACTADOR, 50D, "1234xx8", 1950, empresaTeste);

		empresaRepository.save(empresaTeste);
		veiculoRepository.save(veiculoTeste);

		return veiculoTeste;
	}
}
