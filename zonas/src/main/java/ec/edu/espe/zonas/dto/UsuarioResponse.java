package ec.edu.espe.zonas.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record UsuarioResponse(
        UUID idPerson,
        String username,
        boolean active,
        LocalDateTime lastLogin,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        PersonaResponse persona,
        List<RolResponse> roles) {
}
