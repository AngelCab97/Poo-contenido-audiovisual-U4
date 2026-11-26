package persistencia;

import modelo.ContenidoAudiovisual;

public interface FormatoContenido {

    String getTipo();

    boolean admite(ContenidoAudiovisual contenido);

    ContenidoAudiovisual leer(String titulo, int duracion, String genero, String detalle);

    String escribirDetalle(ContenidoAudiovisual contenido);
}