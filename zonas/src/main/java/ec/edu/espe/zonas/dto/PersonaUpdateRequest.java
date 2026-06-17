package ec.edu.espe.zonas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PersonaUpdateRequest {

    private static final String NAME_PATTERN =
            "^[A-Za-zÁÉÍÓÚáéíóúÑñ]+(?: [A-Za-zÁÉÍÓÚáéíóúÑñ]+)*$";

    @Pattern(regexp = "\\d{10,13}", message = "El DNI debe contener entre 10 y 13 digitos")
    private String dni;

    @Size(min = 2, max = 30)
    @Pattern(
            regexp = NAME_PATTERN,
            message = "El primer nombre solo debe contener letras y espacios simples")
    private String firstName;

    @Size(max = 30)
    @Pattern(
            regexp = NAME_PATTERN,
            message = "El segundo nombre solo debe contener letras y espacios simples")
    private String middleName;

    @Size(min = 2, max = 30)
    @Pattern(
            regexp = NAME_PATTERN,
            message = "El apellido solo debe contener letras y espacios simples")
    private String lastName;

    @Email
    @Size(max = 50)
    private String email;

    @Pattern(
            regexp = "09\\d{8}",
            message = "El telefono debe ser un celular ecuatoriano de 10 digitos")
    private String phone;

    private String address;

    @Size(max = 30)
    @Pattern(
            regexp = NAME_PATTERN,
            message = "La nacionalidad solo debe contener letras y espacios simples")
    private String nationality;
}
