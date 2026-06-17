package ec.edu.espe.zonas.repositorios;

import ec.edu.espe.zonas.entidades.UsuarioRol;
import ec.edu.espe.zonas.entidades.UsuarioRolId;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UsuarioRolRepositorio extends JpaRepository<UsuarioRol, UsuarioRolId> {

    @EntityGraph(attributePaths = "rol")
    List<UsuarioRol> findByIdIdUserAndActiveTrue(UUID idUser);
}
