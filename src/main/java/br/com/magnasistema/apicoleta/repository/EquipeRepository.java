package br.com.magnasistema.apicoleta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import br.com.magnasistema.apicoleta.entity.Equipe;
import jakarta.transaction.Transactional;

public interface EquipeRepository extends JpaRepository<Equipe, Long> {

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM tb_equipe; ALTER SEQUENCE tb_equipe_id_seq RESTART WITH 1", nativeQuery = true)
	void deleteAllAndResetSequence();

	

}
