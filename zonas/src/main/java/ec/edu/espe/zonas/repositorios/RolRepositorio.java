package ec.edu.espe.zonas.repositorios;

import ec.edu.espe.zonas.entidades.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RolRepositorio extends JpaRepository<Rol, UUID> {

    Optional<Rol> findByName(String name);
}
