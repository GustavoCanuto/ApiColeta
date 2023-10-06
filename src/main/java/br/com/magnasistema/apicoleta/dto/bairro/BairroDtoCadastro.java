package br.com.magnasistema.apicoleta.dto.bairro;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BairroDtoCadastro(
		
        @NotNull
        Long idCidade,
               
        @NotNull
        @NotBlank
        String nome,
        
        String logradouros
		
		
		) {

}
