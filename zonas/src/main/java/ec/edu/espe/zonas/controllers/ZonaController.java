package ec.edu.espe.zonas.controllers;

import ec.edu.espe.zonas.dto.ZonaRequestDto;
import ec.edu.espe.zonas.dto.ZonaRespondeDto;
import ec.edu.espe.zonas.services.ZonaServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/zonas")
@RequiredArgsConstructor
public class ZonaController {

    private final ZonaServicio zonaServicio;

    @GetMapping
    public List<ZonaRespondeDto> listar() {
        return zonaServicio.ListarZonas();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ZonaRespondeDto crear(@Valid @RequestBody ZonaRequestDto request) {
        return zonaServicio.crearZona(request);
    }

    @PutMapping("/{id}")
    public ZonaRespondeDto actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody ZonaRequestDto request) {
        return zonaServicio.actualizarZona(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desactivar(@PathVariable UUID id) {
        zonaServicio.desactivarZona(id);
    }
}
