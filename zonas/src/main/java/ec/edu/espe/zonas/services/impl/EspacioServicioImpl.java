package ec.edu.espe.zonas.services.impl;

import ec.edu.espe.zonas.dto.EspacioRequestDto;
import ec.edu.espe.zonas.dto.EspacioRespondeDto;
import ec.edu.espe.zonas.entidades.Espacio;
import ec.edu.espe.zonas.entidades.EstadoEspacio;
import ec.edu.espe.zonas.entidades.Zona;
import ec.edu.espe.zonas.repositorios.EspacioRepositorio;
import ec.edu.espe.zonas.repositorios.ZonaRepositorio;
import ec.edu.espe.zonas.services.EspacioServicio;
import ec.edu.espe.zonas.utils.UtilsMapers;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
                .orElseThrow(() ->
                        new RuntimeException(
                                "Zona no encontrada con id: " + dto.getIdZona()));

        Espacio nuevoEspacio = mapper.toEntity(dto);

        nuevoEspacio.setZona(objZona);
        nuevoEspacio.setEstado(EstadoEspacio.DISPONIBLE);
        nuevoEspacio.setFechaCreacion(LocalDateTime.now());
        nuevoEspacio.setFechaModificacion(LocalDateTime.now());

        espacioRepositorio.save(nuevoEspacio);

        return mapper.toResponse(nuevoEspacio);
    }

    @Override
    @Transactional
    public EspacioRespondeDto actualizarEspacio(EspacioRequestDto dto) {

        if (dto == null || dto.getCodigo() == null) {
            throw new IllegalArgumentException(
                    "El código del espacio es obligatorio para actualizar");
        }

        Optional<Espacio> opt = espacioRepositorio.findByCodigo(dto.getCodigo());

        Espacio existente = opt.orElseThrow(() ->
                new RuntimeException(
                        "Espacio no encontrado con codigo: " + dto.getCodigo()));

        if (dto.getIdZona() != null) {

            Zona zona = zonaRepositorio.findById(dto.getIdZona())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Zona no encontrada con id: " + dto.getIdZona()));

            existente.setZona(zona);
        }

        existente.setDescripcion(dto.getDescripcion());
        existente.setTipo(dto.getTipo());

        // VALIDAR ESTADO
        if (dto.getEstado() != null) {

            if (existente.getEstado() == dto.getEstado()) {
                throw new IllegalStateException(
                        "El espacio ya se encuentra en estado "
                                + dto.getEstado());
            }

            validarCambioEstado(existente.getEstado(), dto.getEstado());

            existente.setEstado(dto.getEstado());
        }

        existente.setFechaModificacion(LocalDateTime.now());

        espacioRepositorio.save(existente);

        return mapper.toResponse(existente);
    }

    @Override
    @Transactional
    public void eliminarEspacio(UUID idEspacio) {

        Espacio esp = espacioRepositorio.findById(idEspacio)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Espacio no encontrado con id: " + idEspacio));

        espacioRepositorio.delete(esp);
    }

    @Override
    @Transactional
    public EspacioRespondeDto cambiarEstado(
            UUID idEspacio,
            EstadoEspacio nuevoEstado) {

        Espacio espacio = espacioRepositorio.findById(idEspacio)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Espacio no encontrado con id: " + idEspacio));

        EstadoEspacio estadoActual = espacio.getEstado();

        // VALIDAR MISMO ESTADO
        if (estadoActual == nuevoEstado) {

            throw new IllegalStateException(
                    "No se puede cambiar el estado de "
                            + estadoActual
                            + " a "
                            + nuevoEstado);
        }

        // VALIDAR CAMBIO
        validarCambioEstado(estadoActual, nuevoEstado);

        // ACTUALIZAR
        espacio.setEstado(nuevoEstado);
        espacio.setFechaModificacion(LocalDateTime.now());

        espacioRepositorio.save(espacio);

        return mapper.toResponse(espacio);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EspacioRespondeDto> obtenerEspaciosPorEstado(
            EstadoEspacio estado) {

        return espacioRepositorio.findByEstado(estado).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EspacioRespondeDto> obtenerEspaciosPorZona(
            UUID idZona,
            EstadoEspacio estado) {

        Zona zona = zonaRepositorio.findById(idZona)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Zona no encontrada con id: " + idZona));

        return espacioRepositorio.findByZonaAndEstado(zona, estado).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    //Validaciones de estado
    private void validarCambioEstado(
            EstadoEspacio actual,
            EstadoEspacio nuevo) {

        // MISMO ESTADO
        if (actual == nuevo) {

            throw new IllegalStateException(
                    "El espacio ya se encuentra en estado " + actual);
        }

        // MANTENIMIENTO -> OCUPADO
        if (actual == EstadoEspacio.MANTENIMIENTO
                && nuevo == EstadoEspacio.OCUPADO) {

            throw new IllegalStateException(
                    "Un espacio en mantenimiento no puede pasar a ocupado");
        }

        // RESERVADO -> RESERVADO
        if (actual == EstadoEspacio.RESERVADO
                && nuevo == EstadoEspacio.RESERVADO) {

            throw new IllegalStateException(
                    "El espacio ya está reservado");
        }
    }
}