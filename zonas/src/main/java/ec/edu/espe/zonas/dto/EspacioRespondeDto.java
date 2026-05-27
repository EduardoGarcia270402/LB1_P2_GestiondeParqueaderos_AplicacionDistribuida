package ec.edu.espe.zonas.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import ec.edu.espe.zonas.entidades.Espacio;
import ec.edu.espe.zonas.entidades.TipoEspacio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EspacioRespondeDto {
    private UUID id;

    private String codigo;//nombre

    private String descripcion;

    private TipoEspacio tipo;

    private boolean estado;

    private UUID idZona;

    private List<Espacio> espacios;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaModificacion;
}
