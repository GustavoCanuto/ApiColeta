package br.com.magnasistema.apicoleta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import br.com.magnasistema.apicoleta.entity.Estado;
import jakarta.transaction.Transactional;

public interface EstadoRepository extends JpaRepository<Estado, Long> {

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM tb_estado; ALTER SEQUENCE tb_estado_id_seq RESTART WITH 1", nativeQuery = true)
	void deleteAllAndResetSequence();


		
}
