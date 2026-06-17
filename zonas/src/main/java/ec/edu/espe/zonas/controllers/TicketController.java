package ec.edu.espe.zonas.controllers;

import ec.edu.espe.zonas.dto.TicketCloseRequest;
import ec.edu.espe.zonas.dto.TicketCreateRequest;
import ec.edu.espe.zonas.dto.TicketResponse;
import ec.edu.espe.zonas.services.TicketServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketServicio ticketServicio;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TicketResponse crear(@Valid @RequestBody TicketCreateRequest request) {
        return ticketServicio.crear(request);
    }

    @GetMapping
    public List<TicketResponse> listar() {
        return ticketServicio.listar();
    }

    @GetMapping("/{id}")
    public TicketResponse obtenerPorId(@PathVariable UUID id) {
        return ticketServicio.obtenerPorId(id);
    }

    @PatchMapping("/{id}/cerrar")
    public TicketResponse cerrar(
            @PathVariable UUID id,
            @Valid @RequestBody TicketCloseRequest request) {
        return ticketServicio.cerrar(id, request);
    }
}
