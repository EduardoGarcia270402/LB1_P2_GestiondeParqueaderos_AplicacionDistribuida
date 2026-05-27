package ec.edu.espe.zonas.services;

import java.util.List;
import java.util.UUID;

import ec.edu.espe.zonas.dto.ZonaRequestDto;
import ec.edu.espe.zonas.dto.ZonaRespondeDto;

public interface ZonaServicio {
    List<ZonaRespondeDto> ListarZonas();

    ZonaRespondeDto crearZona(ZonaRequestDto request);

    ZonaRequestDto actualizarZona(UUID idZona, ZonaRequestDto request);

    void desactivarZona(UUID idZona);
}
