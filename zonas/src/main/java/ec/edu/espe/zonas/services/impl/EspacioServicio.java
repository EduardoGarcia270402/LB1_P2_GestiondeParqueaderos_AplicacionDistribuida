package ec.edu.espe.zonas.services.impl;

import ec.edu.espe.zonas.dto.EspacioRequestDto;
import ec.edu.espe.zonas.dto.EspacioRespondeDto;
import ec.edu.espe.zonas.entidades.EstadoEspacio;

import java.util.List;
import java.util.UUID;

public interface EspacioServicio {

    List<EspacioRespondeDto> obtenerEspacios();

    EspacioRespondeDto crearEspacio(EspacioRequestDto dto);

    EspacioRespondeDto actualizarEspacio(EspacioRequestDto dto);

    void eliminarEspacio(UUID idEspacio);

    EspacioRespondeDto cambiarEstado(EstadoEspacio estado);

    List<EspacioRespondeDto> obtenerEspaciosPorEstado(EstadoEspacio estado);

    List<EspacioRespondeDto> obtenerEspaciosPorZona(UUID idZona, EstadoEspacio estado);
}
