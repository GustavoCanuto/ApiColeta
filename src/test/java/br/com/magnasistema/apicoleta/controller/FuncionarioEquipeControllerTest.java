package br.com.magnasistema.apicoleta.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import br.com.magnasistema.apicoleta.dto.empresa.EmpresaDtoCadastrar;
import br.com.magnasistema.apicoleta.dto.funcionario.FuncionarioDtoListagemEquipes;
import br.com.magnasistema.apicoleta.dto.funcionarioequipe.FuncionarioEquipeDtoCadastro;
import br.com.magnasistema.apicoleta.dto.funcionarioequipe.FuncionarioEquipeDtoDetalhar;
import br.com.magnasistema.apicoleta.entity.Empresa;
import br.com.magnasistema.apicoleta.entity.Equipe;
import br.com.magnasistema.apicoleta.entity.Funcionario;
import br.com.magnasistema.apicoleta.entity.FuncionarioEquipe;
import br.com.magnasistema.apicoleta.entity.Veiculo;
import br.com.magnasistema.apicoleta.enums.TipoEmpresa;
import br.com.magnasistema.apicoleta.enums.TipoFuncao;
import br.com.magnasistema.apicoleta.enums.TipoVeiculo;
import br.com.magnasistema.apicoleta.repository.EmpresaRepository;
import br.com.magnasistema.apicoleta.repository.EquipeRepository;
import br.com.magnasistema.apicoleta.repository.FuncionarioEquipeRepository;
import br.com.magnasistema.apicoleta.repository.FuncionarioRepository;
import br.com.magnasistema.apicoleta.repository.VeiculoRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class FuncionarioEquipeControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private FuncionarioEquipeRepository funcionarioEquipeRepository;

	@Autowired
	private EquipeRepository equipeRepository;

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	@Autowired
	private EmpresaRepository empresaRepository;

	@Autowired
	private VeiculoRepository veiculoRepository;

	private final String URI_PRINCIPAL = "/funcionarioequipe";

	private Equipe equipeTesteGoblal;
	private Empresa empresaTesteGoblal;

	@BeforeEach
	void inicializar() {

		empresaTesteGoblal = criarEmpresa();

		equipeTesteGoblal = criarEquipe();

	}

	@AfterEach
	void finalizar() {

		funcionarioEquipeRepository.deleteAllAndResetSequence();
		equipeRepository.deleteAllAndResetSequence();
		funcionarioRepository.deleteAllAndResetSequence();
		veiculoRepository.deleteAllAndResetSequence();
		empresaRepository.deleteAllAndResetSequence();

	}

	@ParameterizedTest
	@MethodSource("parametrosFuncao")
	@DisplayName("Deveria cadastrar um funcionario na Equipe com informações válidas independente da funcao")
	void cadastrarFuncionarioEquipeCenario1(TipoFuncao funcao) {

		equipeRepository.save(equipeTesteGoblal);

		funcionarioRepository.save(criarFuncionario(funcao));

		FuncionarioEquipeDtoCadastro requestBody = new FuncionarioEquipeDtoCadastro(1L, 1L);

		ResponseEntity<FuncionarioEquipeDtoDetalhar> responseEntity = restTemplate.postForEntity(URI_PRINCIPAL,
				requestBody, FuncionarioEquipeDtoDetalhar.class);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody().funcionario().getFuncao()).isEqualTo(funcao);

	}

	@Test
	@DisplayName("Não Deveria cadastrar duas vezes o funcionario na mesma equipe")
	void cadastrarFuncionarioEquipeCenario2() {

		popularEquipe(equipeTesteGoblal, criarFuncionario(TipoFuncao.COLETOR));

		FuncionarioEquipeDtoCadastro requestBody = new FuncionarioEquipeDtoCadastro(1L, 1L);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity(URI_PRINCIPAL, new HttpEntity<>(requestBody),
				String.class);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody()).contains("O funcionário já está na equipe.");

	}

	@ParameterizedTest
	@MethodSource("parametrosFuncaoUnica")
	@DisplayName("Irá testa funcao duplicada")
	void cadastrarFuncionarioEquipeCenario3(TipoFuncao funcao, String mensagemDeErro) {

		popularEquipeCompleta(equipeTesteGoblal);

		funcionarioRepository.save(criarFuncionario(funcao));

		FuncionarioEquipeDtoCadastro requestBody = new FuncionarioEquipeDtoCadastro(5L, 1L);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity(URI_PRINCIPAL, requestBody, String.class);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody()).isEqualTo(mensagemDeErro);

	}

	@ParameterizedTest
	@MethodSource("parametrosCadastroInvalido")
	@DisplayName("Não Deveria cadastrar com informações inválidas")
	void cadastrarFuncionarioEquipeCenario4(Long idFuncionario, Long idEquipe, String mensagemDeErro) {

		equipeRepository.save(equipeTesteGoblal);
		
		FuncionarioEquipeDtoCadastro requestBody = new FuncionarioEquipeDtoCadastro(idFuncionario, idEquipe);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity(URI_PRINCIPAL, requestBody, String.class);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody()).isEqualTo(mensagemDeErro);

	}

	@Test
	@DisplayName("Validar regra Limite Coletores")
	void cadastrarFuncionarioEquipeCenario5() {

		popularEquipeComColetores(equipeTesteGoblal);

		FuncionarioEquipeDtoCadastro requestBody = new FuncionarioEquipeDtoCadastro(5L, 1L);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity(URI_PRINCIPAL, requestBody, String.class);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody()).isEqualTo("A equipe já tem 4 coletores");
	}

	@Test
	@DisplayName("Deveria excluir um Funcionario de Uma Equipe por ID")
	void excluirFuncionarioEquipePorId() {

		funcionarioRepository.save(criarFuncionario(TipoFuncao.COLETOR));

		Long idFuncionarioEquipeExistente = 1L;

		ResponseEntity<Void> responseEntity = restTemplate.exchange(URI_PRINCIPAL + "/{id}", HttpMethod.DELETE, null,
				Void.class, idFuncionarioEquipeExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT); 
	}

	@Test
	@DisplayName("Deveria listar equipes pelo id de um funcionario")
	void listarEquipesCenario1() {

		Funcionario funcionario = criarFuncionario(TipoFuncao.COLETOR);

		popularEquipe(equipeTesteGoblal, funcionario);

		Long idFuncionarioExistente = funcionario.getId(); 

		ResponseEntity<FuncionarioDtoListagemEquipes> responseEntity = restTemplate.exchange(
				URI_PRINCIPAL + "/funcionario/{id}", HttpMethod.GET, null, FuncionarioDtoListagemEquipes.class,
				idFuncionarioExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody().id()).isEqualTo(idFuncionarioExistente);
		assertThat(responseEntity.getBody().equipes().get(0).equipe().id()).isEqualTo(equipeTesteGoblal.getId());
	}
	
	@Test
	@DisplayName("Deveria listar funcionarios pelo id de uma equipe")
	void listarFuncionariosEquipesCenario1() {

		Funcionario funcionario = criarFuncionario(TipoFuncao.COLETOR);

		popularEquipe(equipeTesteGoblal, funcionario);

		Long idEquipeExistente = equipeTesteGoblal.getId(); 

		ResponseEntity<FuncionarioDtoListagemEquipes> responseEntity = restTemplate.exchange(
				"/equipe/{id}", HttpMethod.GET, null, FuncionarioDtoListagemEquipes.class,
				idEquipeExistente);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody().id()).isEqualTo(idEquipeExistente);
	}

	
	static Stream<Arguments> parametrosCadastroInvalido() {
		return Stream.of(Arguments.of(15L, 1L,"Id do Funcionario não encontrado"),

				Arguments.of(1L, 15L,  "Id da Equipe não encontrado")

		);
	}

	static Stream<Arguments> parametrosFuncaoUnica() {
		return Stream.of(

				Arguments.of(TipoFuncao.MOTORISTA, "Já existe um motorista na equipe."),
				Arguments.of(TipoFuncao.SUPERVISOR, "Já existe um supervisor na equipe.")

		);
	}

	static Stream<Arguments> parametrosFuncao() {
		return Stream.of(

				Arguments.of(TipoFuncao.MOTORISTA), Arguments.of(TipoFuncao.SUPERVISOR),
				Arguments.of(TipoFuncao.COLETOR));
	}

	private Empresa criarEmpresa() {

		Empresa empresa = new Empresa(new EmpresaDtoCadastrar("empresa teste", "012345678912345", "400289222",
				"g@hotmail.com", TipoEmpresa.PUBLICA));

		empresaRepository.save(empresa);

		return empresa;
	}

	private Equipe criarEquipe() {

		Veiculo veiculoTeste = new Veiculo(TipoVeiculo.CAMINHAO_COMPACTADOR, 50D, "1234xx8", 1950, empresaTesteGoblal);

		veiculoRepository.save(veiculoTeste);

		return new Equipe("equipe teste", "descricao", veiculoTeste);

	}

	private Funcionario criarFuncionario(TipoFuncao funcao) {

		return new Funcionario("funcionario teste", "36133766622", LocalDate.of(1990, 5, 15), "fy1s7@gmail.com", funcao,
				empresaTesteGoblal);

	}

	private void popularEquipe(Equipe equipe, Funcionario funcionario) {

		FuncionarioEquipe funcionarioEquipeTeste = new FuncionarioEquipe(funcionario, equipe);

		funcionarioEquipeRepository.save(funcionarioEquipeTeste);

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

	private void popularEquipeComColetores(Equipe equipe) {

		var funcionarioTeste1 = new Funcionario("funcionario teste", "11133366621", LocalDate.of(1990, 5, 15),
				"fy1@gmail.com", TipoFuncao.COLETOR, empresaTesteGoblal);

		var funcionarioTeste2 = new Funcionario("funcionario teste", "21133366622", LocalDate.of(1990, 5, 15),
				"fy2@gmail.com", TipoFuncao.MOTORISTA, empresaTesteGoblal);

		var funcionarioTeste3 = new Funcionario("funcionario teste", "21133366623", LocalDate.of(1990, 5, 15),
				"fy3@gmail.com", TipoFuncao.COLETOR, empresaTesteGoblal);

		var funcionarioTeste4 = new Funcionario("funcionario teste", "31133366624", LocalDate.of(1990, 5, 15),
				"fy4@gmail.com", TipoFuncao.COLETOR_AUXILIAR, empresaTesteGoblal);

		var funcionarioTeste5 = new Funcionario("funcionario teste", "36133366625", LocalDate.of(1990, 5, 15),
				"fy5@gmail.com", TipoFuncao.COLETOR_AUXILIAR, empresaTesteGoblal);

		var funcionarioTeste6 = new Funcionario("funcionario teste", "21133366626", LocalDate.of(1990, 5, 15),
				"fy6@gmail.com", TipoFuncao.MOTORISTA, empresaTesteGoblal);

		List<FuncionarioEquipe> funcionarioEquipeList = new ArrayList<>();
		funcionarioEquipeList.add(new FuncionarioEquipe(funcionarioTeste1, equipe));
		funcionarioEquipeList.add(new FuncionarioEquipe(funcionarioTeste2, equipe));
		funcionarioEquipeList.add(new FuncionarioEquipe(funcionarioTeste3, equipe));
		funcionarioEquipeList.add(new FuncionarioEquipe(funcionarioTeste4, equipe));
		funcionarioEquipeList.add(new FuncionarioEquipe(funcionarioTeste5, equipe));
		funcionarioEquipeList.add(new FuncionarioEquipe(funcionarioTeste6, equipe));

		funcionarioEquipeRepository.saveAll(funcionarioEquipeList);

	} 

}
