package br.com.magnasistema.apicoleta.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

import br.com.magnasistema.apicoleta.dto.coleta.ColetaDtoAtualizar;
import br.com.magnasistema.apicoleta.dto.coleta.ColetaDtoCadastro;
import br.com.magnasistema.apicoleta.dto.coleta.ColetaDtoDetalhar;
import br.com.magnasistema.apicoleta.dto.coleta.ColetaDtoModificar;
import br.com.magnasistema.apicoleta.dto.empresa.EmpresaDtoCadastrar;
import br.com.magnasistema.apicoleta.dto.endereco.EnderecoDtoCadastrar;
import br.com.magnasistema.apicoleta.entity.Bairro;
import br.com.magnasistema.apicoleta.entity.Cidade;
import br.com.magnasistema.apicoleta.entity.Coleta;
import br.com.magnasistema.apicoleta.entity.Destino;
import br.com.magnasistema.apicoleta.entity.Empresa;
import br.com.magnasistema.apicoleta.entity.Endereco;
import br.com.magnasistema.apicoleta.entity.Equipe;
import br.com.magnasistema.apicoleta.entity.Estado;
import br.com.magnasistema.apicoleta.entity.Funcionario;
import br.com.magnasistema.apicoleta.entity.FuncionarioEquipe;
import br.com.magnasistema.apicoleta.entity.Veiculo;
import br.com.magnasistema.apicoleta.enums.TipoDestino;
import br.com.magnasistema.apicoleta.enums.TipoEmpresa;
import br.com.magnasistema.apicoleta.enums.TipoFuncao;
import br.com.magnasistema.apicoleta.enums.TipoVeiculo;
import br.com.magnasistema.apicoleta.infra.PageResponse;
import br.com.magnasistema.apicoleta.repository.BairroRepository;
import br.com.magnasistema.apicoleta.repository.CidadeRepository;
import br.com.magnasistema.apicoleta.repository.ColetaRepository;
import br.com.magnasistema.apicoleta.repository.DestinoRepository;
import br.com.magnasistema.apicoleta.repository.EmpresaRepository;
import br.com.magnasistema.apicoleta.repository.EquipeRepository;
import br.com.magnasistema.apicoleta.repository.EstadoRepository;
import br.com.magnasistema.apicoleta.repository.FuncionarioEquipeRepository;
import br.com.magnasistema.apicoleta.repository.FuncionarioRepository;
import br.com.magnasistema.apicoleta.repository.VeiculoRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ColetaControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private EstadoRepository estadoRepository;

	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private BairroRepository bairroRepository;

	@Autowired
	private EmpresaRepository empresaRepository;

	@Autowired
	private DestinoRepository destinoRepository;

	@Autowired
	private VeiculoRepository veiculoRepository;

	@Autowired
	private EquipeRepository equipeRepository;

	@Autowired
	private FuncionarioEquipeRepository funcionarioEquipeRepository;

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	@Autowired
	private ColetaRepository coletaRepository;

	private final String URI_PRINCIPAL = "/coleta";

	private final String URI_PESQUISA_EQUIPE = "/coleta/equipe";

	private final String URI_MODIFICAR = "/coleta/modificar";

	private Cidade cidadeTesteGoblal;
	
	private Empresa empresaTesteGoblal;

	@BeforeEach
	void inicializar() {

		cidadeTesteGoblal = criarCidade();
		empresaTesteGoblal = criarEmpresa();

		criarDestino("012345678999345", "gdestino@hotmail.com", TipoDestino.RECICLAGEM);

		criarBairro();

	}

	@AfterEach
	void finalizar() {

		coletaRepository.deleteAllAndResetSequence();
		funcionarioEquipeRepository.deleteAllAndResetSequence();
		equipeRepository.deleteAllAndResetSequence();
		funcionarioRepository.deleteAllAndResetSequence();
		destinoRepository.deleteAllAndResetSequence();
		veiculoRepository.deleteAllAndResetSequence();
		empresaRepository.deleteAllAndResetSequence();
		bairroRepository.deleteAllAndResetSequence();
		cidadeRepository.deleteAllAndResetSequence();
		estadoRepository.deleteAllAndResetSequence();

	}

	@ParameterizedTest
	@MethodSource("parametrosCadastroInvalido")
	@DisplayName("Não Deveria cadastrar coleta com entidade inválidas")
	void cadastrarColetaCenario1(Long idEquipe, Long idBairro, Long idDestino, String mensagemDeErro) {

		criarEquipe(TipoVeiculo.CAMINHAO_DE_RECICLAGEM);

		ColetaDtoCadastro requestBody = new ColetaDtoCadastro(LocalDateTime.of(2023, 10, 2, 15, 30), null, null,
				idEquipe, idBairro, idDestino);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity(URI_PRINCIPAL, requestBody, String.class);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody()).contains(mensagemDeErro);

	}

	@ParameterizedTest
	@MethodSource("parametrosTimestampInvalido")
	@DisplayName("Não Deveria cadastrar coleta timestamp inválido")
	void cadastrarColetaCenario2(LocalDateTime timestampInicial, LocalDateTime timestampFinal, Double peso,
			String mensagemDeErro) {

		criarEquipe(TipoVeiculo.CAMINHAO_DE_RECICLAGEM);

		ColetaDtoCadastro requestBody = new ColetaDtoCadastro(timestampInicial, timestampFinal, peso, 1L, 1L, 1L);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity(URI_PRINCIPAL, requestBody, String.class);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody()).contains(mensagemDeErro);

	}

	@Test
	@DisplayName("Deveria cadastrar coleta com dados Validos")
	void cadastrarColetaCenario3() {

		criaColetaCompleta();

		ColetaDtoCadastro requestBody = new ColetaDtoCadastro(LocalDateTime.of(2023, 10, 2, 18, 30),
				LocalDateTime.of(2023, 10, 2, 19, 30), 50D, 1L, 1L, 1L);

		ResponseEntity<ColetaDtoDetalhar> responseEntity = restTemplate.postForEntity(URI_PRINCIPAL, requestBody,
				ColetaDtoDetalhar.class);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody().bairro().getNome()).isEqualTo("bairro teste");

	}

	@Test
	@DisplayName("Deveria cadastrar coleta com dados Validos Sem Ultima Coleta feita")
	void cadastrarColetaCenario4() {

		criarEquipe(TipoVeiculo.CAMINHAO_DE_RECICLAGEM);

		ColetaDtoCadastro requestBody = new ColetaDtoCadastro(LocalDateTime.of(2023, 10, 2, 18, 30), null, null, 1L, 1L,
				1L);

		ResponseEntity<ColetaDtoDetalhar> responseEntity = restTemplate.postForEntity(URI_PRINCIPAL, requestBody,
				ColetaDtoDetalhar.class);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody().bairro().getNome()).isEqualTo("bairro teste");

	}

	@Test
	@DisplayName("Não Deveria cadastrar coleta se a ultima nao foi finalizada")
	void cadastrarColetaCenario5() {

		criaColetaIncompleta();

		ColetaDtoCadastro requestBody = new ColetaDtoCadastro(LocalDateTime.of(2023, 10, 2, 18, 30), null, null, 1L, 1L,
				2L);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity(URI_PRINCIPAL, requestBody, String.class);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody())
				.contains("A equipe precisa finalizar a ultima coleta para registrar uma nova");

	}

	@ParameterizedTest
	@MethodSource("parametrosTimestampDuplicado")
	@DisplayName("Não Deveria cadastrar se tiver duplicada de timestamp")
	void cadastrarColetaCenario6(LocalDateTime timestampInicial, LocalDateTime timestampFinal) {

		criaColetaIncompleta();

		ColetaDtoCadastro requestBody = new ColetaDtoCadastro(timestampInicial, timestampFinal, 50D, 1L, 1L, 1L);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity(URI_PRINCIPAL, requestBody, String.class);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody()).contains("A equipe já tem uma coleta nesse timestamp");

	}

	@Test
	@DisplayName("Não Deveria cadastrar peso da coleta maior que a capacidade do veiculo")
	void cadastrarColetaCenario7() {

		criarEquipe(TipoVeiculo.CAMINHAO_DE_RECICLAGEM);

		ColetaDtoCadastro requestBody = new ColetaDtoCadastro(LocalDateTime.of(2023, 10, 2, 18, 30),
				LocalDateTime.of(2023, 10, 2, 19, 30), 800D, 1L, 1L, 1L);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity(URI_PRINCIPAL, requestBody, String.class);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody()).contains("O peso da coleta excede a capacidade do veículo da equipe.");

	}

	@Test
	@DisplayName("Não Deveria cadastrar coleta de caminhao de reciclagem em destino diferente de reciclagem")
	void cadastrarColetaCenario8() {

		criarEquipe(TipoVeiculo.CAMINHAO_DE_RECICLAGEM);

		criarDestino("012345678999111", "g111@hotmail.com", TipoDestino.ATERRO_CONTROLADO);

		ColetaDtoCadastro requestBody = new ColetaDtoCadastro(LocalDateTime.of(2023, 10, 2, 18, 30), null, null, 1L, 1L,
				2L);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity(URI_PRINCIPAL, requestBody, String.class);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody())
				.contains("Se o veículo da equipe é do tipo reciclagem, o destino também deve ser do tipo reciclagem.");

	}

	@ParameterizedTest
	@MethodSource("parametrosExcluirFuncionarioEquipe")
	@DisplayName("Não Deveria cadastrar coleta com equipe incompleta")
	void cadastrarColetaCenario9(Long id) {

		criarEquipe(TipoVeiculo.CAMINHAO_DE_RECICLAGEM);

		funcionarioEquipeRepository.deleteById(id);

		ColetaDtoCadastro requestBody = new ColetaDtoCadastro(LocalDateTime.of(2023, 10, 2, 18, 30),
				LocalDateTime.of(2023, 10, 2, 19, 30), 800D, 1L, 1L, 1L);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity(URI_PRINCIPAL, requestBody, String.class);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody()).contains("A equipe precisa ter pelo menos 1 coletor e 1 motorista.");

	}

	@ParameterizedTest
	@MethodSource("parametrosTimestampInvalidoAtualizar")
	@DisplayName("Não Deveria atualizar coleta timestamp invalido")
	void atualizarColetaCenario1(LocalDateTime timeStampFinal, Double peso, String mensagemDeErro) {

		criaColetaIncompleta();

		Long idDoColetaExistente = 1L;

		ColetaDtoAtualizar requestBody = new ColetaDtoAtualizar(timeStampFinal, peso);

		ResponseEntity<String> responseEntity = restTemplate.exchange(URI_PRINCIPAL + "/{id}", HttpMethod.PUT,
				new HttpEntity<>(requestBody), String.class, idDoColetaExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody()).contains(mensagemDeErro);

	}

	@Test
	@DisplayName("Não Deveria atualizar coleta se ja foi finalizada")
	void atualizarColetaCenario2() {

		criaColetaCompleta();

		Long idDoColetaExistente = 1L;

		ColetaDtoAtualizar requestBody = new ColetaDtoAtualizar(LocalDateTime.of(2023, 10, 2, 17, 30), 40D);

		ResponseEntity<String> responseEntity = restTemplate.exchange(URI_PRINCIPAL + "/{id}", HttpMethod.PUT,
				new HttpEntity<>(requestBody), String.class, idDoColetaExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody()).contains("A coleta já foi finalizada e não pode ser atualizada.");

	}

	@Test
	@DisplayName("Deveria atualizar coleta com dados validos")
	void atualizarColetaCenario3() {

		criaColetaIncompleta();

		Long idDoColetaExistente = 1L;

		ColetaDtoAtualizar requestBody = new ColetaDtoAtualizar(LocalDateTime.of(2023, 10, 2, 16, 30), 50D);

		ResponseEntity<ColetaDtoDetalhar> responseEntity = restTemplate.exchange(URI_PRINCIPAL + "/{id}", HttpMethod.PUT,
				new HttpEntity<>(requestBody), ColetaDtoDetalhar.class, idDoColetaExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody().id()).isEqualTo(idDoColetaExistente);

	}

	@ParameterizedTest
	@MethodSource("parametrosModificarColeta")
	@DisplayName("Deveria modificar coleta com dados validos")
	void modificiarColetaCenario1(Double peso, Long idDestino) {

		criaColetaCompleta();

		Long idDoColetaExistente = 1L;

		ColetaDtoModificar requestBody = new ColetaDtoModificar(peso, idDestino);

		ResponseEntity<ColetaDtoDetalhar> responseEntity = restTemplate.exchange(URI_MODIFICAR + "/{id}", HttpMethod.PUT,
				new HttpEntity<>(requestBody), ColetaDtoDetalhar.class, idDoColetaExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody().id()).isEqualTo(idDoColetaExistente);

	}

	@Test
	@DisplayName("Não Deveria modificar coleta com destino invalido")
	void modificiarColetaCenario1() {

		criaColetaCompleta();

		Long idDoColetaExistente = 1L;

		ColetaDtoModificar requestBody = new ColetaDtoModificar(10D, 15L);

		ResponseEntity<String> responseEntity = restTemplate.exchange(URI_MODIFICAR + "/{id}", HttpMethod.PUT,
				new HttpEntity<>(requestBody), String.class, idDoColetaExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody()).contains("Id do Destino não encontrado");

	}

	@Test
	@DisplayName("Deveria listar coletas pelo id ")
	void listarColetaCenario1() {

		criaColetaCompleta();

		Long idEquipeExistente = 1L;

		ResponseEntity<PageResponse<ColetaDtoDetalhar>> responseEntity = restTemplate.exchange(URI_PRINCIPAL,
				HttpMethod.GET, null, new ParameterizedTypeReference<PageResponse<ColetaDtoDetalhar>>() {
				}, idEquipeExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isNotNull();

		PageResponse<ColetaDtoDetalhar> pageResponse = responseEntity.getBody();

		assertThat(pageResponse.isEmpty()).isFalse();
		assertThat(pageResponse.getContent()).isNotEmpty();
		assertThat(pageResponse.getContent()).isNotEmpty();
		assertThat(pageResponse.getContent().get(0).id()).isEqualTo(1L);

	}

	@Test
	@DisplayName("Deveria detalhar coleta pelo id ")
	void detalharColetaCenario1() {

		criaColetaCompleta();

		Long idEquipeExistente = 1L;

		ResponseEntity<ColetaDtoDetalhar> responseEntity = restTemplate.exchange(URI_PRINCIPAL + "/{id}", HttpMethod.GET,
				null, ColetaDtoDetalhar.class, idEquipeExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody().id()).isEqualTo(idEquipeExistente);

	}

	@Test
	@DisplayName("Deveria retornar 404 com id invalido ")
	void erro404() {

		Long idNaoExistente = 15L;

		ResponseEntity<String> responseEntity = restTemplate.exchange(URI_PRINCIPAL + "/{id}", HttpMethod.GET, null,
				String.class, idNaoExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

	}

	@Test
	@DisplayName("Deveria retornar 500")
	void erro400() {

		Long idNaoExistente = 15L;

		ResponseEntity<String> responseEntity = restTemplate.exchange(URI_PESQUISA_EQUIPE + "?nome", HttpMethod.GET, null,
				String.class, idNaoExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@Test
	@DisplayName("Deveria listar coletas  pelo id da Equipe")
	void listarColetaPorEquipeCenario1() {

		criaColetaCompleta();

		Long idEquipeExistente = 1L;

		ResponseEntity<PageResponse<ColetaDtoDetalhar>> responseEntity = restTemplate.exchange(
				URI_PESQUISA_EQUIPE + "/{id}", HttpMethod.GET, null,
				new ParameterizedTypeReference<PageResponse<ColetaDtoDetalhar>>() {
				}, idEquipeExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isNotNull();

		PageResponse<ColetaDtoDetalhar> pageResponse = responseEntity.getBody();

		assertThat(pageResponse.isEmpty()).isFalse();
		assertThat(pageResponse.getContent()).isNotEmpty();
		assertThat(pageResponse.getContent().get(0).id()).isEqualTo(1L);

	}

	@Test
	@DisplayName("Deveria excluir uma Coleta por ID")
	void excluirColetaPorId() {

		criaColetaCompleta();

		Long idColetaExistente = 1L;

		ResponseEntity<Void> responseEntity = restTemplate.exchange(URI_PRINCIPAL + "/{id}", HttpMethod.DELETE, null,
				Void.class, idColetaExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}

	@Test
	@DisplayName("Não Deveria excluir equipe se existe Coleta registrada")
	void excluirColetaComColetaRegistrada() {

		criaColetaCompleta();

		Long idEquipeExistente = 1L;

		ResponseEntity<String> responseEntity = restTemplate.exchange("/equipe/{id}", HttpMethod.DELETE, null,
				String.class, idEquipeExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody())
				.contains("Não é possível excluir devido existir coleta registrada com essa entidade.");
	}

	static Stream<Arguments> parametrosModificarColeta() {
		return Stream.of(

				Arguments.of(null, 1L),

				Arguments.of(20D, null),

				Arguments.of(20D, 1L));
	}

	static Stream<Arguments> parametrosTimestampInvalidoAtualizar() {
		return Stream.of(

				Arguments.of(LocalDateTime.of(2023, 10, 2, 15, 30), 50D, "A equipe já tem uma coleta nesse timestamp"),

				Arguments.of(LocalDateTime.of(2023, 10, 2, 06, 30), 50d,
						"O timestampFinal deve ser depois do timestampInicial"),

				Arguments.of(LocalDateTime.of(2023, 10, 2, 16, 30), 800D,
						"O peso da coleta excede a capacidade do veículo da equipe."));
	}

	static Stream<Arguments> parametrosCadastroInvalido() {
		return Stream.of(

				Arguments.of(15L, 1L, 1L, "Id da Equipe não encontrado"),

				Arguments.of(1L, 15L, 1L, "Id do Bairro não encontrado"),

				Arguments.of(1L, 1L, 15L, "Id do Destino não encontrado")

		);
	}

	static Stream<Arguments> parametrosTimestampInvalido() {
		return Stream.of(

				Arguments.of(LocalDateTime.of(2023, 10, 2, 15, 30), LocalDateTime.of(2023, 10, 2, 13, 30), 50D,
						"O timestampFinal deve ser depois do timestampInicial."),

				Arguments.of(LocalDateTime.of(2023, 10, 2, 15, 30), LocalDateTime.of(2023, 10, 2, 15, 30), 50D,
						"O timestampFinal deve ser depois do timestampInicial."),

				Arguments.of(LocalDateTime.of(2023, 10, 2, 15, 30), LocalDateTime.of(2023, 10, 2, 16, 30), null,
						"Se a coleta foi finalizada é necessario informar o peso"),

				Arguments.of(LocalDateTime.of(2023, 10, 2, 15, 30), null, 50D,
						"Para adicionar peso da coleta é necessario informar o timestampFinal da coleta"));
	}

	static Stream<Arguments> parametrosTimestampDuplicado() {
		return Stream.of(

				Arguments.of(LocalDateTime.of(2023, 10, 2, 15, 30), LocalDateTime.of(2023, 10, 2, 16, 30), 50D),

				Arguments.of(LocalDateTime.of(2023, 10, 2, 06, 30), LocalDateTime.of(2023, 10, 2, 15, 30), 50D)

		);
	}

	static Stream<Arguments> parametrosExcluirFuncionarioEquipe() {
		return Stream.of(

				Arguments.of(1L),

				Arguments.of(2L)

		);
	}

	private Coleta criaColetaIncompleta() {

		var equipe =criarEquipe(TipoVeiculo.CAMINHAO_COMPACTADOR);
				
		var destino = criarDestino("012345678999111", "g111@hotmail.com", TipoDestino.ATERRO_CONTROLADO);
		
		
		Coleta coleta = new Coleta(
				new ColetaDtoCadastro(LocalDateTime.of(2023, 10, 2, 15, 30), null, null, null, null, null),
				equipe, criarBairro(),
				destino);

		coletaRepository.save(coleta);

		return coleta;
	}

	private Coleta criaColetaCompleta() {

		var equipe = criarEquipe(TipoVeiculo.CAMINHAO_COMPACTADOR);

		var destino = criarDestino("012345678999111", "g111@hotmail.com", TipoDestino.ATERRO_CONTROLADO);

		Coleta coleta = new Coleta(new ColetaDtoCadastro(LocalDateTime.of(2023, 10, 2, 15, 30),
				LocalDateTime.of(2023, 10, 2, 16, 30), 50D, null, null, null), equipe, criarBairro(), destino);

		coletaRepository.save(coleta);

		return coleta;
	}

	private Destino criarDestino(String cnpj, String Email, TipoDestino tipo) {

		Empresa empresaTeste = new Empresa(
				new EmpresaDtoCadastrar("empresa teste", cnpj, "400289222", Email, TipoEmpresa.PUBLICA));

		Endereco enderecoTeste = new Endereco(new EnderecoDtoCadastrar(1L, "xx", "22", "02211010"), cidadeTesteGoblal);

		Destino destinoTeste = new Destino("destino teste", tipo, 60D, enderecoTeste, empresaTeste);

		empresaRepository.save(empresaTeste);
		destinoRepository.save(destinoTeste);

		return destinoTeste;
	}

	private Equipe criarEquipe(TipoVeiculo tipo) {

		Veiculo veiculoTeste = new Veiculo(tipo, 50D, "1234xx8", 1950, empresaTesteGoblal);

		veiculoRepository.save(veiculoTeste);

		Equipe equipe = new Equipe("equipe teste", "descricao", veiculoTeste);

		popularEquipeCompleta(equipe);

		return equipe;

	}

	private Empresa criarEmpresa() {

		Empresa empresaTeste = new Empresa(new EmpresaDtoCadastrar("empresa teste", "111119999912345", "400289222",
				"g999@hotmail.com", TipoEmpresa.PUBLICA));

		empresaRepository.save(empresaTeste);

		return empresaTeste;
	}

	private Bairro criarBairro() {

		Bairro bairroTeste = new Bairro("bairro teste", "logradouros", cidadeTesteGoblal);

		bairroRepository.save(bairroTeste);

		return bairroTeste;
	}

	private Cidade criarCidade() {

		Estado estadoTeste = new Estado("estado teste ", "uf");
		Cidade cidadeTeste = new Cidade("cidade teste", estadoTeste);

		estadoRepository.save(estadoTeste);
		cidadeRepository.save(cidadeTeste);

		return cidadeTeste;
	}

	private void popularEquipeCompleta(Equipe equipe) {

		var funcionarioTeste1 = new Funcionario("funcionario teste", "11133366622", LocalDate.of(1990, 5, 15),
				"fy3@gmail.com", TipoFuncao.COLETOR, empresaTesteGoblal);

		var funcionarioTeste2 = new Funcionario("funcionario teste", "21133366622", LocalDate.of(1990, 5, 15),
				"fy2@gmail.com", TipoFuncao.MOTORISTA, empresaTesteGoblal);

		var funcionarioTeste3 = new Funcionario("funcionario teste", "31133366622", LocalDate.of(1990, 5, 15),
				"fy1@gmail.com", TipoFuncao.COLETOR_AUXILIAR, empresaTesteGoblal);

		var funcionarioTeste4 = new Funcionario("funcionario teste", "36133366622", LocalDate.of(1990, 5, 15),
				"fy1s@gmail.com", TipoFuncao.SUPERVISOR, empresaTesteGoblal);

		List<FuncionarioEquipe> funcionarioEquipeList = new ArrayList<>();
		funcionarioEquipeList.add(new FuncionarioEquipe(funcionarioTeste1, equipe));
		funcionarioEquipeList.add(new FuncionarioEquipe(funcionarioTeste2, equipe));
		funcionarioEquipeList.add(new FuncionarioEquipe(funcionarioTeste3, equipe));
		funcionarioEquipeList.add(new FuncionarioEquipe(funcionarioTeste4, equipe));

		funcionarioEquipeRepository.saveAll(funcionarioEquipeList);

	}

}
