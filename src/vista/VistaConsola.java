package vista;

import java.util.List;
import java.util.Scanner;

import modelo.ContenidoAudiovisual;

public class VistaConsola implements Vista {

    private final Scanner teclado;

    public VistaConsola(Scanner teclado) {
        this.teclado = teclado;
    }

    @Override
    public void mostrarMenu() {
        System.out.println();
        System.out.println("===== GESTOR DE CONTENIDO AUDIOVISUAL =====");
        System.out.println("1. Registrar película");
        System.out.println("2. Registrar serie de TV");
        System.out.println("3. Registrar documental");
        System.out.println("4. Registrar podcast");
        System.out.println("5. Registrar concierto");
        System.out.println("6. Listar todo el contenido");
        System.out.println("7. Buscar por título");
        System.out.println("8. Agregar actor/temporada/investigador/invitado/canción");
        System.out.println("9. Acciones especiales (publicar episodio, duración del setlist)");
        System.out.println("10. Eliminar contenido");
        System.out.println("11. Cargar datos de ejemplo");
        System.out.println("12. Cargar contenido desde CSV");
        System.out.println("13. Guardar contenido en CSV");
        System.out.println("0. Salir");
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    @Override
    public void mostrarDetalles(ContenidoAudiovisual contenido) {
        System.out.println(contenido.obtenerDetalles());
        System.out.println();
    }

    @Override
    public void mostrarResumen(List<ContenidoAudiovisual> contenidos) {
        for (ContenidoAudiovisual contenido : contenidos) {
            System.out.println("[" + contenido.getId() + "] " + contenido.getTitulo()
                    + " (" + contenido.getClass().getSimpleName() + ")");
        }
    }

    @Override
    public String leerTexto(String mensaje) {
        String texto;
        do {
            System.out.print(mensaje);
            texto = teclado.nextLine().trim();
            if (texto.isEmpty()) {
                System.out.println("Este campo no puede estar vacío.");
            }
        } while (texto.isEmpty());
        return texto;
    }

    @Override
    public int leerEntero(String mensaje, int minimo) {
        while (true) {
            System.out.print(mensaje);
            try {
                int valor = Integer.parseInt(teclado.nextLine().trim());
                if (valor >= minimo) {
                    return valor;
                }
                System.out.println("El valor debe ser mayor o igual a " + minimo + ".");
            } catch (NumberFormatException e) {
                System.out.println("Ingresa un número entero válido.");
            }
        }
    }

    @Override
    public boolean leerSiNo(String mensaje) {
        while (true) {
            System.out.print(mensaje + " (s/n): ");
            String respuesta = teclado.nextLine().trim().toLowerCase();
            if (respuesta.equals("s")) return true;
            if (respuesta.equals("n")) return false;
            System.out.println("Responde con 's' o 'n'.");
        }
    }
}