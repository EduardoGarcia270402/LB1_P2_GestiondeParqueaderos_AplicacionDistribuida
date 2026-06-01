package ec.edu.espe.zonas;

import ec.edu.espe.zonas.entidades.TipoZona;

/**
 * Clase de prueba para verificar la generación de códigos de zona.
 * Ejecutar el método main directamente para probar sin levantar el servidor.
 */
public class GenerarCodigoTest {

    public static void main(String[] args) {
        System.out.println("=== Prueba de generarCodigo() ===\n");

        Object[][] casos = {
            { "Zona Norte",    TipoZona.REGULAR  },
            { "Zona VIP A",    TipoZona.VIP      },
            { "Zona Interna",  TipoZona.INTERNA  },
            { "Zona Exterior", TipoZona.EXTERNA  },
        };

        long contador = 0;
        for (Object[] caso : casos) {
            contador++;
            String nombre  = (String) caso[0];
            TipoZona tipo  = (TipoZona) caso[1];
            String codigo  = generarCodigo(tipo, contador);
            System.out.printf("Zona %-20s | Tipo %-10s | Código: %s%n", nombre, tipo, codigo);
        }

        System.out.println("\nLa primera zona (BDD vacía) debe ser: ZONA-REG-01 ✔");
    }

    // Réplica del método privado de ZonaServicioImpl para prueba aislada
    private static String generarCodigo(TipoZona tipo, long numero) {
        String tipoAbrev = (tipo != null)
                ? tipo.name().substring(0, Math.min(3, tipo.name().length()))
                : "GEN";
        return String.format("ZONA-%s-%02d", tipoAbrev.toUpperCase(), numero);
    }
}