package ec.edu.espe.zonas.services.impl;

import ec.edu.espe.zonas.dto.TicketCloseRequest;
import ec.edu.espe.zonas.dto.TicketCreateRequest;
import ec.edu.espe.zonas.dto.TicketResponse;
import ec.edu.espe.zonas.entidades.Espacio;
import ec.edu.espe.zonas.entidades.EstadoEspacio;
import ec.edu.espe.zonas.entidades.EstadoTicket;
import ec.edu.espe.zonas.entidades.Ticket;
import ec.edu.espe.zonas.entidades.Usuario;
import ec.edu.espe.zonas.entidades.VehiculoTicket;
import ec.edu.espe.zonas.repositorios.EspacioRepositorio;
import ec.edu.espe.zonas.repositorios.TicketRepositorio;
import ec.edu.espe.zonas.repositorios.UsuarioRepositorio;
import ec.edu.espe.zonas.repositorios.VehiculoTicketRepositorio;
import ec.edu.espe.zonas.services.TicketServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketServicioImpl implements TicketServicio {

    private final TicketRepositorio ticketRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final VehiculoTicketRepositorio vehiculoRepositorio;
    private final EspacioRepositorio espacioRepositorio;

    @Override
    @Transactional
    public TicketResponse crear(TicketCreateRequest request) {
        Usuario usuario = usuarioRepositorio.findById(request.getUserId())
                .filter(Usuario::isActive)
                .orElseThrow(() -> new RuntimeException("Usuario activo no encontrado"));

        VehiculoTicket vehiculo = vehiculoRepositorio.findById(request.getVehiculoId())
                .orElseThrow(() -> new RuntimeException("Vehiculo no encontrado"));

        Espacio espacio = espacioRepositorio.findById(request.getEspacioId())
                .orElseThrow(() -> new RuntimeException("Espacio no encontrado"));

        if (!espacio.isActivo() || espacio.getEstado() != EstadoEspacio.DISPONIBLE) {
            throw new IllegalStateException("El espacio no esta disponible");
        }

        ticketRepositorio.findByIdVehiculoAndEstado(vehiculo.getId(), EstadoTicket.ABIERTO)
                .ifPresent(ticket -> {
                    throw new IllegalStateException("Ya existe un ticket abierto para el vehiculo");
                });

        ticketRepositorio.findByIdEspacioAndEstado(espacio.getId(), EstadoTicket.ABIERTO)
                .ifPresent(ticket -> {
                    throw new IllegalStateException("Ya existe un ticket abierto para el espacio");
                });

        espacio.setActivo(false);
        espacio.setEstado(EstadoEspacio.OCUPADO);
        espacio.setFechaModificacion(LocalDateTime.now());
        espacioRepositorio.save(espacio);

        Ticket ticket = Ticket.builder()
                .idUser(usuario.getIdPerson())
                .idVehiculo(vehiculo.getId())
                .idEspacio(espacio.getId())
                .estado(EstadoTicket.ABIERTO)
                .total(BigDecimal.ZERO)
                .build();

        return toResponse(ticketRepositorio.save(ticket), usuario, vehiculo, espacio);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketResponse> listar() {
        return ticketRepositorio.findAllByOrderByFechaIngresoDesc().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TicketResponse obtenerPorId(UUID id) {
        return toResponse(buscarTicket(id));
    }

    @Override
    @Transactional
    public TicketResponse cerrar(UUID id, TicketCloseRequest request) {
        Ticket ticket = buscarTicket(id);
        if (ticket.getEstado() == EstadoTicket.CERRADO) {
            throw new IllegalStateException("El ticket ya esta cerrado");
        }

        Espacio espacio = espacioRepositorio.findById(ticket.getIdEspacio())
                .orElseThrow(() -> new RuntimeException("Espacio no encontrado"));

        ticket.setEstado(EstadoTicket.CERRADO);
        ticket.setFechaSalida(LocalDateTime.now());
        ticket.setTotal(request.getTotal() == null ? BigDecimal.ZERO : request.getTotal());

        espacio.setActivo(true);
        espacio.setEstado(EstadoEspacio.DISPONIBLE);
        espacio.setFechaModificacion(LocalDateTime.now());
        espacioRepositorio.save(espacio);

        return toResponse(ticketRepositorio.save(ticket));
    }

    private Ticket buscarTicket(UUID id) {
        return ticketRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));
    }

    private TicketResponse toResponse(Ticket ticket) {
        return toResponse(ticket, ticket.getUsuario(), ticket.getVehiculo(), ticket.getEspacio());
    }

    private TicketResponse toResponse(
            Ticket ticket,
            Usuario usuario,
            VehiculoTicket vehiculo,
            Espacio espacio) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .userId(ticket.getIdUser())
                .username(usuario == null ? null : usuario.getUsername())
                .vehiculoId(ticket.getIdVehiculo())
                .placaVehiculo(vehiculo == null ? null : vehiculo.getPlaca())
                .espacioId(ticket.getIdEspacio())
                .codigoEspacio(espacio == null ? null : espacio.getCodigo())
                .estado(ticket.getEstado())
                .fechaIngreso(ticket.getFechaIngreso())
                .fechaSalida(ticket.getFechaSalida())
                .total(ticket.getTotal())
                .createdAt(ticket.getCreatedAt())
                .updatedAt(ticket.getUpdatedAt())
                .build();
    }
}
