package ec.edu.espe.zonas.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UsuarioRolResponse(
        UUID idUser,
        UUID idRole,
        String roleName,
        boolean active,
        LocalDateTime assignedAt) {
}
