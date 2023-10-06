package br.com.magnasistema.apicoleta.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import br.com.magnasistema.apicoleta.entity.Empresa;
import jakarta.transaction.Transactional;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

	Page<Empresa> findByNomeContainingIgnoreCase(String nome, Pageable paginacao);

	boolean existsByEmail(String email);

	boolean existsByCnpj(String cnpj);


	@Transactional
	@Modifying
	@Query(value = "DELETE FROM tb_empresa; ALTER SEQUENCE tb_empresa_id_seq RESTART WITH 1", nativeQuery = true)
	void deleteAllAndResetSequence();

}
