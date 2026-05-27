package ec.edu.espe.zonas.repositorios;

import ec.edu.espe.zonas.entidades.Espacio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EspacioRepositorio extends JpaRepository<Espacio, UUID> {
    boolean existsByCodigo(String codigo);

    List<Espacio> findByZona(UUID idZona);

    List<Espacio> findByZonaAndEstado(UUID idZona, int estado);

    List<Espacio> findByEstado(boolean estado);
}
