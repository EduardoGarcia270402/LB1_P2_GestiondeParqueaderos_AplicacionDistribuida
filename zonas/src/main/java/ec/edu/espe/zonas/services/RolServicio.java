package ec.edu.espe.zonas.services;

import ec.edu.espe.zonas.dto.RolCreateRequest;
import ec.edu.espe.zonas.dto.RolResponse;
import ec.edu.espe.zonas.dto.UsuarioRolResponse;
import ec.edu.espe.zonas.entidades.Rol;
import ec.edu.espe.zonas.entidades.Usuario;
import ec.edu.espe.zonas.entidades.UsuarioRol;
import ec.edu.espe.zonas.entidades.UsuarioRolId;
import ec.edu.espe.zonas.repositorios.RolRepositorio;
import ec.edu.espe.zonas.repositorios.UsuarioRepositorio;
import ec.edu.espe.zonas.repositorios.UsuarioRolRepositorio;
import ec.edu.espe.zonas.utils.IdentidadMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RolServicio {

    private final RolRepositorio rolRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final UsuarioRolRepositorio usuarioRolRepositorio;
    private final IdentidadMapper mapper;

    @Transactional
    public RolResponse crear(RolCreateRequest request) {
        if (rolRepositorio.findByName(request.getName()).isPresent()) {
            throw new IllegalStateException("El rol ya existe");
        }

        Rol rol = Rol.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        return mapper.toResponse(rolRepositorio.save(rol));
    }

    @Transactional(readOnly = true)
    public List<RolResponse> listar() {
        return rolRepositorio.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public UsuarioRolResponse asignar(UUID userId, UUID roleId) {
        Usuario usuario = usuarioRepositorio.findById(userId)
                .filter(Usuario::isActive)
                .orElseThrow(() -> new RuntimeException("Usuario activo no encontrado"));
        Rol rol = rolRepositorio.findById(roleId)
                .filter(Rol::isActive)
                .orElseThrow(() -> new RuntimeException("Rol activo no encontrado"));
        UsuarioRolId id = new UsuarioRolId(userId, roleId);

        if (usuarioRolRepositorio.existsById(id)) {
            throw new IllegalStateException("El usuario ya tiene asignado este rol");
        }

        UsuarioRol asignacion = UsuarioRol.builder()
                .id(id)
                .usuario(usuario)
                .rol(rol)
                .build();
        return mapper.toResponse(usuarioRolRepositorio.save(asignacion));
    }

    @Transactional(readOnly = true)
    public List<RolResponse> listarPorUsuario(UUID userId) {
        if (!usuarioRepositorio.existsById(userId)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        return usuarioRolRepositorio.findByIdIdUserAndActiveTrue(userId).stream()
                .map(UsuarioRol::getRol)
                .map(mapper::toResponse)
                .toList();
    }
}
