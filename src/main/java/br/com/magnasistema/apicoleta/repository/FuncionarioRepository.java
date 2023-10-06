package br.com.magnasistema.apicoleta.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import br.com.magnasistema.apicoleta.entity.Funcionario;
import br.com.magnasistema.apicoleta.enums.TipoFuncao;
import jakarta.transaction.Transactional;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long>{

	Page<Funcionario> findByFuncao(TipoFuncao funcao, Pageable paginacao);

	Page<Funcionario> findByEmpresaIdAndFuncao(long idEmpresa, TipoFuncao funcao, Pageable paginacao);

	Page<Funcionario> findByEmpresaId(long idEmpresa, Pageable paginacao);

	boolean existsByCpf(String cpf);

	boolean existsByEmail(String email);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM tb_funcionario; ALTER SEQUENCE tb_funcionario_id_seq RESTART WITH 1", nativeQuery = true)
	void deleteAllAndResetSequence();

	

}
