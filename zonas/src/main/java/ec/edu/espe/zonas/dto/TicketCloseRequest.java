package ec.edu.espe.zonas.dto;

import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TicketCloseRequest {

    @DecimalMin(value = "0.00", message = "El total no puede ser negativo")
    private BigDecimal total;
}
