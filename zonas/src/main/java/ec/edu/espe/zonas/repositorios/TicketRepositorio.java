package ec.edu.espe.zonas.repositorios;

import ec.edu.espe.zonas.entidades.EstadoTicket;
import ec.edu.espe.zonas.entidades.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketRepositorio extends JpaRepository<Ticket, UUID> {

    Optional<Ticket> findByIdVehiculoAndEstado(UUID idVehiculo, EstadoTicket estado);

    Optional<Ticket> findByIdEspacioAndEstado(UUID idEspacio, EstadoTicket estado);

    List<Ticket> findAllByOrderByFechaIngresoDesc();
}
