package ec.edu.espe.zonas.controllers;

import ec.edu.espe.zonas.dto.EspacioRequestDto;
import ec.edu.espe.zonas.dto.EspacioRespondeDto;
import ec.edu.espe.zonas.entidades.EstadoEspacio;
import ec.edu.espe.zonas.services.EspacioServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/espacios")
@RequiredArgsConstructor
public class EspacioController {

    private final EspacioServicio espacioServicio;

    @GetMapping
    public List<EspacioRespondeDto> listar(
            @RequestParam(required = false) EstadoEspacio estado,
            @RequestParam(required = false) UUID zonaId) {
        if (zonaId != null && estado != null) {
            return espacioServicio.obtenerEspaciosPorZona(zonaId, estado);
        }
        if (estado != null) {
            return espacioServicio.obtenerEspaciosPorEstado(estado);
        }
        return espacioServicio.obtenerEspacios();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EspacioRespondeDto crear(@Valid @RequestBody EspacioRequestDto request) {
        return espacioServicio.crearEspacio(request);
    }

    @PutMapping
    public EspacioRespondeDto actualizar(@Valid @RequestBody EspacioRequestDto request) {
        return espacioServicio.actualizarEspacio(request);
    }

    @PatchMapping("/{id}/estado")
    public EspacioRespondeDto cambiarEstado(
            @PathVariable UUID id,
            @RequestParam EstadoEspacio estado) {
        return espacioServicio.cambiarEstado(id, estado);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable UUID id) {
        espacioServicio.eliminarEspacio(id);
    }
}
