package ec.edu.espe.zonas.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.edu.espe.zonas.dto.ZonaRequestDto;
import ec.edu.espe.zonas.dto.ZonaRespondeDto;
import ec.edu.espe.zonas.repositorios.ZonaRepositorio;
import ec.edu.espe.zonas.services.ZonaServicio;
import ec.edu.espe.zonas.entidades.Zona;

@Service
public class ZonaServicioImpl implements ZonaServicio {

    @Autowired
    private ZonaRepositorio repositorioZona;

    @Override
    public List<ZonaRespondeDto> ListarZonas() {
        return repositorioZona.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public ZonaRespondeDto crearZona(ZonaRequestDto request) {
        if (repositorioZona.existsByNombre(request.getNombre())) {
            throw new RuntimeException("Ya existe una zona con el nombre: " + request.getNombre());
        }

        long totalZonas = repositorioZona.count();

        Zona objZona = new Zona();
        objZona.setNombre(request.getNombre());
        objZona.setDescripcion(request.getDescripcion());
        objZona.setTipo(request.getTipo());
        objZona.setEstado(1); // 1 = activo
        objZona.setCapacidad(request.getCapacidad());
        objZona.setFechaCreacion(LocalDateTime.now());
        objZona.setFechaModificacion(LocalDateTime.now());
        objZona.setCodigo(generarCodigo(request, totalZonas + 1));

        repositorioZona.save(objZona);
        return mapToDto(objZona);
    }

    @Override
    public ZonaRespondeDto actualizarZona(UUID idZona, ZonaRequestDto request) {
        Zona zona = repositorioZona.findById(idZona)
                .orElseThrow(() -> new RuntimeException("Zona no encontrada con id: " + idZona));

        if (!zona.getNombre().equals(request.getNombre())
                && repositorioZona.existsByNombre(request.getNombre())) {
            throw new RuntimeException("Ya existe una zona con el nombre: " + request.getNombre());
        }

        zona.setNombre(request.getNombre());
        zona.setDescripcion(request.getDescripcion());
        zona.setTipo(request.getTipo());
        zona.setCapacidad(request.getCapacidad());
        zona.setFechaModificacion(LocalDateTime.now());

        repositorioZona.save(zona);
        return mapToDto(zona);
    }

    @Override
    public void desactivarZona(UUID idZona) {
        Zona zona = repositorioZona.findById(idZona)
                .orElseThrow(() -> new RuntimeException("Zona no encontrada con id: " + idZona));

        zona.setEstado(0); // 0 = inactivo
        zona.setFechaModificacion(LocalDateTime.now());
        repositorioZona.save(zona);
    }

    // ─────────────── Métodos privados ───────────────

    private ZonaRespondeDto mapToDto(Zona objZona) {
        return ZonaRespondeDto.builder()
                .id(objZona.getId())
                .nombre(objZona.getNombre())
                .codigo(objZona.getCodigo())
                .descripcion(objZona.getDescripcion())
                .estado(objZona.getEstado())
                .tipo(objZona.getTipo())
                .capacidad(objZona.getCapacidad())
                .fechaCreacion(objZona.getFechaCreacion())
                .fechaModificacion(objZona.getFechaModificacion())
                .build();
    }

    /**
     * Genera un código con el formato: ZONA-<TIPO_ABREV>-<NUMERO>
     * Ejemplo: ZONA-REG-01, ZONA-VIP-02, ZONA-INT-03
     * La primera zona insertada en una BDD vacía será ZONA-REG-01 (o el tipo correspondiente).
     */
    private String generarCodigo(ZonaRequestDto request, long numero) {
        String tipoAbrev = (request.getTipo() != null)
                ? request.getTipo().name().substring(0, Math.min(3, request.getTipo().name().length()))
                : "GEN";
        return String.format("ZONA-%s-%02d", tipoAbrev.toUpperCase(), numero);
    }
}
