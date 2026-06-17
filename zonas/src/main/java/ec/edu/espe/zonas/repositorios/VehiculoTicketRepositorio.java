package ec.edu.espe.zonas.repositorios;

import ec.edu.espe.zonas.entidades.VehiculoTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VehiculoTicketRepositorio extends JpaRepository<VehiculoTicket, UUID> {
}
