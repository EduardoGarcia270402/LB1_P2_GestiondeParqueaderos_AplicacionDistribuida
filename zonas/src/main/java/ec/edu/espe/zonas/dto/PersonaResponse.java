package ec.edu.espe.zonas.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record PersonaResponse(
        UUID id,
        String dni,
        String firstName,
        String middleName,
        String lastName,
        String email,
        String phone,
        String address,
        String nationality,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
