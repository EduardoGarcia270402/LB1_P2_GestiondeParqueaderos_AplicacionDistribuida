package ec.edu.espe.zonas.services;

import ec.edu.espe.zonas.dto.PersonaCreateRequest;
import ec.edu.espe.zonas.dto.PersonaResponse;
import ec.edu.espe.zonas.dto.PersonaUpdateRequest;
import ec.edu.espe.zonas.entidades.Persona;
import ec.edu.espe.zonas.repositorios.PersonaRepositorio;
import ec.edu.espe.zonas.utils.IdentidadMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PersonaServicio {

    private final PersonaRepositorio personaRepositorio;
    private final IdentidadMapper mapper;

    @Transactional
    public PersonaResponse crear(PersonaCreateRequest request) {
        if (personaRepositorio.existsByDni(request.getDni())
                || personaRepositorio.existsByEmail(request.getEmail())
                || personaRepositorio.existsByPhone(request.getPhone())) {
            throw new IllegalStateException(
                    "Ya existe una persona con el mismo DNI, email o telefono");
        }

        Persona persona = Persona.builder()
                .dni(request.getDni())
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .nationality(request.getNationality())
                .build();
        return mapper.toResponse(personaRepositorio.save(persona));
    }

    @Transactional(readOnly = true)
    public List<PersonaResponse> listar() {
        return personaRepositorio.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PersonaResponse obtener(UUID id) {
        return mapper.toResponse(buscarEntidad(id));
    }

    @Transactional
    public PersonaResponse actualizar(UUID id, PersonaUpdateRequest request) {
        Persona persona = buscarEntidad(id);
        if ((request.getDni() != null
                && personaRepositorio.existsByDniAndIdNot(request.getDni(), id))
                || (request.getEmail() != null
                && personaRepositorio.existsByEmailAndIdNot(request.getEmail(), id))
                || (request.getPhone() != null
                && personaRepositorio.existsByPhoneAndIdNot(request.getPhone(), id))) {
            throw new IllegalStateException(
                    "Ya existe una persona con el mismo DNI, email o telefono");
        }

        if (request.getDni() != null) persona.setDni(request.getDni());
        if (request.getFirstName() != null) persona.setFirstName(request.getFirstName());
        if (request.getMiddleName() != null) persona.setMiddleName(request.getMiddleName());
        if (request.getLastName() != null) persona.setLastName(request.getLastName());
        if (request.getEmail() != null) persona.setEmail(request.getEmail());
        if (request.getPhone() != null) persona.setPhone(request.getPhone());
        if (request.getAddress() != null) persona.setAddress(request.getAddress());
        if (request.getNationality() != null) persona.setNationality(request.getNationality());

        return mapper.toResponse(personaRepositorio.save(persona));
    }

    @Transactional
    public void desactivar(UUID id) {
        Persona persona = buscarEntidad(id);
        persona.setActive(false);
        personaRepositorio.save(persona);
    }

    @Transactional(readOnly = true)
    public Persona buscarEntidad(UUID id) {
        return personaRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
    }
}
