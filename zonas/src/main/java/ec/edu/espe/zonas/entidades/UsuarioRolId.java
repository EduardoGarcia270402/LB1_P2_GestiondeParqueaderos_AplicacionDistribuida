package ec.edu.espe.zonas.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRolId implements Serializable {

    @Column(name = "id_user")
    private UUID idUser;

    @Column(name = "id_role")
    private UUID idRole;
}
