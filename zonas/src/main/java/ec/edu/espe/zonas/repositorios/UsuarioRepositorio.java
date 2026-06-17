package ec.edu.espe.zonas.repositorios;

import ec.edu.espe.zonas.entidades.Usuario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepositorio extends JpaRepository<Usuario, UUID> {

    boolean existsByUsername(String username);

    List<Usuario> findByUsernameStartingWith(String usernameBase);

    @Override
    @EntityGraph(attributePaths = {"persona", "usuarioRoles", "usuarioRoles.rol"})
    Optional<Usuario> findById(UUID id);

    @Override
    @EntityGraph(attributePaths = {"persona", "usuarioRoles", "usuarioRoles.rol"})
    List<Usuario> findAll();
}
