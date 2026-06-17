package ec.edu.espe.zonas.services;

import ec.edu.espe.zonas.dto.UsuarioCreateRequest;
import ec.edu.espe.zonas.dto.UsuarioResponse;
import ec.edu.espe.zonas.entidades.Persona;
import ec.edu.espe.zonas.entidades.Usuario;
import ec.edu.espe.zonas.repositorios.PersonaRepositorio;
import ec.edu.espe.zonas.repositorios.UsuarioRepositorio;
import ec.edu.espe.zonas.utils.IdentidadMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServicio {

    private final UsuarioRepositorio usuarioRepositorio;
    private final PersonaRepositorio personaRepositorio;
    private final PasswordEncoder passwordEncoder;
    private final IdentidadMapper mapper;

    @Transactional
    public UsuarioResponse crear(UsuarioCreateRequest request) {
        Persona persona = personaRepositorio.findById(request.getPersonId())
                .filter(Persona::isActive)
                .orElseThrow(() -> new RuntimeException("Persona activa no encontrada"));

        if (usuarioRepositorio.existsById(request.getPersonId())) {
            throw new IllegalStateException("La persona ya tiene un usuario");
        }

        String username = generarUsername(persona);

        Usuario usuario = Usuario.builder()
                .idPerson(persona.getId())
                .persona(persona)
                .username(username)
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .build();
        usuarioRepositorio.save(usuario);
        return obtener(persona.getId());
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listar() {
        return usuarioRepositorio.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponse obtener(UUID id) {
        return mapper.toResponse(buscarEntidad(id));
    }

    @Transactional(readOnly = true)
    public Usuario buscarEntidad(UUID id) {
        return usuarioRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    private String generarUsername(Persona persona) {
        String primerNombre = normalizar(persona.getFirstName());
        String segundoNombre = normalizar(persona.getMiddleName());
        String apellidoPaterno = normalizar(
                persona.getLastName() == null
                        ? ""
                        : persona.getLastName().trim().split("\\s+")[0]);

        if (primerNombre.isBlank() || segundoNombre.isBlank() || apellidoPaterno.isBlank()) {
            throw new IllegalArgumentException(
                    "La persona debe tener primer nombre, segundo nombre y apellido paterno para generar el username");
        }

        String base = primerNombre.charAt(0) + ""
                + segundoNombre.charAt(0)
                + apellidoPaterno;

        Set<String> usados = usuarioRepositorio.findByUsernameStartingWith(base)
                .stream()
                .map(Usuario::getUsername)
                .collect(Collectors.toSet());

        int secuencia = 1;
        String username = construirUsername(base, secuencia);
        while (usados.contains(username)) {
            secuencia++;
            username = construirUsername(base, secuencia);
        }
        return username;
    }

    private String construirUsername(String base, int secuencia) {
        String suffix = String.valueOf(secuencia);
        int baseMaxLength = Math.max(1, 15 - suffix.length());
        return base.substring(0, Math.min(base.length(), baseMaxLength)) + suffix;
    }

    private String normalizar(String value) {
        if (value == null) {
            return "";
        }
        String sinTildes = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return sinTildes
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]", "");
    }
}
