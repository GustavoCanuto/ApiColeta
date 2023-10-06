package br.com.magnasistema.apicoleta.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import br.com.magnasistema.apicoleta.entity.Veiculo;
import br.com.magnasistema.apicoleta.enums.TipoVeiculo;
import jakarta.transaction.Transactional;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

	Page<Veiculo> findByCapacidadeGreaterThanAndTipo(double capacidade, TipoVeiculo tipo, Pageable paginacao);

	Page<Veiculo> findByCapacidadeGreaterThan(double capacidade, Pageable paginacao);

	Page<Veiculo> findByTipo(TipoVeiculo tipo, Pageable paginacao);

	Page<Veiculo> findByEmpresaIdAndCapacidadeGreaterThanAndTipo(long idEmpresa, double capacidade, TipoVeiculo tipo,
			Pageable paginacao);

	Page<Veiculo> findByEmpresaIdAndCapacidadeGreaterThan(long idEmpresa, double capacidade, Pageable paginacao);

	Page<Veiculo> findByEmpresaIdAndTipo(long idEmpresa, TipoVeiculo tipo, Pageable paginacao);

	Page<Veiculo> findByEmpresaId(long idEmpresa, Pageable paginacao);

	boolean existsByPlaca(String placa);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM tb_veiculo; ALTER SEQUENCE tb_veiculo_id_seq RESTART WITH 1", nativeQuery = true)
	void deleteAllAndResetSequence();

}
