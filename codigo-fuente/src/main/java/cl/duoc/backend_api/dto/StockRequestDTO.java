package cl.duoc.backend_api.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

@Data
public class StockRequestDTO {

    @NotEmpty(message = "La lista de productos no puede estar vacía")
    private List<ItemDescuentoDTO> items;
}