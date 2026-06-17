package ec.edu.espe.zonas.controllers;

import ec.edu.espe.zonas.dto.RolResponse;
import ec.edu.espe.zonas.dto.UsuarioCreateRequest;
import ec.edu.espe.zonas.dto.UsuarioResponse;
import ec.edu.espe.zonas.dto.UsuarioRolResponse;
import ec.edu.espe.zonas.services.RolServicio;
import ec.edu.espe.zonas.services.UsuarioServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioServicio usuarioServicio;
    private final RolServicio rolServicio;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse crear(@Valid @RequestBody UsuarioCreateRequest request) {
        return usuarioServicio.crear(request);
    }

    @GetMapping
    public List<UsuarioResponse> listar() {
        return usuarioServicio.listar();
    }

    @GetMapping("/{id}")
    public UsuarioResponse obtener(@PathVariable UUID id) {
        return usuarioServicio.obtener(id);
    }

    @PostMapping("/{userId}/roles/{roleId}")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioRolResponse asignarRol(
            @PathVariable UUID userId,
            @PathVariable UUID roleId) {
        return rolServicio.asignar(userId, roleId);
    }

    @GetMapping("/{userId}/roles")
    public List<RolResponse> listarRoles(@PathVariable UUID userId) {
        return rolServicio.listarPorUsuario(userId);
    }
}
