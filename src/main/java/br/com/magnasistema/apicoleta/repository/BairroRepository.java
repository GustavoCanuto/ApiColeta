package br.com.magnasistema.apicoleta.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import br.com.magnasistema.apicoleta.entity.Bairro;
import jakarta.transaction.Transactional;

public interface BairroRepository extends JpaRepository<Bairro, Long> {

	Page<Bairro> findByCidadeId(Long id, Pageable paginacao);

	Page<Bairro> findByCidadeIdAndNomeContainingIgnoreCase(Long id, String nome, Pageable paginacao);

	Page<Bairro> findByNomeContainingIgnoreCase(String nome, Pageable paginacao);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM tb_bairro; ALTER SEQUENCE tb_bairro_id_seq RESTART WITH 1", nativeQuery = true)
	void deleteAllAndResetSequence();
}
