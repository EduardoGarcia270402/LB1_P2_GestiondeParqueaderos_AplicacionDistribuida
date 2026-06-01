package ec.edu.espe.zonas.repositorios;

import ec.edu.espe.zonas.entidades.Espacio;
import ec.edu.espe.zonas.entidades.Zona;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EspacioRepositorio extends JpaRepository<Espacio, UUID> {

    boolean existsByCodigo(String codigo);

    List<Espacio> findByZona(Zona zona);

    List<Espacio> findByZonaAndEstado(Zona zona, boolean estado);

    List<Espacio> findByEstado(boolean estado);
}