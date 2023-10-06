package br.com.magnasistema.apicoleta.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import br.com.magnasistema.apicoleta.entity.Cidade;
import jakarta.transaction.Transactional;

public interface CidadeRepository extends JpaRepository<Cidade, Long> {
	
	Page<Cidade> findByEstadoId(Long estadoId, Pageable pageable);

	Page<Cidade> findByEstadoIdAndNomeContainingIgnoreCase(Long id, String nome, Pageable paginacao);

	Page<Cidade> findByNomeContainingIgnoreCase(String nome, Pageable paginacao);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM tb_cidade; ALTER SEQUENCE tb_cidade_id_seq RESTART WITH 1", nativeQuery = true)
	void deleteAllAndResetSequence();

}
