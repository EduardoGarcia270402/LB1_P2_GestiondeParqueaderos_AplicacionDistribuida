package ec.edu.espe.zonas.repositorios;

import ec.edu.espe.zonas.entidades.Espacio;
import ec.edu.espe.zonas.entidades.Zona;
import ec.edu.espe.zonas.entidades.EstadoEspacio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EspacioRepositorio extends JpaRepository<Espacio, UUID> {

    boolean existsByCodigo(String codigo);

    java.util.Optional<Espacio> findByCodigo(String codigo);

    List<Espacio> findByZona(Zona zona);

    List<Espacio> findByZonaAndEstado(Zona zona, EstadoEspacio estado);

    List<Espacio> findByEstado(EstadoEspacio estado);
}