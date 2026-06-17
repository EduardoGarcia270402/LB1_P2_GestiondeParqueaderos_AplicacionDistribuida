package ec.edu.espe.zonas.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class TicketCreateRequest {

    @NotNull(message = "El usuario es obligatorio")
    private UUID userId;

    @NotNull(message = "El vehiculo es obligatorio")
    private UUID vehiculoId;

    @NotNull(message = "El espacio es obligatorio")
    private UUID espacioId;
}
