package ec.edu.espe.zonas.entidades;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "espacios")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Espacio {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false, length = 12)//EJEMPLO: ESPACIO-1A-ZONA-A1
    private String codigo;//nombre

    @Column(length = 12)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEspacio tipo;

    @Column(nullable = false)
    private boolean estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id.zona")
    private Zona zona;

    @OneToMany(mappedBy = "zona", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Espacio> espacios;

    @Column
    private LocalDateTime fechaCreacion;

    @Column
    private LocalDateTime fechaModificacion;
}
