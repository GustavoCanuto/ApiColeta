package br.com.magnasistema.apicoleta.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import br.com.magnasistema.apicoleta.entity.Coleta;
import jakarta.transaction.Transactional;

public interface ColetaRepository extends JpaRepository<Coleta, Long> {

	boolean existsByEquipeIdAndTimestampInicialOrTimestampFinal(Long equipeId, LocalDateTime timestampInicial,
			LocalDateTime timestampFinal);

	Coleta findTopByEquipeIdOrderByIdDesc(Long idEquipe);

	Page<Coleta> findByEquipeIdOrderByIdDesc(Long id, Pageable paginacao);

	@Transactional
	@Modifying
	@Query(value = "ALTER SEQUENCE tb_coleta_id_seq RESTART WITH 1; DELETE FROM tb_coleta", nativeQuery = true)
	void deleteAllAndResetSequence();

}
