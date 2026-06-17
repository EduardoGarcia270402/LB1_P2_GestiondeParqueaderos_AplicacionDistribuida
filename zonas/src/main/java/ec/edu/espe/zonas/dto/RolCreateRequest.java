package ec.edu.espe.zonas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RolCreateRequest {

    @NotBlank
    @Pattern(regexp = "CLIENTE|OPERADOR", message = "El rol debe ser CLIENTE u OPERADOR")
    private String name;

    @Size(max = 255)
    private String description;
}
