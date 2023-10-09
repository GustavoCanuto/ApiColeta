package br.com.magnasistema.apicoleta.dto.bairro;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BairroDtoCadastro(
		
        @NotNull
        Long idCidade,
               
        @NotBlank
        @Size(max = 255)
        String nome,
        
        @Size(max = 255)
        String logradouros
		
		
		) {

}
