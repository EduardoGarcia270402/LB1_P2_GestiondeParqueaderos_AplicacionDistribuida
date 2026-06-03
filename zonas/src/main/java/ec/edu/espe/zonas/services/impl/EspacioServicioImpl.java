package ec.edu.espe.zonas.services.impl;

import ec.edu.espe.zonas.dto.EspacioRequestDto;
import ec.edu.espe.zonas.dto.EspacioRespondeDto;
import ec.edu.espe.zonas.entidades.Espacio;
import ec.edu.espe.zonas.entidades.EstadoEspacio;
import ec.edu.espe.zonas.entidades.Zona;
import ec.edu.espe.zonas.repositorios.EspacioRepositorio;
import ec.edu.espe.zonas.repositorios.ZonaRepositorio;
import ec.edu.espe.zonas.utils.UtilsMapers;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EspacioServicioImpl implements EspacioServicio {

    private final EspacioRepositorio espacioRepositorio;
    private final ZonaRepositorio zonaRepositorio;
    private final UtilsMapers mapper;

    @Override
    @Transactional(readOnly = true)
    public List<EspacioRespondeDto> obtenerEspacios() {
        return espacioRepositorio.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EspacioRespondeDto crearEspacio(EspacioRequestDto dto) {
        Zona objZona = zonaRepositorio.findById(dto.getIdZona())
                .orElseThrow(() -> new RuntimeException("Zona no encontrada con id: " + dto.getIdZona()));

        Espacio nuevoEspacio = mapper.toEntity(dto);
        nuevoEspacio.setZona(objZona);
        nuevoEspacio.setEstado(EstadoEspacio.DISPONIBLE);
        nuevoEspacio.setFechaCreacion(LocalDateTime.now());
        nuevoEspacio.setFechaModificacion(LocalDateTime.now());
        espacioRepositorio.save(nuevoEspacio);

        return mapper.toResponse(nuevoEspacio);
    }

    @Override
    public EspacioRespondeDto actualizarEspacio(EspacioRequestDto dto) {
        if (dto == null || dto.getCodigo() == null) {
            throw new IllegalArgumentException("El código del espacio es obligatorio para actualizar");
        }

        Optional<Espacio> opt = espacioRepositorio.findByCodigo(dto.getCodigo());
        Espacio existente = opt.orElseThrow(() -> new RuntimeException("Espacio no encontrado con codigo: " + dto.getCodigo()));

        if (dto.getIdZona() != null) {
            Zona zona = zonaRepositorio.findById(dto.getIdZona())
                    .orElseThrow(() -> new RuntimeException("Zona no encontrada con id: " + dto.getIdZona()));
            existente.setZona(zona);
        }

        existente.setDescripcion(dto.getDescripcion());
        existente.setTipo(dto.getTipo());
        existente.setEstado(dto.getEstado());
        existente.setFechaModificacion(LocalDateTime.now());

        espacioRepositorio.save(existente);
        return mapper.toResponse(existente);
    }

    @Override
    public void eliminarEspacio(UUID idEspacio) {
        Espacio esp = espacioRepositorio.findById(idEspacio)
                .orElseThrow(() -> new RuntimeException("Espacio no encontrado con id: " + idEspacio));
        espacioRepositorio.delete(esp);
    }

    @Override
    public EspacioRespondeDto cambiarEstado(EstadoEspacio estado) {
        List<Espacio> todos = espacioRepositorio.findAll();
        if (todos.isEmpty()) return null;

        LocalDateTime ahora = LocalDateTime.now();
        Espacio primero = null;
        for (Espacio e : todos) {
            if (e.getEstado() != estado) {
                e.setEstado(estado);
                e.setFechaModificacion(ahora);
                if (primero == null) primero = e;
            }
        }
        espacioRepositorio.saveAll(todos);
        return primero == null ? null : mapper.toResponse(primero);
    }

    @Override
    public List<EspacioRespondeDto> obtenerEspaciosPorEstado(EstadoEspacio estado) {
        return espacioRepositorio.findByEstado(estado).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EspacioRespondeDto> obtenerEspaciosPorZona(UUID idZona, EstadoEspacio estado) {
        Zona zona = zonaRepositorio.findById(idZona)
            .orElseThrow(() -> new RuntimeException("Zona no encontrada con id: " + idZona));

        return espacioRepositorio.findByZonaAndEstado(zona, estado).stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
    }
}
