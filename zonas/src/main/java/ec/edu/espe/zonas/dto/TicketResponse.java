package ec.edu.espe.zonas.dto;

import ec.edu.espe.zonas.entidades.EstadoTicket;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class TicketResponse {

    private UUID id;
    private UUID userId;
    private String username;
    private UUID vehiculoId;
    private String placaVehiculo;
    private UUID espacioId;
    private String codigoEspacio;
    private EstadoTicket estado;
    private LocalDateTime fechaIngreso;
    private LocalDateTime fechaSalida;
    private BigDecimal total;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
