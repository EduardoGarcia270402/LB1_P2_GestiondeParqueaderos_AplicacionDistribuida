package ec.edu.espe.zonas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PersonaCreateRequest {

    @NotBlank
    @Pattern(regexp = "\\d{10,13}", message = "El DNI debe contener entre 10 y 13 digitos")
    private String dni;

    @NotBlank
    @Size(min = 2, max = 30)
    private String firstName;

    @Size(max = 30)
    private String middleName;

    @NotBlank
    @Size(min = 2, max = 30)
    private String lastName;

    @NotBlank
    @Email
    @Size(max = 50)
    private String email;

    @NotBlank
    @Pattern(regexp = "\\d{7,15}", message = "El telefono debe contener entre 7 y 15 digitos")
    private String phone;

    private String address;

    @Size(max = 30)
    private String nationality;
}
