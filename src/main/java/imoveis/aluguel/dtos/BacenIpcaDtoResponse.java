package imoveis.aluguel.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BacenIpcaDtoResponse {

    @JsonProperty("data")
    private String data;

    @JsonProperty("valor")
    private String valor;

    public Double getValorAsDouble() {
        if (valor == null || valor.isEmpty()) {
            return null;
        }
        return Double.parseDouble(valor);
    }

}
