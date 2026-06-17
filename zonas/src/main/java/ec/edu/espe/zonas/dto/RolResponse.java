package ec.edu.espe.zonas.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record RolResponse(
        UUID id,
        String name,
        String description,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
