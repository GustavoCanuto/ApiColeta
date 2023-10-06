package br.com.magnasistema.apicoleta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import br.com.magnasistema.apicoleta.entity.Destino;
import jakarta.transaction.Transactional;

public interface DestinoRepository extends JpaRepository<Destino, Long>{


	@Transactional
	@Modifying
	@Query(value = "DELETE FROM tb_destino; ALTER SEQUENCE tb_destino_id_seq RESTART WITH 1", nativeQuery = true)
	void deleteAllAndResetSequence();

}
