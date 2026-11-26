package vista;

import java.util.List;

import modelo.ContenidoAudiovisual;

public interface Vista {

    void mostrarMenu();

    void mostrarMensaje(String mensaje);

    void mostrarDetalles(ContenidoAudiovisual contenido);

    void mostrarResumen(List<ContenidoAudiovisual> contenidos);

    String leerTexto(String mensaje);

    int leerEntero(String mensaje, int minimo);

    boolean leerSiNo(String mensaje);
}