package ec.edu.espe.zonas.controllers;

import ec.edu.espe.zonas.dto.PersonaCreateRequest;
import ec.edu.espe.zonas.dto.PersonaResponse;
import ec.edu.espe.zonas.dto.PersonaUpdateRequest;
import ec.edu.espe.zonas.services.PersonaServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/personas")
@RequiredArgsConstructor
public class PersonaController {

    private final PersonaServicio personaServicio;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PersonaResponse crear(@Valid @RequestBody PersonaCreateRequest request) {
        return personaServicio.crear(request);
    }

    @GetMapping
    public List<PersonaResponse> listar() {
        return personaServicio.listar();
    }

    @GetMapping("/{id}")
    public PersonaResponse obtener(@PathVariable UUID id) {
        return personaServicio.obtener(id);
    }

    @PatchMapping("/{id}")
    public PersonaResponse actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody PersonaUpdateRequest request) {
        return personaServicio.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desactivar(@PathVariable UUID id) {
        personaServicio.desactivar(id);
    }
}
