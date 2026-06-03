package ec.edu.espe.zonas;

import ec.edu.espe.zonas.entidades.Espacio;
import ec.edu.espe.zonas.entidades.EstadoEspacio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CambiarEstadoMain {

    public static void main(String[] args) {
        List<Espacio> espacios = new ArrayList<>();

        Espacio e1 = Espacio.builder()
                .id(UUID.randomUUID())
                .codigo("ESP-001")
                .descripcion("Cercano a la entrada")
                .tipo(null)
                .activo(true)
                .estado(EstadoEspacio.DISPONIBLE)
                .fechaCreacion(LocalDateTime.now())
                .fechaModificacion(LocalDateTime.now())
                .build();

        Espacio e2 = Espacio.builder()
                .id(UUID.randomUUID())
                .codigo("ESP-002")
                .descripcion("Cerca salida")
                .tipo(null)
                .activo(true)
                .estado(EstadoEspacio.OCUPADO)
                .fechaCreacion(LocalDateTime.now())
                .fechaModificacion(LocalDateTime.now())
                .build();

        espacios.add(e1);
        espacios.add(e2);

        System.out.println("Estados iniciales:");
        espacios.forEach(s -> System.out.printf("%s -> %s%n", s.getCodigo(), s.getEstado()));

        System.out.println();
        // Caso 1: intentar cambiar DISPONIBLE -> DISPONIBLE (debe fallar)
        try {
            System.out.println("Intentando cambiar ESP-001 de DISPONIBLE a DISPONIBLE...");
            cambiarEstado(espacios, e1.getId(), EstadoEspacio.DISPONIBLE);
        } catch (IllegalStateException ex) {
            System.out.println("Validación: " + ex.getMessage());
        }

        // Caso 2: cambiar DISPONIBLE -> OCUPADO (debe pasar)
        try {
            System.out.println("Intentando cambiar ESP-001 de DISPONIBLE a OCUPADO...");
            cambiarEstado(espacios, e1.getId(), EstadoEspacio.OCUPADO);
            System.out.println("Cambio realizado: " + e1.getCodigo() + " -> " + e1.getEstado());
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        // Caso 3: cambiar OCUPADO -> DISPONIBLE (debe pasar)
        try {
            System.out.println("Intentando cambiar ESP-002 de OCUPADO a DISPONIBLE...");
            cambiarEstado(espacios, e2.getId(), EstadoEspacio.DISPONIBLE);
            System.out.println("Cambio realizado: " + e2.getCodigo() + " -> " + e2.getEstado());
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        System.out.println();
        System.out.println("Estados finales:");
        espacios.forEach(s -> System.out.printf("%s -> %s%n", s.getCodigo(), s.getEstado()));
    }

    private static void cambiarEstado(List<Espacio> espacios, UUID idEspacio, EstadoEspacio nuevoEstado) {
        Espacio e = espacios.stream()
                .filter(s -> s.getId().equals(idEspacio))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Espacio no encontrado con id: " + idEspacio));

        if (e.getEstado() == nuevoEstado) {
            throw new IllegalStateException("No se puede cambiar de " + nuevoEstado + " a " + nuevoEstado);
        }

        // Validaciones adicionales pueden añadirse aquí

        e.setEstado(nuevoEstado);
        e.setFechaModificacion(LocalDateTime.now());
    }
}
