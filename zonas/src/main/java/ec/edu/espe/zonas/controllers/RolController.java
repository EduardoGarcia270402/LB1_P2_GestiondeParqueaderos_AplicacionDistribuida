package ec.edu.espe.zonas.controllers;

import ec.edu.espe.zonas.dto.RolCreateRequest;
import ec.edu.espe.zonas.dto.RolResponse;
import ec.edu.espe.zonas.services.RolServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RolController {

    private final RolServicio rolServicio;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RolResponse crear(@Valid @RequestBody RolCreateRequest request) {
        return rolServicio.crear(request);
    }

    @GetMapping
    public List<RolResponse> listar() {
        return rolServicio.listar();
    }
}
