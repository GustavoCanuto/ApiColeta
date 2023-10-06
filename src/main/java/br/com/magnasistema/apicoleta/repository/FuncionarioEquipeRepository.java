package br.com.magnasistema.apicoleta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import br.com.magnasistema.apicoleta.entity.FuncionarioEquipe;
import jakarta.transaction.Transactional;

public interface FuncionarioEquipeRepository extends JpaRepository<FuncionarioEquipe, Long> {

	List<FuncionarioEquipe>  findByEquipeId(Long id);

	List<FuncionarioEquipe> findByFuncionarioId(long id);

	boolean existsByFuncionarioIdAndEquipeId(Long idFuncionario, Long idEquipe);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM tb_funcionario_equipe; ALTER SEQUENCE tb_funcionario_equipe_id_seq RESTART WITH 1", nativeQuery = true)
	void deleteAllAndResetSequence();


}