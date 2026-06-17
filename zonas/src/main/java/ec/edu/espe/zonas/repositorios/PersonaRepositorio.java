package ec.edu.espe.zonas.repositorios;

import ec.edu.espe.zonas.entidades.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PersonaRepositorio extends JpaRepository<Persona, UUID> {

    boolean existsByDni(String dni);

    boolean existsByDniAndIdNot(String dni, UUID id);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, UUID id);

    boolean existsByPhone(String phone);

    boolean existsByPhoneAndIdNot(String phone, UUID id);
}
