package br.com.magnasistema.apicoleta.dto.empresa;

import br.com.magnasistema.apicoleta.entity.Empresa;
import br.com.magnasistema.apicoleta.enums.TipoEmpresa;

public record EmpresaDtoDetalhar (
		long id,
		
		String nome,
		
		
		String cnpj,
	
		
		String telefone,

		
		String email,
	
		TipoEmpresa tipo
		)

{

	public EmpresaDtoDetalhar(Empresa empresa) {
		this(empresa.getId(), empresa.getNome(),empresa.getCnpj(),empresa.getTelefone(),empresa.getEmail(),empresa.getTipo());

	}

}
