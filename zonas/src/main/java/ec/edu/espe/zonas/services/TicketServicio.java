package ec.edu.espe.zonas.services;

import ec.edu.espe.zonas.dto.TicketCloseRequest;
import ec.edu.espe.zonas.dto.TicketCreateRequest;
import ec.edu.espe.zonas.dto.TicketResponse;

import java.util.List;
import java.util.UUID;

public interface TicketServicio {

    TicketResponse crear(TicketCreateRequest request);

    List<TicketResponse> listar();

    TicketResponse obtenerPorId(UUID id);

    TicketResponse cerrar(UUID id, TicketCloseRequest request);
}
