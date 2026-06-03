package ec.edu.espe.zonas.services.impl;

import java.util.List;
import java.util.UUID;

import ec.edu.espe.zonas.dto.EspacioRequestDto;
import ec.edu.espe.zonas.dto.EspacioRespondeDto;

public interface EstadoServicio {

    List<EspacioRespondeDto> obtenerEstados();

    EspacioRequestDto crearEspacio(EspacioRequestDto dto);

    EspacioRequestDto actualizarEspacio(EspacioRequestDto dto);

    void eliminarEspacio(UUID idEspacio);
}
