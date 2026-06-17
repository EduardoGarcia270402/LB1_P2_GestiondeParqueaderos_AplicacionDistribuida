package ec.edu.espe.zonas.utils;

import ec.edu.espe.zonas.dto.PersonaResponse;
import ec.edu.espe.zonas.dto.RolResponse;
import ec.edu.espe.zonas.dto.UsuarioResponse;
import ec.edu.espe.zonas.dto.UsuarioRolResponse;
import ec.edu.espe.zonas.entidades.Persona;
import ec.edu.espe.zonas.entidades.Rol;
import ec.edu.espe.zonas.entidades.Usuario;
import ec.edu.espe.zonas.entidades.UsuarioRol;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IdentidadMapper {

    public PersonaResponse toResponse(Persona persona) {
        return new PersonaResponse(
                persona.getId(),
                persona.getDni(),
                persona.getFirstName(),
                persona.getMiddleName(),
                persona.getLastName(),
                persona.getEmail(),
                persona.getPhone(),
                persona.getAddress(),
                persona.getNationality(),
                persona.isActive(),
                persona.getCreatedAt(),
                persona.getUpdatedAt());
    }

    public RolResponse toResponse(Rol rol) {
        return new RolResponse(
                rol.getId(),
                rol.getName(),
                rol.getDescription(),
                rol.isActive(),
                rol.getCreatedAt(),
                rol.getUpdatedAt());
    }

    public UsuarioResponse toResponse(Usuario usuario) {
        List<RolResponse> roles = usuario.getUsuarioRoles() == null
                ? List.of()
                : usuario.getUsuarioRoles().stream()
                        .filter(UsuarioRol::isActive)
                        .map(UsuarioRol::getRol)
                        .map(this::toResponse)
                        .toList();

        return new UsuarioResponse(
                usuario.getIdPerson(),
                usuario.getUsername(),
                usuario.isActive(),
                usuario.getLastLogin(),
                usuario.getCreatedAt(),
                usuario.getUpdatedAt(),
                toResponse(usuario.getPersona()),
                roles);
    }

    public UsuarioRolResponse toResponse(UsuarioRol usuarioRol) {
        return new UsuarioRolResponse(
                usuarioRol.getId().getIdUser(),
                usuarioRol.getId().getIdRole(),
                usuarioRol.getRol().getName(),
                usuarioRol.isActive(),
                usuarioRol.getAssignedAt());
    }
}
